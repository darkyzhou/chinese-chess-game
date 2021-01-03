package chessgame.gui.index;

import chessgame.util.ImageResources;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private final Image backgroundImage;

    public BackgroundPanel() {
        backgroundImage = ImageResources.getImage("frame/startup-index.jpg");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }
}
