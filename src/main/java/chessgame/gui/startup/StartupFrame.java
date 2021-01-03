package chessgame.gui.startup;

import chessgame.game.connection.ConnectionBootstrapper;
import chessgame.game.Game;
import chessgame.game.bootstrap.GameBootstrapper;
import chessgame.gui.BaseFrame;
import chessgame.gui.game.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

public class StartupFrame extends BaseFrame {
    private final int randomlyPickedHostPort;

    private final PlayerSettingsPanel playerSettingsPanel;
    private final ConnectionSettingsPanel connectionSettingsPanel;

    public StartupFrame() {
        randomlyPickedHostPort = ConnectionBootstrapper.getRandomPort();
        playerSettingsPanel = new PlayerSettingsPanel();
        playerSettingsPanel.onNextStepButtonClick(e -> this.showConnectionSettingsPanel());
        connectionSettingsPanel = new ConnectionSettingsPanel(randomlyPickedHostPort);

        connectionSettingsPanel.onConnectAsGuestButtonClick(tuple -> {
            InetAddress address = tuple.getFirst();
            int port = tuple.getSecond();
            if (address.getHostName().equals(ConnectionBootstrapper.getDefaultAddress().getHostAddress()) && port == randomlyPickedHostPort) {
                JOptionPane.showMessageDialog(this, "不可以连接自己进行游戏", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ConnectionBootstrapper.runAsGuest(address, port, socket -> {
                Game game;
                try {
                    game = GameBootstrapper.bootstrapAsGuest(playerSettingsPanel.getPlayerName(), playerSettingsPanel.getPlayerFigureFileName(), socket);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                System.out.println("StartupFrame: game created: " + game);
                launchGameFrame(game);
            });
        });

        setContentPane(playerSettingsPanel);
        pack();
    }

    private void showConnectionSettingsPanel() {
        setContentPane(connectionSettingsPanel);
        pack();
        ConnectionBootstrapper.runAsHost(randomlyPickedHostPort, socket -> {
            Game game;
            try {
                game = GameBootstrapper.bootstrapAsHost(playerSettingsPanel.getPlayerName(), playerSettingsPanel.getPlayerFigureFileName(), socket);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            System.out.println("StartupFrame: game created: " + game);
            launchGameFrame(game);
        });
    }

    private void launchGameFrame(Game game) {
        EventQueue.invokeLater(() -> {
            new GameFrame(game).showFrame();
            setVisible(false);
        });
    }
}
