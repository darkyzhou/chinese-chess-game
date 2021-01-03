package chessgame.game.chess;

import chessgame.game.chessboard.ChessBoard;
import chessgame.util.Position;

import java.util.List;

public class ChessCannon extends Chess {
    public ChessCannon(ChessSide side) {
        super(side, "cannon");
    }

    @Override
    public boolean canMove(ChessSide side, ChessBoard board, Position from, Position to) {
        if (from.getX() != to.getX() && from.getY() != to.getY()) {
            return false;
        }

        List<Chess> chessList = board.getChessAlongSide(from, to, true);
        if (board.getChessAt(to) == null) {
            return chessList.isEmpty();
        } else {
            return chessList.size() == 2;
        }
    }

    @Override
    public String getDisplayName() {
        return getSide() == ChessSide.CHU ? "包" : "炮";
    }
}
