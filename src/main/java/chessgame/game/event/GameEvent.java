package chessgame.game.event;

import chessgame.game.player.Player;

public abstract class GameEvent {
    private final Player player;

    public GameEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "GameEvent{" +
                "player=" + player +
                '}';
    }
}
