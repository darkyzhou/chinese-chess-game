package chessgame.game.chessboard;

import chessgame.game.chess.Chess;
import chessgame.util.Position;

public final class MoveChessAction {
    private final Position from, to;
    private final Chess movedChess, targetChess;

    public MoveChessAction(Position from, Position to, Chess movedChess, Chess targetChess) {
        this.from = from;
        this.to = to;
        this.movedChess = movedChess;
        this.targetChess = targetChess;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Chess getMovedChess() {
        return movedChess;
    }

    public Chess getTargetChess() {
        return targetChess;
    }

    @Override
    public String toString() {
        return "MoveChessAction{" +
                "from=" + from +
                ", to=" + to +
                ", movedChess=" + movedChess +
                ", targetChess=" + targetChess +
                '}';
    }
}
