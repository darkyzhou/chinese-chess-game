package chessgame.game.guievent;

public class GameLogGuiEvent extends GuiEvent {
    private final GameLog gameLog;

    public GameLogGuiEvent(GameLog gameLog) {
        this.gameLog = gameLog;
    }

    public GameLog getGameLog() {
        return gameLog;
    }

    @Override
    public String toString() {
        return "GameLogGuiEvent{" +
                "gameLog=" + gameLog +
                '}';
    }
}
