package chessgame.game.chess;

import chessgame.game.chessboard.ChessBoard;
import chessgame.util.Position;

public class ChessElephant extends Chess {
    public ChessElephant(ChessSide side) {
        super(side, "elephant");
    }

    @Override
    public boolean canMove(ChessSide side, ChessBoard board, Position from, Position to) {
        if (!board.isPositionInOwnArea(to, side)) {
            return false;
        }

        int deltaX = Math.abs(from.getX() - to.getX());
        int deltaY = Math.abs(from.getY() - to.getY());
        if (deltaX != 2 || deltaY != 2) {
            return false;
        }

        int midX = (from.getX() + to.getX()) / 2;
        int midY = (from.getY() + to.getY()) / 2;
        if (board.getChessAt(new Position(midX, midY)) != null) {
            return false;
        }

        return true;
    }

    @Override
    public String getDisplayName() {
        return getSide() == ChessSide.CHU ? "象" : "相";
    }
}
