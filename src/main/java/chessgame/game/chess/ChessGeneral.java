package chessgame.game.chess;

import chessgame.game.chessboard.ChessBoard;
import chessgame.util.Position;

import java.util.List;

public class ChessGeneral extends Chess {
    public ChessGeneral(ChessSide side) {
        super(side, "general");
    }

    @Override
    public boolean canMove(ChessSide side, ChessBoard board, Position from, Position to) {
        if (from.getX() == to.getX()) {
            List<Chess> chessList = board.getChessAlongSide(from, to, true);
            Chess targetChess = board.getChessAt(to);
            if (chessList.size() == 1 && (targetChess == ChessBoard.CHESS_GENERAL_CHU || targetChess == ChessBoard.CHESS_GENERAL_HAN)) {
                return true;
            }
        }

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
        return getSide() == ChessSide.CHU ? "将" : "帅";
    }
}
