package chessgame.gui.game;

import chessgame.game.Game;
import chessgame.game.player.Player;
import chessgame.game.chess.Chess;
import chessgame.game.chess.ChessSide;
import chessgame.game.chessboard.ChessBoard;
import chessgame.util.ImageResources;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class PlayerInfoPanel extends JPanel {
    private final Game game;
    private final Player player;

    private final Font font;
    private final String playerNameText;
    private final Image playerFigureImage;

    public PlayerInfoPanel(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.playerFigureImage = ImageResources.getImage("figure/" + player.getFigureName());
        this.playerNameText = player.getName() + (player.equals(game.getLocalPlayer()) ? "（你）" : "");

        Font defaultFont = UIManager.getFont("Label.font");
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.SIZE, 18);
        this.font = defaultFont.deriveFont(defaultFont.getStyle() | Font.BOLD).deriveFont(attributes);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(playerFigureImage, 0, 0, getWidth(), getHeight(), this);

        if (player.equals(game.getOnTurnPlayer())) {
            Chess chess = player.getSide() == ChessSide.CHU ? ChessBoard.CHESS_GENERAL_CHU : ChessBoard.CHESS_GENERAL_HAN;
            g2d.drawImage(chess.getImage(), getWidth() - 60, 10, 50, 50, this);
        }

        g2d.setFont(font);
        if (game.getLocalPlayer().equals(player)) {
            g2d.setColor(Color.GRAY);
            g2d.drawString(playerNameText, 11, getHeight() - 9);
            g2d.setColor(Color.WHITE);
            g2d.drawString(playerNameText, 10, getHeight() - 10);
        } else {
            g2d.setColor(Color.GRAY);
            g2d.drawString(playerNameText, 11, 11 + 18);
            g2d.setColor(Color.WHITE);
            g2d.drawString(playerNameText, 10, 10 + 18);
        }

        g2d.dispose();
    }
}
