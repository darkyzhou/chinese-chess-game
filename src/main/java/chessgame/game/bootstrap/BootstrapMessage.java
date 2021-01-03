package chessgame.game.bootstrap;

import chessgame.game.player.Player;
import chessgame.game.chess.ChessSide;

public class BootstrapMessage {
    private final ChessSide chessSide;
    private final Player player;
    private final boolean hostFirst;

    public BootstrapMessage(ChessSide ch, Player remotePlayer, boolean hostFirst) {
        this.chessSide = ch;
        this.player = remotePlayer;
        this.hostFirst = hostFirst;
    }

    public Player getPlayer() {
        return player;
    }

    public ChessSide getChessSide() {
        return chessSide;
    }

    public boolean isHostFirst() {
        return hostFirst;
    }

    @Override
    public String toString() {
        return "BootstrapMessage{" +
                "chessSide=" + chessSide +
                ", player=" + player +
                ", hostFirst=" + hostFirst +
                '}';
    }
}
