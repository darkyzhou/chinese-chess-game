package chessgame.game.chess;

import chessgame.game.chessboard.ChessBoard;
import chessgame.util.ImageResources;
import chessgame.util.Position;

import java.awt.*;

public abstract class Chess {
    private final ChessSide side;
    private final Image image;

    protected Chess(ChessSide side, String imageName) {
        this.side = side;
        this.image = ImageResources.getImage(String.format("chess/chess-%s-%s.png", imageName, side == ChessSide.CHU ? "chu" : "han"));
    }

    public ChessSide getSide() {
        return side;
    }

    public Image getImage() {
        return image;
    }

    public abstract boolean canMove(ChessSide side, ChessBoard board, Position from, Position to);

    public abstract String getDisplayName();

    @Override
    public String toString() {
        return "Chess{" +
                "side=" + side + ',' +
                "displayName=" + getDisplayName() +
                '}';
    }
}
