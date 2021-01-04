package chessgame.game.connection;

import chessgame.game.event.GameEvent;
import chessgame.util.eventbus.EventBus;
import com.google.gson.Gson;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageReaderMonitor implements Thread.UncaughtExceptionHandler {
    private final Gson gson = new Gson();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final MessageReader reader = new MessageReader(running);
    private final Scanner remoteInput;
    private final EventBus<GameEvent> eventBus;

    public MessageReaderMonitor(Scanner remoteInput, EventBus<GameEvent> eventBus) {
        this.remoteInput = remoteInput;
        this.eventBus = eventBus;
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println("MessageReaderMonitor: MessageReader fails with the following exception, restarting now");
        e.printStackTrace(System.err);
        startReader();
    }

    public void startReader() {
        System.out.println("MessageReaderMonitor: starting MessageReader");
        reader.setUncaughtExceptionHandler(this);
        reader.start();
    }

    public void stopReader() {
        System.out.println("MessageReaderMonitor: stopping MessageReader");
        running.set(false);
    }

    private class MessageReader extends Thread {
        private final AtomicBoolean running;

        private MessageReader(AtomicBoolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while (remoteInput.hasNext() && running.get()) {
                try {
                    String json = remoteInput.nextLine();
                    System.out.println("MessageReader: received event: " + json);
                    Message message = gson.fromJson(json, Message.class);
                    GameEvent event = (GameEvent) gson.fromJson(message.getData(), Class.forName(message.getType()));
                    eventBus.broadcastEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
