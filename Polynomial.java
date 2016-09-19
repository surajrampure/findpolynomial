import java.util.*;
import java.io.*;


public class Polynomial {

    static int [][] matrix;
    static int [] anscolumn;
    static int [] coefs;           // Set of coefficients of the resulting polynomial
    static int degree;             // The number of data points provided AND the number of coefficients in the resulting polynomial
                                   // Note that the final polynomial will have degree (degree-1)
    static ArrayList <Tuple> vals;

    public static void main (String [] args) {

        // Loads all tuples into ArrayList
        Scanner inFile = null;
        vals = new ArrayList <Tuple> ();

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
        anscolumn = new int[degree];

        fillMatrix();

        System.out.println(Arrays.toString(anscolumn));

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

    public static void fillMatrix() {
        for (int i = 0; i < degree; i ++) {
            for (int j = 0; j < degree; j ++) {
                matrix[i][j] = (int) Math.pow(vals.get(i).getX(), degree-i-1);
            }
            anscolumn[i] = vals.get(i).getY();
        }
    }

    public static int[][] replaceC (int[][] matrix, int[] newc, int c) {
        int deg = matrix.length;
        for (int i = 0; i < deg; i ++) {
            matrix[i][c] = newc[i];
        }

        return matrix;
    }

    public static int[][] removeRC (int [][] matrix, int r, int c) {
        int deg = matrix.length;
        int[][] new_matrix = new int[deg-1][deg-1];
        for (int i = 0; i < deg; i ++) {
            if (i < r) {
                for (int j = 0; j < deg; j ++) {
                    if (j < c) {
                        new_matrix[i][j] = matrix[i][j];
                    }
                    if (j > c) {
                        new_matrix[i][j-1] = matrix[i][j];
                    }
                }
            }

            if (i > r) {
                for (int j = 0; j < deg; j ++) {
                    if (j < c) {
                        new_matrix[i-1][j] = matrix[i][j];
                    }

                    if (j > c) {
                        new_matrix[i-1][j-1] = matrix[i][j];
                    }
                }
            }
        }
        return new_matrix;
    }


    public static int findDet (int [][] matrix) {
        int deg = matrix.length;
        if (deg == 2) {
            return matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0];
        }
        else {
            int[] vals = new int[deg];
            for (int i = 0; i < deg; i++) {
                vals[i] = findDet(removeRC(matrix, 0, i));
            }
            for (int i = 0; i < deg; i++) {
                vals[i] *= matrix[0][i]*(int)Math.pow(-1, i);
            }

            return sum(vals);
        }

    }

    public static int sum (int[] vals) {
        int v = 0;
        for (int i: vals) {
            v += i;
        }
        return v;
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