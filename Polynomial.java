import java.util.*;
import java.io.*;


public class Polynomial {

    static int [][] matrix;
    static int [] coefs;           // Set of coefficients of the resulting polynomial
    static int degree;

    public static void main (String [] args) {

        // Loads all tuples into ArrayList
        Scanner inFile = null;
        ArrayList <Tuple> vals = new ArrayList <Tuple> ();

        try {
            inFile = new Scanner(new File ("text.txt"));
        }
        catch (IOException e) {
            System.out.println(e);
        }

        degree = Integer.parseInt(inFile.nextLine());
        String[] line = new String[2];

        for (int i = 0; i < degree; i++) {
            line = inFile.nextLine().split(" ");
            Tuple t = new Tuple(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
            System.out.println(t);
            vals.add(t);
        }

        coefs = new int[degree];

        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        System.out.println(Arrays.deepToString(matrix));
        matrix = shrink(matrix);
        System.out.println(Arrays.deepToString(matrix));

    }

    // Finds determinant of matrix

    /*
    public int findDet (int [][] matrix) {
        if (matrix[0].length == 2) {
            return (matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0]);
        }
        else {

        }
    }
    */

    // Removes the first row and first column of a given matrix
    // and returns the new matrix
    public static int[][] shrink (int [][] matrix) {
        int deg = matrix.length;
        matrix = Arrays.copyOfRange(matrix, 1, deg);
        for (int i = 0; i < deg-1; i ++) {
            matrix[i] = Arrays.copyOfRange(matrix[i], 1, deg);
        }

        return matrix;
    }
}

class Tuple {
    int x, y;
    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
    }
}