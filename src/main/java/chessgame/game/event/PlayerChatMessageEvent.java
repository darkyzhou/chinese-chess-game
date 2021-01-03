package chessgame.game.event;

import chessgame.game.player.Player;

public class PlayerChatMessageEvent extends GameEvent {
    private final String message;

    public PlayerChatMessageEvent(Player player, String message) {
        super(player);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "PlayerChatMessageEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
