import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestTable {
    public static void main(String[] args) throws FileNotFoundException {

        String[] properties;
        Point[] records;
        int col, row;
        String fileURL = "table/data.csv";

        // finding number of records
        row = 0;
        Scanner scLines = new Scanner(new File(fileURL));
        while (scLines.hasNext()) {
            scLines.nextLine();
            row ++;
        }

        records = new Point[row];
        Scanner sc = new Scanner(new File(fileURL));
        properties = "A B C D E".split(" ");
        col = properties.length;

        int i = 0;
        while (sc.hasNext()) {
            float[] coordinates = new float[col];
            String[] sArr = sc.nextLine().split(",");
            for (int j = 0; j < col; j++)
                coordinates[j] = Float.parseFloat(sArr[j]);
            records[i++] = new Point(coordinates);
        }

        Table table = new Table(records, properties);

        float[] lowerBounds = {20, 15, 10, 15, 10};
        float[] upperBounds = {50, 90, 80, 40, 85};

        Point[] result = table.search(lowerBounds, upperBounds);

        for (i = 0; i < col; i++) {
            System.out.print("       ");
            System.out.print(lowerBounds[i] + " << " + properties[i] + " << " + upperBounds[i]);
        }
        System.out.println("\n");
        for (Point point : result)
            System.out.println(point);

    }
}
