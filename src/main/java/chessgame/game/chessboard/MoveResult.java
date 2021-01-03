package chessgame.game.chessboard;

import chessgame.game.chess.Chess;
import chessgame.game.chess.ChessSide;

public final class MoveResult {
    private final ChessSide winSide;
    private final Chess movedChess;
    private final Chess targetChess;

    public MoveResult(ChessSide winSide, Chess movedChess, Chess targetChess) {
        this.winSide = winSide;
        this.movedChess = movedChess;
        this.targetChess = targetChess;
    }

    public ChessSide getWinSide() {
        return winSide;
    }

    public Chess getMovedChess() {
        return movedChess;
    }

    public Chess getTargetChess() {
        return targetChess;
    }

    @Override
    public String toString() {
        return "MoveResult{" +
                "winSide=" + winSide +
                ", movedChess=" + movedChess +
                ", targetChess=" + targetChess +
                '}';
    }
}
