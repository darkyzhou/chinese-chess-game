package chessgame.game.guievent;

public final class GameLog {
    private final String content;

    public GameLog(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "GameLog{" +
                "content='" + content + '\'' +
                '}';
    }
}
