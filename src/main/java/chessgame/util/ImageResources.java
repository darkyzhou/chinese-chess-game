package chessgame.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ImageResources {
    private static final Map<String, Image> icons = new HashMap<>();

    public static final String[] IMAGE_NAMES = {
            "frame/startup-index.jpg",
            "frame/log-panel-background.jpg",
            "chess/chess-board.jpg",
            "chess/chess-advisor-chu.png",
            "chess/chess-advisor-han.png",
            "chess/chess-cannon-chu.png",
            "chess/chess-cannon-han.png",
            "chess/chess-chariot-chu.png",
            "chess/chess-chariot-han.png",
            "chess/chess-elephant-chu.png",
            "chess/chess-elephant-han.png",
            "chess/chess-general-chu.png",
            "chess/chess-general-han.png",
            "chess/chess-horse-chu.png",
            "chess/chess-horse-han.png",
            "chess/chess-soldier-chu.png",
            "chess/chess-soldier-han.png",
            "game/game-failure.png",
            "game/game-victory.png",
            "game/shade.png",
            "figure/cao-cao.png",
            "figure/da-qiao.png",
            "figure/gan-ning.png",
            "figure/guan-yu.png",
            "figure/guo-jia.png",
            "figure/liu-bei.png",
            "figure/lv-meng.png",
            "figure/ma-chao.png",
            "figure/sun-quan.png",
            "figure/zhang-fei.png",
            "figure/zhang-liao.png",
            "figure/zhao-yun.png",
            "figure/zhou-yu.png",
            "figure/zhu-ge-liang.png"
    };

    public static void loadImages() {
        for (String name : IMAGE_NAMES) {
            try {
                icons.put(name, ImageIO.read(ImageResources.class.getResourceAsStream("/images/" + name)));
            } catch (Exception e) {
                throw new RuntimeException("ImageResources: error while loading image for " + name, e);
            }
        }
    }

    public static Image getImage(String name) {
        if (!icons.containsKey(name)) {
            throw new IllegalArgumentException("image not found for " + name);
        }
        return icons.get(name);
    }
}
