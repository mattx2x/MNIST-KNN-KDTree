public class KDTree {
    public KDTNode root;
    private final int K;
    private int numberOfNodes;
    private int inRangePoints;
    private Point[] rangePoints;
    private KNNQueue queue;
    private Point nearestPoint;

    private class KDTNode {
        public Point point;
        public KDTNode left, right;
        public KDTNode(Point point) {
            this.point = point;
            left = right = null;
        }
    }

    public KDTree(int K) {
        this.K = K;
        root = null;
        numberOfNodes = 0;
        inRangePoints = 0;
    }

    public KDTree createTree(Point[] points) {
        for(Point point : points)
            insert(point);

        return this;
    }

    public void insert(Point point) {
        KDTNode parentNode, node, newNode;
        parentNode = node = root;
        newNode = new KDTNode(point);
        boolean isLeftChild = false;
        int d = 0;
        while (node != null) {
            isLeftChild = false;
            parentNode = node;
            if (point.getCoordinate(d % K) <= node.point.getCoordinate(d % K)) {
                node = node.left;
                isLeftChild = true;
            }
            else
                node = node.right;

            d++;
        }

        if(root == null)
            root = newNode;
        else {
            if(isLeftChild)
                parentNode.left = newNode;
            else
                parentNode.right = newNode;
        }

        numberOfNodes ++;
    }

    public Point findNearest(Point point) {
        nearestPoint = null;
        nnTraverse(root, 0, point);

        return nearestPoint;
    }

    public Point[] findMNearest(Point target, int m) {
        queue = new KNNQueue(target, m);
        knnTraverse(root, target, 0);

        return queue.getPoints();
    }

    public Point[] searchRange(float[] lowerBounds, float[] upperBounds) {
        inRangePoints = 0;
        rangePoints = new Point[numberOfNodes];
        rangeTraverse(root, 0, lowerBounds, upperBounds);

        //reduce size of rangePoints array
        Point[] temp = new Point[inRangePoints];
        for (int i = 0; i < inRangePoints; i++)
            temp[i] = rangePoints[i];
        rangePoints = temp;

        return rangePoints;
    }

    public boolean pointExists(float[] coordinates) {
        return existTraverse(root, 0, coordinates);
    }

    public boolean deletePoint(float[] point) {
        return deleteTraverse(null, root, point, 0, false);
    }

    //****************************  Helper methods  ***********************************

    private void nnTraverse(KDTNode node, int d, Point target) {
        if(node == null)
            return;

        if (target.getCoordinate(d % K) > node.point.getCoordinate(d % K)) {
            nnTraverse(node.right, d + 1, target);
            if ((nearestPoint == null) || node.point.distanceFrom(target) < nearestPoint.distanceFrom(target))
                nearestPoint = node.point;
            if (Math.abs(node.point.getCoordinate(d % K) - target.getCoordinate(d % K)) < nearestPoint.distanceFrom(target))
                nnTraverse(node.left, d + 1, target);
        }

        else {
            nnTraverse(node.left, d + 1, target);
            if ((nearestPoint == null) || node.point.distanceFrom(target) < nearestPoint.distanceFrom(target))
                nearestPoint = node.point;
            if (Math.abs(node.point.getCoordinate(d % K) - target.getCoordinate(d % K)) < nearestPoint.distanceFrom(target))
                nnTraverse(node.right, d + 1, target);
        }
    }

    private void knnTraverse(KDTNode node, Point target, int d) {
        if(node == null)
            return;

        if (target.getCoordinate(d % K) > node.point.getCoordinate(d % K)) {
            knnTraverse(node.right, target, d + 1);
            queue.add(node.point);
            if (!queue.isFull() || Math.abs(node.point.getCoordinate(d % K) - target.getCoordinate(d % K)) < queue.rearDistance())
                knnTraverse(node.left, target, d + 1);
        }

        else {
            knnTraverse(node.left, target, d + 1);
            queue.add(node.point);
            if (!queue.isFull() || Math.abs(node.point.getCoordinate(d % K) - target.getCoordinate(d % K)) < queue.rearDistance())
                knnTraverse(node.right, target, d + 1);
        }

    }

    private void rangeTraverse(KDTNode node, int d, float[] lowerBounds, float[] upperBounds) {
        if (node == null)
            return;

        //traverse left
        if (node.point.getCoordinate(d % K) >= upperBounds[d % K]) {
            if (isInRange(node, lowerBounds, upperBounds))
                addToRangePoints(node.point);
            rangeTraverse(node.left, d + 1, lowerBounds, upperBounds);
        }

        //traverse right
        else if (node.point.getCoordinate(d % K) < lowerBounds[d % K])
            rangeTraverse(node.right, d + 1, lowerBounds, upperBounds);

            //traverse both
        else {
            if (isInRange(node, lowerBounds, upperBounds))
                addToRangePoints(node.point);
            rangeTraverse(node.left, d + 1, lowerBounds, upperBounds);
            rangeTraverse(node.right, d + 1, lowerBounds, upperBounds);
        }

    }

    private void addToRangePoints(Point point) {
        rangePoints[inRangePoints ++] = point;
    }

    private boolean isInRange(KDTNode node, float[] lowerBounds, float[] upperBounds) {
        for (int i = 0; i < node.point.getLength(); i++)
            if((node.point.getCoordinate(i) < lowerBounds[i]) || (node.point.getCoordinate(i) > upperBounds[i]))
                return false;

        return true;
    }

    private boolean existTraverse(KDTNode node, int d, float[] coordinates) {
        if (node == null)
            return false;

        if (node.point.getCoordinate(d % K) == coordinates[d % K]  &&  areSamePoints(node.point, coordinates))
            return true;

        if (node.point.getCoordinate(d % K) >= coordinates[d % K])
            return existTraverse(node.left, d + 1, coordinates);

        else
            return existTraverse(node.right, d + 1, coordinates);

    }

    private boolean areSamePoints(Point point, float[] coordinates) {
        for (int i = 0; i < coordinates.length; i++)
            if(coordinates[i] != point.getCoordinate(i))
                return false;

        return true;
    }

    private boolean areSamePoints(Point p1, Point p2) {
        for (int i = 0; i < p1.getLength(); i++)
            if (p1.getCoordinate(i) != p2.getCoordinate(i))
                return  false;

        return true;
    }

    private boolean deleteTraverse(KDTNode parent, KDTNode node, float[] coordinates, int d, boolean isLeft) {
        if (node == null)
            return false;

        int cd = d % K;

        if (node.point.getCoordinate(cd) == coordinates[cd]  &&  areSamePoints(node.point, coordinates)) {
            delete(parent, node, node.point, d, isLeft);
            return true;
        }

        if (node.point.getCoordinate(cd) >= coordinates[cd])
            return deleteTraverse(node, node.left, coordinates, d + 1, true);

        else
            return deleteTraverse(node, node.right, coordinates, d + 1, false);

    }

    private KDTNode min(KDTNode vertex, KDTNode left, KDTNode right, int dime) {
        KDTNode res = vertex;
        if(left != null  &&  left.point.getCoordinate(dime) < res.point.getCoordinate(dime))
            res = left;
        if(right != null  &&  right.point.getCoordinate(dime) < res.point.getCoordinate(dime))
            res = right;

        return res;
    }

    private KDTNode findMin(KDTNode node, int dime, int depth) {
        if (node == null)
            return null;

        if (depth % K == dime) {
            if (node.left == null)
                return node;
            return findMin(node.left, dime, depth+1);
        }

        return min(node,
                findMin(node.left, dime, depth+1),
                findMin(node.right, dime, depth+1), dime);

    }

    private KDTNode delete(KDTNode parent, KDTNode node, Point point, int depth, boolean isLeft) {
        int cd = depth % K;
        if (areSamePoints(node.point, point)) {
            if (node.right != null) {
                KDTNode min = findMin(node.right, cd, depth);
                node.point = min.point;
                node.right = delete(node, node.right, min.point, depth+1, false);
            } else if (node.left != null) {
                KDTNode min = findMin(node.left, cd, depth);
                node.point = min.point;
                node.right = delete(node, node.left, min.point, depth+1, true);
            }
            else {
                if (parent != root)
                    if (isLeft)
                        parent.left = null;
                    else
                        parent.right = null;
                else
                    root = null;

                return null;
            }

            return node;
        }

        if (point.getCoordinate(cd) < node.point.getCoordinate(cd))
            node.left = delete(node, node.left, point, depth+1, true);
        else
            node.right = delete(node, node.right, point, depth+1, false);

        return node;

    }

}