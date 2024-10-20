import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BLOSUM62Loader {

    private final Map<Character, Integer> aaToIndex = new LinkedHashMap<>();
    private int[][] matrix = null;

    private final List<String> aminoAcids = new ArrayList<>();

    public BLOSUM62Loader() {
        load();
    }


    public void load() {
        String filename = "data/BLOSUM62.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;


            /**
             * *******************************************************
             * Header
             */
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;


                if (Character.isLetter(line.replaceAll("\\s+", "").charAt(0))) {
                    String[] tokens = line.trim().split("\\s+");
                    for (int i = 0; i < tokens.length; i++) {
                        aminoAcids.add(tokens[i]);
                        aaToIndex.put(tokens[i].charAt(0), i);
                    }

                    // Initialize the matrix
                    int m = aminoAcids.size();
                    int n = aminoAcids.size();
                    matrix = new int[m][n];
                    break;  // Header line processed - break!
                }
            }

            /**
             * ****************************************************************
             * Rest
             */
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                // Split the line into tokens
                String[] tokens = line.split("\\s+");
                String rowAA = tokens[0];
                int rowIndex = aaToIndex.get(rowAA.charAt(0));

                // Parse the scores and fill the matrix
                for (int i = 1; i < tokens.length; i++) {
                    int value = Integer.parseInt(tokens[i]);
                    matrix[rowIndex][i - 1] = value;
                }
            }

            br.close();

            /**
             * Comment out if needed - debugging.
             */

            /*

            System.out.println("Amino Acids: " + aminoAcids);
            System.out.println("BLOSUM62 Matrix:");
            System.out.print("   ");
            for (String aa : aminoAcids) {
                System.out.printf("%4s", aa);
            }
            System.out.println();

            for (int i = 0; i < matrix.length; i++) {
                System.out.printf("%2s ", aminoAcids.get(i));
                for (int j = 0; j < matrix[i].length; j++) {
                    System.out.printf("%4d", matrix[i][j]);
                }
                System.out.println();
            }


             */


        } catch (IOException e) {
            e.printStackTrace();
        }

//        return matrix;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public Map<Character, Integer> getAaToIndex() {
        return aaToIndex;
    }

    public List<String> getAminoAcids() {
        return aminoAcids;
    }
}