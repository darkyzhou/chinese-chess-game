package chessgame.gui.index;

import chessgame.gui.BaseFrame;
import chessgame.gui.startup.StartupFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class IndexFrame extends BaseFrame {
    private final BackgroundPanel backgroundPanel;
    private final JButton startGameButton;

    public IndexFrame() {
        startGameButton = new JButton("开始游戏");
        startGameButton.setBounds(650, 20, 120, 50);
        startGameButton.addActionListener(this::onStartGameButtonClick);

        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);
        backgroundPanel.add(startGameButton);

        setContentPane(backgroundPanel);
    }

    private void onStartGameButtonClick(ActionEvent event) {
        new StartupFrame().showFrame();
        setVisible(false);
    }
}