package chessgame.game.event;

import chessgame.game.player.Player;

public class PlayerReplyRetractionEvent extends GameEvent {
    private final boolean accepted;

    public PlayerReplyRetractionEvent(Player player, boolean accepted) {
        super(player);
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public String toString() {
        return "PlayerReplyRetractionEvent{" +
                "accepted=" + accepted +
                '}';
    }
}
