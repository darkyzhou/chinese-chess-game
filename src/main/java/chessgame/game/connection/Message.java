package chessgame.game.connection;

public final class Message {
    private final String type, data;

    public Message(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
