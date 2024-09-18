import java.util.Arrays;

public class Point {
    private float[] coordinates;

    public Point(float[] coordinates) {
        this.coordinates = coordinates;
    }

    public float getCoordinate(int index) {
        return coordinates[index];
    }

    public int getLength() {
        return coordinates.length;
    }

    public double distanceFrom(Point target) {
        double sum = 0;
        for(int i=0; i < coordinates.length; i++) {
            double abs = this.getCoordinate(i) - target.getCoordinate(i);
            sum += abs * abs;
        }

        return Math.sqrt(sum);
    }

    public String toString() {
        return Arrays.toString(coordinates);
    }

}
