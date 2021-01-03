package chessgame.game.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public final class ConnectionBootstrapper {
    public static InetAddress getDefaultAddress() {
        try {
            return InetAddress.getByName(null);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getRandomPort() {
        return 30000 + (int) Math.floor(Math.random() * 10000);
    }

    public static void runAsHost(int port, Consumer<Socket> onConnect) {
        new Thread(() -> {
            try {
                System.out.println("ConnectionBootstrapper: listening on " + port);
                ServerSocket socket = new ServerSocket(port);
                Socket s = socket.accept();
                System.out.println(String.format("ConnectionBootstrapper: Got connection from %s:%d", s.getInetAddress().getHostAddress(), s.getPort()));
                onConnect.accept(s);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void runAsGuest(InetAddress remoteAddress, int port, Consumer<Socket> onConnect) {
        new Thread(() -> {
            try {
                System.out.println(String.format("ConnectionBootstrapper: connecting to %s:%s", remoteAddress.getHostAddress(), port));
                Socket s = new Socket(remoteAddress, port);
                onConnect.accept(s);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).start();
    }
}
