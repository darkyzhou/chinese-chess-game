package chessgame.game.chess;

import chessgame.game.chessboard.ChessBoard;
import chessgame.util.Position;

public class ChessSoldier extends Chess {
    public ChessSoldier(ChessSide side) {
        super(side, "soldier");
    }

    @Override
    public boolean canMove(ChessSide side, ChessBoard board, Position from, Position to) {
        if (!board.isPositionInOwnArea(from, side)) {
            return from.distanceToInSquare(to) == 1;
        }

        if (from.getX() != to.getX()) {
            return false;
        }

        if (side == ChessSide.CHU) {
            return to.getY() - from.getY() == 1;
        } else {
            return to.getY() - from.getY() == -1;
        }
    }

    @Override
    public String getDisplayName() {
        return getSide() == ChessSide.CHU ? "卒" : "兵";
    }
}
