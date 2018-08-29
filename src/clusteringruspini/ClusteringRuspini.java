package clusteringruspini;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClusteringRuspini {

    static int k = 4, n, N;
    static int newCentroidNumber = 0;
    static float xCentroid, yCentroid;
    static List<Ruspini> datasetRuspini = new ArrayList<>();
    static List<Ruspini> dataClusterList = new ArrayList<>();
    static List<Centroid> listCentroid = new ArrayList<>();
    static List<Centroid> listNewCentroid = new ArrayList<>();
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        readFile();

        String ans = null;

        do {
            int choose = Menu();
            System.out.flush();
            switch (choose) {
                case 1:
                    createCentroid();
                    do {
                        calculateDistance();
                        newCentroidNumber++;
                    } while (!createNewCentroid());
                    printCluster();
                    break;
                case 2:
                    N = dataClusterList.size();
                    n = N;
                    hierarchicalClustering();
                    printCluster();                   
                    break;
//                case 3 : case 4 : case 5 :
//                    System.out.println("Not ready");
//                    break;
                default:
                    System.out.println("Error input");
                    break;
            }
            System.out.println("Back to main menu (y/n) ?");
            ans = input.next();
            clearScreen();

        } while (ans.equals("y") || ans.equals("Y"));

    }

    private static void createCentroid() {
        Random rand = new Random();

        for (int i = 0; i < k; i++) {
            xCentroid = rand.nextInt(datasetRuspini.size() - 1);
            yCentroid = rand.nextInt(datasetRuspini.size() - 1);
            listCentroid.add(new Centroid(xCentroid, yCentroid));
        }
        System.out.println("\nFirst Centroid : ");
        int number = 0;
        for (Centroid centroid : listCentroid) {
            System.out.println("Centroid " + (number + 1) + " : (" + centroid.getxCentroid() + " , " + centroid.getyCentroid() + ")");
            number++;
        }
    }

    private static void calculateDistance() {

        for (Ruspini dataCluster : dataClusterList) {
            int cluster = 0;
            float distance = Float.MAX_VALUE;
            for (Centroid centroid : listCentroid) {
                float tempDistance = (float) Math.sqrt(Math.pow(dataCluster.getX() - centroid.getxCentroid(), 2)
                        + Math.pow(dataCluster.getY() - centroid.getyCentroid(), 2));

                if (tempDistance < distance) {
                    distance = tempDistance;
                    dataCluster.setCluster(cluster);
                }
                cluster++;
            }
        }
    }

    private static boolean createNewCentroid() {
        listNewCentroid.clear();
        for (int i = 0; i < k; i++) {
            int totalX = 0;
            int totalY = 0;
            int totalInCluster = 0;
            for (int j = 0; j < dataClusterList.size(); j++) {
                if (dataClusterList.get(j).getCluster() == i) {
                    totalX += datasetRuspini.get(j).getX();
                    totalY += datasetRuspini.get(j).getY();
                    totalInCluster++;
                }
            }
            if (totalInCluster > 0) {
                float newXCentroid = totalX / totalInCluster;
                float newYCentroid = totalY / totalInCluster;
                listNewCentroid.add(i, new Centroid(newXCentroid, newYCentroid));
            } else {
                listNewCentroid.add(i, listCentroid.get(i));
            }
        }
        System.out.println("\nNew Centroid " + newCentroidNumber + " : ");
        int number = 0;
        for (Centroid centroid : listNewCentroid) {
            System.out.println("Centroid " + (number + 1) + " : (" + centroid.getxCentroid() + " , " + centroid.getyCentroid() + ")");
            number++;
        }

        boolean equal = true;
        for (int i = 0; i < k; i++) {
            if (listCentroid.get(i).compareTo(listNewCentroid.get(i)) == 0) {
                equal = false;
            }
        }

        if (!equal) {
            listCentroid.clear();
            listCentroid.addAll(listNewCentroid);
        }

        return equal;
    }

    private static void calculateNewDistance() {
        for (Ruspini dataCluster : datasetRuspini) {
            int cluster = 0;
            float distance = Float.MAX_VALUE;
            for (Centroid centroid : listNewCentroid) {
                float tempDistance = (float) Math.sqrt(Math.pow(dataCluster.getX() - centroid.getxCentroid(), 2)
                        + Math.pow(dataCluster.getY() - centroid.getyCentroid(), 2));

                if (tempDistance < distance) {
                    distance = tempDistance;
                    dataCluster.setCluster(cluster);
                }
                cluster++;
            }
            dataClusterList.add(dataCluster);
        }
        createNewCentroid();
    }

    private static void printCluster() {
        System.out.println("\nx\ty\tclass");
        for (Ruspini ruspini : dataClusterList) {
            System.out.println(ruspini.getX() + "\t"
                    + ruspini.getY() + "\t"
                    + (ruspini.getCluster() + 1));
        }
    }

    private static void hierarchicalClustering() {
        initializationCluster();

        while (n > k) {
            singleLinkageProcess();
            n--;
        }

    }

    private static void initializationCluster() {
        for (int i = 0; i < dataClusterList.size(); i++) {
            dataClusterList.get(i).setCluster(i + 1);
        }
    }
    
    private static void singleLinkageProcess() {
        Ruspini data1 = new Ruspini();
        Ruspini data2 = new Ruspini();
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < N; i++) {
            Ruspini dataTmp1 = dataClusterList.get(i);

            for (int j = i + 1; j < N; j++) {
                Ruspini dataTmp2 = dataClusterList.get(j);

                if (dataTmp1.getCluster() == dataTmp2.getCluster()) {
                    continue;
                }

                double distance = Math.sqrt(Math.pow(dataTmp1.getX() - dataTmp2.getX(), 2)
                        + Math.pow(dataTmp1.getY() - dataTmp2.getY(), 2));

                if (minDistance > distance) {
                    minDistance = distance;
                    data1 = dataTmp1;
                    data2 = dataTmp2;
                }
            }

        }
        //Merge Cluster
        if (isClustered(data1.getCluster()) && isClustered(data2.getCluster())) {
            if (getTotalClusterMember(data1.getCluster()) > getTotalClusterMember(data2.getCluster())) {
                mergeCluster(data2.getCluster(), data1.getCluster());
            } else {
                mergeCluster(data1.getCluster(), data2.getCluster());
            }
        } else if (isClustered(data1.getCluster()) && !isClustered(data2.getCluster())) {
            int index = dataClusterList.indexOf(data2);
            dataClusterList.get(index).setCluster(data1.getCluster());
        } else if (!isClustered(data1.getCluster()) && isClustered(data2.getCluster())) {
            int index = dataClusterList.indexOf(data1);
            dataClusterList.get(index).setCluster(data2.getCluster());
        } else {
            int index = dataClusterList.indexOf(data2);
            dataClusterList.get(index).setCluster(data1.getCluster());
        }
    }

    private static boolean isClustered(int cluster) {
        int countCluster = 0;

        for (Ruspini dataCluster : dataClusterList) {
            if (dataCluster.getCluster() == cluster) {
                countCluster++;
            }
        }

        return countCluster > 1;
    }

    private static int getTotalClusterMember(int cluster) {
        int countCluster = 0;

        for (Ruspini dataCluster : dataClusterList) {
            if (dataCluster.getCluster() == cluster) {
                countCluster++;
            }
        }

        return countCluster;
    }

    private static void mergeCluster(int clusterSource, int clusterTarget) {
        for (Ruspini dataCluster : dataClusterList) {
            if (dataCluster.getCluster() == clusterSource) {
                dataCluster.setCluster(clusterTarget);
            }
        }
    }

    private static void readFile() {
        BufferedReader br;
        String line;

        try {
            br = new BufferedReader(new FileReader("src\\resources\\DatasetRuspini.csv"));
            while ((line = br.readLine()) != null) {

                String[] ruspiniData = line.split(",");

                Ruspini ruspini = new Ruspini();
                ruspini.setX(Integer.parseInt(ruspiniData[1]));
                ruspini.setY(Integer.parseInt(ruspiniData[2]));

                datasetRuspini.add(ruspini);
            }
            dataClusterList.addAll(datasetRuspini); //Initialiaze Data
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    private static int Menu() {
        System.out.println("\nClustering : ");
        System.out.println("1. K-Means\n"
                + "2. Single Linkage");
//                + "3. Complete Linkage\n"
//                + "4. Centroid Linkeage\n"
//                + "5. Average Linkage");
        System.out.print("Your choose : ");
        int my_menu = input.nextInt();

        return my_menu;
    }
    public static void clearScreen(){
        System.out.print("\u001b[2J");
        System.out.flush();
        try {
            Robot pressbot = new Robot();
            pressbot.keyPress(17); // Holds CTRL key.
            pressbot.keyPress(76); // Holds L key.
            pressbot.keyRelease(17); // Releases CTRL key.
            pressbot.keyRelease(76); // Releases L key.
        } catch (AWTException ex) {
            Logger.getLogger(ClusteringRuspini.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClusteringRuspini.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}