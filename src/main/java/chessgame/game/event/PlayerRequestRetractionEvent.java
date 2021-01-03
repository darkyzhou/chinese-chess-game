package chessgame.game.event;

import chessgame.game.player.Player;

public class PlayerRequestRetractionEvent extends GameEvent {
    public PlayerRequestRetractionEvent(Player player) {
        super(player);
    }
}
