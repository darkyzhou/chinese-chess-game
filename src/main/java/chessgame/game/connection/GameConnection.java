package chessgame.game.connection;

import chessgame.game.event.GameEvent;
import chessgame.util.eventbus.EventBus;
import chessgame.util.eventbus.EventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class GameConnection implements EventListener<GameEvent> {
    private final Gson gson = new Gson();
    private final EventBus<GameEvent> eventBus = new EventBus<>();
    private final MessageReaderMonitor messageReaderMonitor;

    private final Socket peerSocket;
    private final Scanner remoteInput;
    private final PrintStream remoteOutput;

    public GameConnection(Socket peerSocket, Scanner remoteInput, PrintStream remoteOutput) {
        this.peerSocket = peerSocket;
        this.remoteInput = remoteInput;
        this.remoteOutput = remoteOutput;

        messageReaderMonitor = new MessageReaderMonitor(remoteInput, eventBus);
        messageReaderMonitor.startReader();
    }

    public EventBus<GameEvent> getEventBus() {
        return eventBus;
    }

    public void addEventSource(EventBus<GameEvent> source) {
        source.registerEventListener(this);
    }

    public void destroy() throws IOException {
        messageReaderMonitor.stopReader();
        remoteInput.close();
        remoteOutput.close();
        peerSocket.close();
    }

    @Override
    public void onEvent(GameEvent event) {
        String json = gson.toJson(new Message(event.getClass().getName(), gson.toJson(event)));
        System.out.println("GameConnection: sending event: " + json);
        remoteOutput.println(json);
        remoteOutput.flush();
    }
}
