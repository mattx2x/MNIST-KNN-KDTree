import java.util.Arrays;

public class TestKDTree {
    public static void main(String[] args) {
        Point[] points = new Point[12];
        points[0] = new Point(new float[]{57, 71, 99});
        points[1] = new Point(new float[]{59, 57, 65});
        points[2] = new Point(new float[]{75, 24, 23});
        points[3] = new Point(new float[]{65, 60, 80});
        points[4] = new Point(new float[]{78, 23, 12});
        points[5] = new Point(new float[]{57, 38, 18});
        points[6] = new Point(new float[]{11, 68, 88});
        points[7] = new Point(new float[]{81, 5, 76});
        points[8] = new Point(new float[]{50, 57, 83});
        points[9] = new Point(new float[]{94, 78, 83});
        points[10] = new Point(new float[]{20, 79, 1});
        points[11] = new Point(new float[]{67, 8, 7});

        Point target = new Point(new float[]{4, 24, 30});

        KDTree kdTree = new KDTree(3);
        kdTree.createTree(points);

        System.out.println(kdTree.findNearest(target));
        kdTree.deletePoint(new float[]{57, 38, 18});
        System.out.println(kdTree.findNearest(target));
        kdTree.deletePoint(new float[]{20, 79, 1});
        System.out.println(kdTree.findNearest(target));

    }
}
