package chessgame.game.event;

import chessgame.game.player.Player;
import chessgame.game.chess.ChessSide;

public class PlayerRequestRestartEvent extends GameEvent {
    private final ChessSide side;
    private final boolean first;

    public PlayerRequestRestartEvent(Player player, ChessSide side, boolean first) {
        super(player);
        this.side = side;
        this.first = first;
    }

    public ChessSide getSide() {
        return side;
    }

    public boolean isFirst() {
        return first;
    }

    @Override
    public String toString() {
        return "PlayerRequestRestartEvent{" +
                "side=" + side +
                ", first=" + first +
                '}';
    }
}
