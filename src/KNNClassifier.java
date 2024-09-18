public class KNNClassifier {
    private final int K;
    public KDTree kdTree;

    public KNNClassifier(LabeledPoint[] labeledPoints, int K) {
        kdTree = new KDTree(labeledPoints[0].getLength());
        kdTree.createTree(labeledPoints);

        this.K = K;
    }

    public int classify(Point point) {
        return getDominant(kdTree.findMNearest(point, K));
    }

    public int[] classifyAll(Point[] points) {
        int[] predictedLabels = new int[points.length];
        for (int i = 0; i < points.length; i++)
            predictedLabels[i] = classify(points[i]);

        return predictedLabels;
    }

    public int getDominant(Point[] points) {
        LabeledPoint[] lPoints = new LabeledPoint[points.length];
        int i = 0;
        for(Point temp : points)
            lPoints[i++] = (LabeledPoint)temp;  // cast to labeled points

        int[] arr = new int[10];
        for(i = 0; i < points.length; i++)
            arr[lPoints[i].getLabel()] ++;

        int dominant = 0;
        for (i = 1; i < 10; i++)
            if (arr[i] > arr[dominant])
                dominant = i;

        return dominant;

    }

    public float accuracy(LabeledPoint[] labeledPoints) {
        int corrects = 0;
        int [] predictedLabels = classifyAll(labeledPoints);
        for(int i=0; i < labeledPoints.length; i++)
            if (labeledPoints[i].getLabel() == predictedLabels[i])
                corrects ++;

        return (float)corrects / (float)(labeledPoints.length);
    }


}
