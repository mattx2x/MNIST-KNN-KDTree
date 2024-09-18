public class Table {
    private KDTree kdTree;
    private String[] properties;

    public Table(Point[] records, String[] properties) {
        kdTree = new KDTree(properties.length);
        kdTree.createTree(records);
        this.properties = properties;
    }

    public Point[] search(float[] lowerBounds, float[] upperBounds) {
        if(!isValidBounds(lowerBounds, upperBounds)) {
            System.out.println("invalid bounds!!!");
            System.exit(1);
        }

        return kdTree.searchRange(lowerBounds, upperBounds);
    }

    private boolean isValidBounds(float[] lowerBounds, float[] upperBounds) {
        if(lowerBounds.length != upperBounds.length)
            return false;
        else if (lowerBounds.length != properties.length)
            return false;
        else
            for (int i = 0; i < lowerBounds.length; i++)
                if(lowerBounds[i] > upperBounds[i])
                    return false;

        return true;
    }
}
