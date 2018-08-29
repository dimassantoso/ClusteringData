package clusteringruspini;

public class Ruspini {

    private int x;
    private int y;
    private int cluster;

    public Ruspini() {
    }

    public Ruspini(int x, int y, int cluster) {
        this.x = x;
        this.y = y;
        this.cluster = cluster;
    }
    
    public Ruspini(int x, int y) {
        this.x = x;
        this.y = y;
        this.cluster = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

}
