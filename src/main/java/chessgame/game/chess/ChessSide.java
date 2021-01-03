package chessgame.game.chess;

public enum ChessSide {
    CHU, HAN;

    public ChessSide getReverse() {
        return this == HAN ? CHU : HAN;
    }

    public static ChessSide randomSide() {
        return Math.random() < 0.5d ? ChessSide.CHU : ChessSide.HAN;
    }
}
