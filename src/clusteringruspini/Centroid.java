package clusteringruspini;

public class Centroid implements Comparable<Centroid> {

    private float xCentroid;
    private float yCentroid;

    public Centroid() {
    }

    public Centroid(float xCentroid, float yCentroid) {
        this.xCentroid = xCentroid;
        this.yCentroid = yCentroid;
    }

    public float getxCentroid() {
        return xCentroid;
    }

    public void setxCentroid(float xCentroid) {
        this.xCentroid = xCentroid;
    }

    public float getyCentroid() {
        return yCentroid;
    }

    public void setyCentroid(float yCentroid) {
        this.yCentroid = yCentroid;
    }

    @Override
    public int compareTo(Centroid centroid) {
        if (this.getxCentroid() == centroid.getxCentroid() && this.getyCentroid() == centroid.getyCentroid()) {
            return 1;
        }
        return 0;
    }

}
