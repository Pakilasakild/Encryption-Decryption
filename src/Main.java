import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static ArrayList<Integer> letterToNumber(String text) {
        ArrayList<Integer> nums = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            char c = text.toLowerCase().charAt(i);

            if (c >= 'a' && c <= 'z') {
                nums.add(c - 'a' + 1);
            } else if (c == ' ') {
                nums.add(27);
            } else if (c == '\n') {
                nums.add(28);
            } else if (c == '.') {
                nums.add(29);
            } else if (c == ',') {
                nums.add(30);
            } else if (c == '?') {
                nums.add(31);
            } else if (c == '!') {
                nums.add(32);
            } else if (c == ':') {
                nums.add(33);
            } else if (c == '\'') {
                nums.add(34);
            }
        }
        return nums;
    }

    public static String numberToLetter(int[][] Nums) {
        StringBuilder sb = new StringBuilder();

        int rows = Nums.length;
        int cols = Nums[0].length;

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {

                int number = Nums[row][col];

                if (number == 27) {
                    sb.append(" ");
                } else if (number == 28) {
                    sb.append("\n");
                } else if (number == 29) {
                    sb.append(".");
                } else if (number == 30) {
                    sb.append(",");
                } else if (number == 31) {
                    sb.append("?");
                } else if (number == 32) {
                    sb.append("!");
                } else if (number == 33) {
                    sb.append(":");
                } else if (number == 34) {
                    sb.append("'");
                } else if (number >= 1 && number <= 26) {
                    sb.append((char) (number - 1 + 'a'));
                }
            }
        }

        return sb.toString().stripTrailing();
    }

    public static int[][] encryption(String text, int[][] matrix) {
        ArrayList<Integer> Nums = letterToNumber(text);
        int[][] MatrixNums = MatrixMath.listToMatrix(Nums, matrix);
        return MatrixMath.multiply(matrix, MatrixNums);
    }

    public static String decryption(int[][] text, int[][] matrix) {
        return numberToLetter(MatrixMath.multiply(MatrixMath.invert(matrix), text));
    }

    public static void printMatrixHex(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.print("[ ");
            for (int val : row) {
                System.out.printf("%4X ", val);
            }
            System.out.println("]");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Input your desired operation: \n" +
                "1. Encrypt\n" +
                "2. Decrypt");
        int op = sc.nextInt();
        switch (op) {
            case 1:
                printMatrixHex(encryptCase());
                break;
            case 2:
                System.out.println(decryptCase());
                break;
            default:
                System.out.println("Incorrect operation");
                break;
        }
    }

    private static String decryptCase() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Input size of matrix to decrypt with:");
        int size = sc.nextInt();
        int[][] matrix = new int[size][size];

        System.out.println("Enter the key matrix in decimal (paste the matrix):");
        ArrayList<Integer> decVals = new ArrayList<>();

        while (decVals.size() < size * size && sc.hasNext()) {
            String token = sc.next();
            token = token.replace("[", "").replace("]", "").trim();
            if (!token.isEmpty()) {
                decVals.add(Integer.parseInt(token));
            }
        }

        int decIndex = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = decVals.get(decIndex++);
            }
        }

        System.out.println("Enter the encrypted text in hex (paste the matrix, type 'END' and hit Enter to finish):");
        ArrayList<Integer> hexVals = new ArrayList<>();

        while (sc.hasNext()) {
            String token = sc.next();
            if (token.equalsIgnoreCase("END")) {
                break;
            }

            token = token.replace("[", "").replace("]", "").trim();
            if (!token.isEmpty()) {
                hexVals.add(Integer.parseInt(token, 16));
            }
        }

        int cols = hexVals.size() / size;
        int[][] encryptedMatrix = new int[size][cols];
        int index = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < cols; j++) {
                encryptedMatrix[i][j] = hexVals.get(index++);
            }
        }

        System.out.println("\nDecrypted Text: ");
        return decryption(encryptedMatrix, matrix);
    }

    private static int[][] encryptCase() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter text to be encrypted (type 'END' on a new line to finish):");
        StringBuilder sb = new StringBuilder();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            if (sb.length() == 0 && line.trim().isEmpty()) {
                continue;
            }
            sb.append(line).append("\n");
        }

        String text = sb.toString();
        if (text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }

        int size = (int) Math.ceil(Math.sqrt(text.length()));
        if (size < 2) {
            size = 2;
        }

        int[][] matrix = MatrixMath.generateRandomMatrix(size);
        int[][] encrypted = encryption(text, matrix);

        try (PrintWriter out = new PrintWriter(new FileWriter("encryption.txt"))) {
            out.println("Key Matrix Size: " + size);
            out.println("Key Matrix (Decimal):");
            for (int i = 0; i < size; i++) {
                out.print("[ ");
                for (int j = 0; j < size; j++) {
                    out.print(matrix[i][j] + " ");
                }
                out.println("]");
            }

            out.println("\nEncrypted Text (Hex):");
            for (int i = 0; i < encrypted.length; i++) {
                out.print("[ ");
                for (int j = 0; j < encrypted[0].length; j++) {
                    out.printf("%4X ", encrypted[i][j]);
                }
                out.println("]");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("The matrix which was used to encrypt (saved to encryption.txt):");
        MatrixMath.printMatrix(matrix);

        System.out.println("Your encrypted text in matrix form (Hex) (saved to encryption.txt):");
        return encrypted;
    }
}