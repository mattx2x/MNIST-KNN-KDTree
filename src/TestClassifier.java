import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestClassifier {
    public static void main(String[] args) throws FileNotFoundException {
        LabeledPoint[] train, test;
        int dimensions = 784, trainSize = 10000, testSize = 200;

        train = new LabeledPoint[trainSize];
        test = new LabeledPoint[testSize];

        Scanner scTrain = new Scanner(new File("mnist/train.csv"));
        Scanner scTrainL = new Scanner(new File("mnist/train_labels.csv"));
        Scanner scTest = new Scanner(new File("mnist/test.csv"));
        Scanner scTestL = new Scanner(new File("mnist/test_labels.csv"));

        int i = 0;
        while (scTrain.hasNext()) {
            float[] coordinates = new float[dimensions];
            Scanner sc2 = new Scanner(scTrain.nextLine());
            sc2.useDelimiter(",");
            int j = 0;
            while (sc2.hasNext())
                coordinates[j++] = sc2.nextFloat();
            train[i++] = new LabeledPoint(coordinates, scTrainL.nextInt());
        }

        i = 0;
        while (scTest.hasNext()) {
            float[] coordinates = new float[dimensions];
            Scanner sc2 = new Scanner(scTest.nextLine());
            sc2.useDelimiter(",");
            int j = 0;
            while (sc2.hasNext())
                coordinates[j++] = sc2.nextFloat();
            test[i++] = new LabeledPoint(coordinates, scTestL.nextInt());
        }

        KNNClassifier knnClassifier = new KNNClassifier(train, 5);
        System.out.println(knnClassifier.accuracy(test) * 100 + "%");

    }
}
