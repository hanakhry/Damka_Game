package Utils;

public final class Constants {
    /** The number of pixels of padding between this component's border and the
     * actual Hamka board that is drawn. */
    public static final int trueEasy=100;
    public static final int trueMedium=200;
    public static final int trueHard=500;

    public static final int falseEasy=-250;
    public static final int falseMedium=-100;
    public static final int falseHard=-50;

    public static final int PADDING = 16;
    public static final int pointsAddedEasyQ=100;
    public static final int pointsAddedMediumQ=200;
    public static final int pointsAddedHardQ=500;
    public static final int pointsDecreacedEasyQ=250;
    public static final int pointsDecreacedMediumQ=100;
    public static final int pointsDecreacedHardQ=50;
    /** An ID indicating a point was not on the Hamka board. */
    public static final int INVALID = -1;

    /** The ID of an empty Hamka board tile. */
    public static final int EMPTY = 0;

    /** The ID of a black soldier in the Hamka board. */
    public static final int BLACK_SOLDIER = 4 * 1 + 2 * 1 + 1 * 0;

    /** The ID of a white soldier in the Hamka board. */
    public static final int WHITE_SOLDIER = 4 * 1 + 2 * 0 + 1 * 0;

    /** The ID of a black soldier that is also a queen. */
    public static final int BLACK_QUEEN = 4 * 1 + 2 * 1 + 1 * 1;

    /** The ID of a white soldier that is also a queen. */
    public static final int WHITE_QUEEN = 4 * 1 + 2 * 0 + 1 * 1;

}
