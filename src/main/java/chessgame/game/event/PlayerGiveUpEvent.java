package chessgame.game.event;

import chessgame.game.player.Player;

public class PlayerGiveUpEvent extends GameEvent {
    public PlayerGiveUpEvent(Player player) {
        super(player);
    }
}
