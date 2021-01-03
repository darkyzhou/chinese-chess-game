package chessgame.game.chess;

import chessgame.game.chessboard.ChessBoard;
import chessgame.util.Position;

public class ChessAdvisor extends Chess {
    public ChessAdvisor(ChessSide side) {
        super(side, "advisor");
    }

    @Override
    public boolean canMove(ChessSide side, ChessBoard board, Position from, Position to) {
        if (!board.isPositionInPalace(to)) {
            return false;
        }

        if (!board.isValidMoveInPalace(from, to, side)) {
            return false;
        }

        return true;
    }

    @Override
    public String getDisplayName() {
        return getSide() == ChessSide.CHU ? "士" : "仕";
    }
}
