public class KNNQueue {
    private Point[] queuePoints;
    private Point targetPoint;
    private final int SIZE;
    public int numberOfElements;

    public KNNQueue(Point targetPoint, int k) {
        this.targetPoint = targetPoint;
        this.SIZE = k;
        queuePoints = new Point[SIZE + 1];  // the queuePoints[SIZE] doesn't belong to the queue
        numberOfElements = 0;
    }

    public void add(Point point) {

        if(numberOfElements == 0)
            queuePoints[0] = point;

        for(int i = numberOfElements - 1; i >= 0; i--) {
            if(point.distanceFrom(targetPoint) < queuePoints[i].distanceFrom(targetPoint)) {
                Point temp = queuePoints[i];
                queuePoints[i] = point;
                queuePoints[i+1] = temp;
            } else if (i == numberOfElements-1) {
                queuePoints[i+1] = point;
                break;
            } else
                break;
        }

        if(numberOfElements < SIZE)
            numberOfElements ++;

    }

    public boolean isFull() {
        return numberOfElements == SIZE;
    }

    public double rearDistance() {
        return queuePoints[numberOfElements-1].distanceFrom(targetPoint);
    }

    public Point[] getPoints() {
        Point[] points = new Point[numberOfElements];
        for (int i = 0; i < numberOfElements; i++)    // handle the case queue is not full
            points[i] = queuePoints[i];

        return points;
    }

}
