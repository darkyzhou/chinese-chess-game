package chessgame.game.bootstrap;

import chessgame.game.player.Player;

public class BootstrapResponseMessage {
    private final Player player;

    public BootstrapResponseMessage(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "BootstrapResponseMessage{" +
                "player=" + player +
                '}';
    }
}
