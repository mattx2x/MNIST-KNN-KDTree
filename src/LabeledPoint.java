public class LabeledPoint extends Point{
    private int label;

    public LabeledPoint(float[] coordinates, int label) {
        super(coordinates);
        this.label = label;
    }

    public int getLabel() {
        return label;
    }

}
