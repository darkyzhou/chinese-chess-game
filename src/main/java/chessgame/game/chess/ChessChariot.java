package chessgame.game.chess;

import chessgame.game.chessboard.ChessBoard;
import chessgame.util.Position;

import java.util.List;

public class ChessChariot extends Chess {
    public ChessChariot(ChessSide side) {
        super(side, "chariot");
    }

    @Override
    public boolean canMove(ChessSide side, ChessBoard board, Position from, Position to) {
        if (from.getX() != to.getX() && from.getY() != to.getY()) {
            return false;
        }

        List<Chess> chessList = board.getChessAlongSide(from, to, true);
        if (board.getChessAt(to) == null && chessList.size() >= 1) {
            return false;
        }

        return true;
    }

    @Override
    public String getDisplayName() {
        return getSide() == ChessSide.CHU ? "車" : "俥";
    }
}
