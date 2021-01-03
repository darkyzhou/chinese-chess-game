package chessgame;

import chessgame.gui.index.IndexFrame;
import chessgame.util.ImageResources;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ImageResources.loadImages();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new IndexFrame().showFrame();
    }
}