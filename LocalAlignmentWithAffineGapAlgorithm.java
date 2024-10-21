import java.util.List;
import java.util.Map;

public class LocalAlignmentWithAffineGapAlgorithm {
    private final String v;
    private final String w;

    private int[][] WEIGHTS_M;
    private int[][] WEIGHTS_L;
    private int[][] WEIGHTS_U;
    private int[][] BACKTRACKING;

    private static final int SIGMA = 11;
    private static final int EPSILON = 1;

    private int[][] scoreMatrix;

    private List<String> aminoAcids;

    private Map<Character, Integer> aaToIndexMap;

    public LocalAlignmentWithAffineGapAlgorithm(String v, String w) {
        this.v = v;
        this.w = w;

        loadScoreMatrix();
        initWeights();
        initBacktrack();
    }

    public Data findLocalAlignment() {
        int m = v.length();
        int n = w.length();

        int MAXI = -1;
        int MAXJ = -1;
        int MAX_SCORE = Integer.MIN_VALUE;

        for (int i=1; i<=m; i++) {
            for (int j=1; j<=n; j++) {
                WEIGHTS_L[i][j] = Math.max(
                    WEIGHTS_L[i-1][j] - EPSILON,
                    WEIGHTS_M[i-1][j] - SIGMA
                );
                int del = WEIGHTS_L[i][j];

                WEIGHTS_U[i][j] = Math.max(
                        WEIGHTS_U[i][j-1] - EPSILON,
                        WEIGHTS_M[i][j-1] - SIGMA
                );
                int in = WEIGHTS_U[i][j];

                int mm = WEIGHTS_M[i-1][j-1] + scoreMatrix[aaToIndexMap.get(v.charAt(i-1))][aaToIndexMap.get(w.charAt(j-1))];
                WEIGHTS_M[i][j] = Math.max(
                  0, //to account for local alignment.
                  Math.max(
                      mm,
                      Math.max(del, in)
                  )
                );

                if (WEIGHTS_M[i][j] == 0) {
                    BACKTRACKING[i-1][j-1] = 0;
                } else if (WEIGHTS_M[i][j] == in) {
                    BACKTRACKING[i-1][j-1] = 1;
                } else if (WEIGHTS_M[i][j] == del) {
                    BACKTRACKING[i-1][j-1] = 2;
                } else {
                    BACKTRACKING[i-1][j-1] = 3;
                }

                if (WEIGHTS_M[i][j] > MAX_SCORE) {
                    MAX_SCORE = WEIGHTS_M[i][j];
                    MAXI = i;
                    MAXJ = j;
                }

            }
        }

        System.out.println(MAXI + " " + MAXJ + " " + MAX_SCORE);

        int[] vi = {MAXI-1};
        int[] wi = {MAXJ-1};
        alignRecursive(MAXI-1, MAXJ-1, vi, wi);

        System.out.println(vi[0] + " " + wi[0]);
        System.out.println(v.substring(vi[0], MAXI));
        System.out.println(w.substring(wi[0], MAXJ));

        return new Data(-1, null, null);
    }

    private void initWeights() {
        int m = v.length();
        int n = w.length();

        WEIGHTS_L = new int[m+1][n+1];
        WEIGHTS_M = new int[m+1][n+1];
        WEIGHTS_U = new int[m+1][n+1];

        for (int i=0; i<=m; i++) {
            WEIGHTS_L[i][0] = -i*EPSILON;
            WEIGHTS_M[i][0] = 0;
            WEIGHTS_U[i][0] = 0;
        }

        for (int j=0; j<=n; j++) {
            WEIGHTS_L[0][j] = 0;
            WEIGHTS_M[0][j] = 0;
            WEIGHTS_U[0][j] = -j*EPSILON;
        }

//        printMatrix(WEIGHTS_L);
//        System.out.println("-----------------------------------------");
//        printMatrix(WEIGHTS_M);
//        System.out.println("-----------------------------------------");
//        printMatrix(WEIGHTS_U);

    }

    private void initBacktrack() {
        BACKTRACKING = new int[v.length()][w.length()];
    }

    private void loadScoreMatrix() {
        BLOSUM62Loader blosum62Loader = new BLOSUM62Loader();
        this.scoreMatrix = blosum62Loader.getMatrix();
        this.aminoAcids = blosum62Loader.getAminoAcids();
        this.aaToIndexMap = blosum62Loader.getAaToIndex();
    }

    private void alignRecursive(int i, int j, int[] vi, int[] wi) {
        if (i<=0 || j<=0 || BACKTRACKING[i][j] == 0) {
            return;
        }
        if (BACKTRACKING[i][j] == 3) {
            vi[0] -= 1;
            wi[0] -= 1;
            alignRecursive(i-1, j-1, vi, wi);
        } else if (BACKTRACKING[i][j] == 2) {
            vi[0] -= 1;
            alignRecursive(i-1, j, vi, wi);
        } else if (BACKTRACKING[i][j] == 1) {
            wi[0] -= 1;
            alignRecursive(i, j-1, vi, wi);
        }
    }

    private static void printMatrix(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;

        for (int[] ints : mat) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%4d", ints[j]);
            }
            System.out.println();
        }
    }
}
