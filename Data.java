public class Data {
    private final String alignedV;
    private final String alignedW;
    private final int alignScore;

    public Data(int alignScore, String alignedV, String alignedW) {
        this.alignedV = alignedV;
        this.alignedW = alignedW;
        this.alignScore = alignScore;
    }

    public String getAlignedV() {
        return alignedV;
    }

    public String getAlignedW() {
        return alignedW;
    }

    public int getAlignScore() {
        return alignScore;
    }
}
