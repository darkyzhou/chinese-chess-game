package chessgame.game.event;

import chessgame.game.player.Player;

public class PlayerExitEvent extends GameEvent {
    public PlayerExitEvent(Player player) {
        super(player);
    }
}
