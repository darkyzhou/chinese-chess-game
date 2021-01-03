package chessgame.game.event;

import chessgame.game.player.Player;
import chessgame.util.Position;

public final class PlayerMoveChessEvent extends GameEvent {
    private final Position from, to;

    public PlayerMoveChessEvent(Player player, Position from, Position to) {
        super(player);
        this.from = from;
        this.to = to;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "PlayerMoveChessEvent{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
