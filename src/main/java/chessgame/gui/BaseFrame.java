package chessgame.gui;

import javax.swing.*;
import java.awt.*;

public abstract class BaseFrame extends JFrame {
    protected BaseFrame() {
        getRootPane().setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("象棋游戏");
    }

    public void showFrame() {
        EventQueue.invokeLater(() -> {
            pack();
            setVisible(true);
        });
    }
}
