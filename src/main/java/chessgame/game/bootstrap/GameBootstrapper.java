package chessgame.game.bootstrap;

import chessgame.game.connection.GameConnection;
import chessgame.game.Game;
import chessgame.game.player.Player;
import chessgame.game.chess.ChessSide;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public final class GameBootstrapper {
    private static final Gson GSON = new Gson();

    public static Game bootstrapAsHost(String localPlayerName, String localPlayerFigure, Socket peer) throws IOException {
        PrintStream output = new PrintStream(peer.getOutputStream());
        Scanner input = new Scanner(peer.getInputStream());

        boolean hostFirst = Math.random() < 0.5d;
        ChessSide side = ChessSide.randomSide();
        Player localPlayer = new Player(localPlayerName, localPlayerFigure, side);

        BootstrapMessage bootstrapMessage = new BootstrapMessage(side, localPlayer, hostFirst);
        output.println(GSON.toJson(bootstrapMessage));

        String responseJson = input.nextLine();
        BootstrapResponseMessage responseMessage = GSON.fromJson(responseJson, BootstrapResponseMessage.class);
        Player remotePlayer = responseMessage.getPlayer();

        return new Game(new GameConnection(peer, input, output), localPlayer, remotePlayer, hostFirst);
    }

    public static Game bootstrapAsGuest(String localPlayerName, String localPlayerFigure, Socket peer) throws IOException {
        PrintStream output = new PrintStream(peer.getOutputStream());
        Scanner input = new Scanner(peer.getInputStream());

        String bootstrapMessageJson = input.nextLine();
        BootstrapMessage bootstrapMessage = GSON.fromJson(bootstrapMessageJson, BootstrapMessage.class);
        Player remotePlayer = bootstrapMessage.getPlayer();
        boolean hostFirst = bootstrapMessage.isHostFirst();
        ChessSide remoteSide = bootstrapMessage.getChessSide();
        Player localPlayer = new Player(localPlayerName, localPlayerFigure, remoteSide.getReverse());

        BootstrapResponseMessage responseMessage = new BootstrapResponseMessage(localPlayer);
        output.println(GSON.toJson(responseMessage));

        return new Game(new GameConnection(peer, input, output), localPlayer, remotePlayer, !hostFirst);
    }
}
