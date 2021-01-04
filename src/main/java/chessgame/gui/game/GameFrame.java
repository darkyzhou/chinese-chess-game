package chessgame.gui.game;

import chessgame.game.Game;
import chessgame.game.event.PlayerReplyRetractionEvent;
import chessgame.game.event.PlayerRequestRetractionEvent;
import chessgame.game.guievent.ShutdownGuiEvent;
import chessgame.gui.BaseFrame;
import chessgame.gui.startup.StartupFrame;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends BaseFrame {
    private final Game game;
    private final ChessBoardPanel chessBoardPanel;
    private final GameInfoPanel gameInfoPanel;

    public GameFrame(Game game) {
        this.game = game;

        chessBoardPanel = new ChessBoardPanel(game);
        chessBoardPanel.setBounds(0, 0, 550, 600);

        gameInfoPanel = new GameInfoPanel(game);
        gameInfoPanel.setBounds(550, 0, 250, 600);

        setLayout(null);
        add(chessBoardPanel);
        add(gameInfoPanel);

        game.getGameConnection().getEventBus().registerEventListener(PlayerRequestRetractionEvent.class, e -> EventQueue.invokeLater(() -> onRemotePlayerRequestRetraction(e)));
        game.getGuiEventBus().registerEventListener(ShutdownGuiEvent.class, this::onShutdownGuiEvent);
    }

    private void onRemotePlayerRequestRetraction(PlayerRequestRetractionEvent event) {
        int option = JOptionPane.showConfirmDialog(this, "对方请求悔棋，你接受吗？", "提示", JOptionPane.YES_NO_OPTION);
        game.getGameEventBus().broadcastEvent(new PlayerReplyRetractionEvent(game.getLocalPlayer(), option == JOptionPane.YES_OPTION));
    }

    private void onShutdownGuiEvent(ShutdownGuiEvent event) {
        System.out.println("GameFrame: shutting down current frame and showing startup frame");
        EventQueue.invokeLater(() -> setVisible(false));
        new StartupFrame().showFrame();
    }
}
