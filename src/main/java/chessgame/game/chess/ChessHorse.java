package chessgame.game.chess;

import chessgame.game.chessboard.ChessBoard;
import chessgame.util.Position;

public class ChessHorse extends Chess {
    public ChessHorse(ChessSide side) {
        super(side, "horse");
    }

    @Override
    public boolean canMove(ChessSide side, ChessBoard board, Position from, Position to) {
        if (from.distanceToInSquare(to) != 5) {
            return false;
        }

        int fromX = from.getX();
        int fromY = from.getY();
        int toX = to.getX();
        int toY = to.getY();

        int coX, coY;
        if (Math.abs(fromX - toX) == 2) {
            coX = toX - fromX > 0 ? 1 : -1;
            if (board.getChessAt(new Position(fromX + coX, fromY)) != null) {
                return false;
            }
            coY = toY - fromY;
            if (board.getChessAt(new Position(fromX + coX, fromY + coY)) != null) {
                return false;
            }
        } else {
            coY = toY - fromY > 0 ? 1 : -1;
            if (board.getChessAt(new Position(fromX, fromY + coY)) != null) {
                return false;
            }
            coX = toX - fromX;
            if (board.getChessAt(new Position(fromX + coX, fromY + coY)) != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getDisplayName() {
        return getSide() == ChessSide.CHU ? "馬" : "傌";
    }
}
