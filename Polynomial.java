import java.util.*;
import java.io.*;


public class Polynomial {

    static int [][] matrix;
    static int [] anscolumn;
    static float [] coefs;           // Set of coefficients of the resulting polynomial
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

        // Reads from the data file and fills vals with tuples of the supplied values
        for (int i = 0; i < degree; i++) {
            line = inFile.nextLine().split(" ");
            vals.add(new Tuple(Integer.parseInt(line[0]), Integer.parseInt(line[1])));
        }

        anscolumn = new int[degree];
        matrix = fillMatrix(new int[degree][degree]);
        cramers(matrix, anscolumn);
        coefs = cramers(matrix, anscolumn);             // Coefs contains the coefficients of the resulting polynomial of degree (degree-1)
        System.out.println(Arrays.toString(coefs));
        createHTML("$$" + convertToPolynomial(coefs) + "$$");
        //System.out.println(convertToPolynomial(coefs));
        //createLatexDoc(writeLatex(coefs));              // Experimental – generates latex document

    }

    public static String writeLatex (float[] coefs) {
        int degree = coefs.length;
        String document = "\\documentclass[20pt]{article}\n" +
        "\\usepackage{extsizes}" +
        "\\thispagestyle{empty}" +
        "\\begin{document}\n\\begin{Large}\n$$";
        String eq = convertToPolynomial(coefs);
        document += eq + "$$\n\\end{Large}\n\\end{document}";
        return document;
    }

    public static void createHTML (String s) {
        String document = "<!DOCTYPE html PUBLIC" +
                "\n<html>\n<head>\n<title>findpolynomial</title>" +
                "<script type='text/x-mathjax-config'>" +
                "MathJax.Hub.Config({tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}});" +
                "\n</script>\n<script type='text/javascript'\nsrc=" +
                "'http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML'>" +
                "\n</script>\n</head>\n<body>\n<h2>FindPolynomial</h2>";

        document += s + "\n</body>\n</html>";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("result.html", "UTF-8");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        writer.print(document);
        writer.close();

        Runtime p = Runtime.getRuntime();
        try {
            p.exec("open result.html");
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Creates and opens a png with latex of the resulting polynomial
    public static void createLatexDoc (String s) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("result.tex", "UTF-8");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        writer.print(s);
        writer.close();

        Runtime p = Runtime.getRuntime();
        try {
            p.exec("pdflatex result.tex");
            //p.exec("convert result.pdf result.png");
            //p.exec("convert -density 500 result.png -resize 500 -quality 300 result.png");
            p.exec("convert -density 300 -trim result.pdf -quality 100 result.png");
            p.exec("open result.png");
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Takes the set of coefficients and converts it to a standard looking polynomial
    public static String convertToPolynomial(float[] coefs) {
        String s = "";
        int degree = coefs.length;
        for (int i = 0; i < degree; i ++) {
            if (coefs[i] > 0 && s != "") {
                s += "  +  ";
            }
            if (coefs[i] < 0) {
                coefs[i]*= -1;
                s += "  -  ";
            }
            if (coefs[i] != 0) {
                if (coefs[i] == (int) coefs[i]) {
                    if (coefs[i] != 1 || (coefs[i] == 1 && degree-i-1 == 0)) {
                        s += Integer.toString((int) coefs[i]);
                    }
                }
                else {
                    s += Float.toString(coefs[i]).substring(0, 4);
                }
            }
            if (coefs[i] != 0) {
                if (degree-i-1 == 1) {
                    s += "x";
                }
                else if (degree-i-1 > 1) {
                    s += "x^" + Integer.toString(degree-i-1);
                }
            }

        }
        return "f(x) = " + s;
    }

    public static float[] cramers(int[][] matrix, int[] anscolumn) {
        /*
        int[][] matrix2 = matrix;
        int degree = matrix.length;
        float[] coefs = new float[degree];
        int det_coef = findDet(matrix);
        for (int i = 0; i < degree; i++) {
            coefs[i] = (float) findDet(replaceC(matrix2, anscolumn, i))/det_coef;
        }

        return coefs;
        */
        int[][] matrix2 = cloneArray(matrix);
        int degree = matrix.length;
        float[] coefs = new float[degree];
        int coef_det = findDet(matrix2);
        for (int i = 0; i < degree; i ++) {
            coefs[i] = (float) findDet(replaceC(matrix2, anscolumn, i))/coef_det;
        }
        return coefs;
    }


    // Populates a matrix of the coefficients to be solved
    public static int[][] fillMatrix(int[][] matrix) {
        int degree = matrix.length;
        for (int i = 0; i < degree; i ++) {
            int x = vals.get(i).getX();
            for (int j = 0; j < degree; j ++) {
                matrix[i][j] = (int) Math.pow(x, degree - (j+1));
            }
            anscolumn[i] = vals.get(i).getY();
        }

        return matrix;
    }

    // Replaces column c in a given matrix with a given column
    public static int[][] replaceC (int[][] matrix, int[] newc, int c) {
        int degree = matrix.length;
        int[][] new_matrix = cloneArray(matrix);
        for (int i = 0; i < degree; i ++) {
            new_matrix[i][c] = newc[i];
        }
        return new_matrix;
    }

    // Removes row r and column c from a given matrix
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

    // Finds determinant of a given matrix
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

    // Returns a duplicated version of a given array
    public static int[][] cloneArray(int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

}

// Pretty useless – for storing the input data
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