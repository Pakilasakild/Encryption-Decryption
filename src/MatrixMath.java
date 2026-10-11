import java.util.ArrayList;
import java.util.Random;

public class MatrixMath {

    public static int[][] generateRandomMatrix(int n) {
        int[][] matrix = new int[n][n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            int rowSum = 0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    matrix[i][j] = rand.nextInt(10);
                    rowSum += matrix[i][j];
                }
            }
            matrix[i][i] = rowSum + 1;
        }
        return matrix;
    }

    public static double[][] invert(int[][] matrix) {
        int n = matrix.length;
        double[][] aug = new double[n][2 * n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aug[i][j] = matrix[i][j];
            }
            aug[i][i + n] = 1.0;
        }

        for (int i = 0; i < n; i++) {
            int pivotRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(aug[j][i]) > Math.abs(aug[pivotRow][i])) {
                    pivotRow = j;
                }
            }

            if (pivotRow != i) {
                double[] temp = aug[i];
                aug[i] = aug[pivotRow];
                aug[pivotRow] = temp;
            }

            if (Math.abs(aug[i][i]) < 1e-10) {
                throw new ArithmeticException("Matrix is singular");
            }

            double pivotVal = aug[i][i];
            for (int j = 0; j < 2 * n; j++) {
                aug[i][j] /= pivotVal;
            }

            for (int j = 0; j < n; j++) {
                if (j != i) {
                    double factor = aug[j][i];
                    for (int k = 0; k < 2 * n; k++) {
                        aug[j][k] -= factor * aug[i][k];
                    }
                }
            }
        }

        double[][] inverse = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(aug[i], n, inverse[i], 0, n);
        }

        return inverse;
    }

    public static int[][] multiply(int[][] a, int[][] b) {
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        int[][] result = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    public static double[][] multiply(double[][] a, double[][] b) {
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    public static double[][] multiply(int[][] a, double[][] b) {
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    public static int[][] multiply(double[][] a, int[][] b) {
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        int[][] result = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                double sum = 0;
                for (int k = 0; k < colsA; k++) {
                    sum += a[i][k] * b[k][j];
                }
                result[i][j] = (int) Math.round(sum);
            }
        }
        return result;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.print("[ ");
            for (int val : row) {
                System.out.printf("%4d ", val);
            }
            System.out.println("]");
        }
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.print("[ ");
            for (double val : row) {
                System.out.printf("%8.3f ", val);
            }
            System.out.println("]");
        }
    }

    public static int[][] listToMatrix(ArrayList<Integer> textList, int[][] keyMatrix) {
        int n = keyMatrix.length;

        int cols = (textList.size() + n - 1) / n;

        int[][] textMatrix = new int[n][cols];

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < n; row++) {
                int listIndex = col * n + row;

                if (listIndex < textList.size()) {
                    textMatrix[row][col] = textList.get(listIndex);
                } else {
                    textMatrix[row][col] = 27;
                }
            }
        }

        return textMatrix;
    }
}