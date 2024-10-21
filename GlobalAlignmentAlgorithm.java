import java.util.List;
import java.util.Map;

public class GlobalAlignmentAlgorithm {

    private final String v;
    private final String w;
    private int[][] WEIGHTS;
    private int[][] BACKTRACKING;
    private static final int SIGMA = 5;

    private int[][] scoreMatrix;

    private List<String> aminoAcids;

    private Map<Character, Integer> aaToIndexMap;

    public GlobalAlignmentAlgorithm(String v, String w) {
        this.v = v;
        this.w = w;

        loadScoreMatrix();
        initWeights();
        initBacktrack();
    }

    public Data findGlobalAlignment() {

        for (int i=1; i<WEIGHTS.length; i++) {
            for (int j=1; j<WEIGHTS[0].length; j++) {
                int in = WEIGHTS[i][j-1] - SIGMA; //0
                int del = WEIGHTS[i-1][j] - SIGMA; //1
                int mm = WEIGHTS[i-1][j-1] +
                        scoreMatrix[aaToIndexMap.get(v.charAt(i-1))][aaToIndexMap.get(w.charAt(j-1))]; //2

                WEIGHTS[i][j] = Math.max(in, Math.max(del, mm));
                if (WEIGHTS[i][j] == in) BACKTRACKING[i-1][j-1] = 0;
                else if (WEIGHTS[i][j] == del) BACKTRACKING[i-1][j-1] = 1;
                else BACKTRACKING[i-1][j-1] = 2;
            }
        }


//        printMatrix(WEIGHTS);
//        System.out.println("---------------------------------");
//        printMatrix(BACKTRACKING);
        System.out.println(WEIGHTS[v.length()][w.length()]);

        StringBuilder sbv = new StringBuilder(v);
        StringBuilder sbw = new StringBuilder(w);

//        alignIterative(sbv, sbw);
        alignRecursive(sbv, sbw,v.length()-1, w.length()-1);

        System.out.println(sbv);
        System.out.println(sbw);

        return new Data(WEIGHTS[v.length()][w.length()] , sbv.toString(), sbw.toString());
    }

    private void initWeights() {
        WEIGHTS = new int[v.length()+1][w.length()+1];
        for (int i=0; i<=v.length(); i++) {
            WEIGHTS[i][0] = -i*SIGMA;
        }

        for (int j=0; j<=w.length(); j++) {
            WEIGHTS[0][j] = -j*SIGMA;
        }

//        printMatrix(WEIGHTS);
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

    private void alignIterative(StringBuilder sbv, StringBuilder sbw) {
        int i = BACKTRACKING.length-1;
        int j = BACKTRACKING[0].length-1;
        while (i != 0 && j != 0) {
            if (BACKTRACKING[i][j] == 2) {
                i -= 1;
                j -= 1;
            } else if (BACKTRACKING[i][j] == 1) {
                sbw.insert(j+1, '-');
                i -= 1;
            } else {
                sbv.insert(i+1, "-");
                j -= 1;
            }
        }

        fillDash(sbv, sbw, i, j);

    }

    private void alignRecursive(StringBuilder sbv, StringBuilder sbw, int i, int j) {
        if (i==0 || j==0) {
            fillDash(sbv, sbw, i, j);
            return;
        }

        if (BACKTRACKING[i][j] == 2) {
            alignRecursive(sbv, sbw, i-1, j-1);
        } else if (BACKTRACKING[i][j] == 1) {
            sbw.insert(j+1, '-');
            alignRecursive(sbv, sbw, i-1, j);
        } else {
            sbv.insert(i+1, '-');
            alignRecursive(sbv, sbw, i, j-1);
        }
    }

    private void fillDash(StringBuilder sbv, StringBuilder sbw, int i, int j) {
        //TODO;
        while (i != 0) {
            sbw.insert(0, '-');
            i -= 1;
        }

        while (j != 0) {
            sbv.insert(0, '-');
            j -= 1;
        }
    }

    private void printMatrix(int[][] mat) {
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
