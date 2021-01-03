package chessgame.game;

import chessgame.game.chess.Chess;
import chessgame.game.chess.ChessSide;
import chessgame.game.connection.GameConnection;
import chessgame.game.chessboard.ChessBoard;
import chessgame.game.event.*;
import chessgame.game.chessboard.MoveResult;
import chessgame.game.guievent.*;
import chessgame.game.player.Player;
import chessgame.util.eventbus.EventBus;

public class Game {
    private final EventBus<GuiEvent> guiEventBus;
    private final EventBus<GameEvent> gameEventBus;
    private final GameConnection gameConnection;

    private final ChessBoard chessBoard;
    private final Player localPlayer;
    private final Player remotePlayer;

    private GameState gameState;
    private Player winner;

    public Game(GameConnection gameConnection, Player localPlayer, Player remotePlayer, boolean localFirst) {
        this.gameConnection = gameConnection;
        this.localPlayer = localPlayer;
        this.remotePlayer = remotePlayer;

        guiEventBus = new EventBus<>();
        gameEventBus = new EventBus<>();
        chessBoard = new ChessBoard(localPlayer.getSide());
        gameState = localFirst ? GameState.ON_TURN : GameState.WAIT_FOR_TURN;

        gameEventBus.registerEventListener(PlayerMoveChessEvent.class, this::onPlayerMoveChess);
        gameEventBus.registerEventListener(PlayerRequestRetractionEvent.class, this::onPlayerRequestRetraction);
        gameEventBus.registerEventListener(PlayerReplyRetractionEvent.class, this::onPlayerReplyRetraction);
        gameEventBus.registerEventListener(PlayerGiveUpEvent.class, this::onPlayerGiveUp);
        gameEventBus.registerEventListener(PlayerExitEvent.class, this::onPlayerExit);
        gameEventBus.registerEventListener(PlayerChatMessageEvent.class, this::onPlayerSendChatMessage);

        gameConnection.addEventSource(gameEventBus);
        gameConnection.getEventBus().registerEventListener(PlayerMoveChessEvent.class, this::onPlayerMoveChess);
        gameConnection.getEventBus().registerEventListener(PlayerRequestRetractionEvent.class, this::onPlayerRequestRetraction);
        gameConnection.getEventBus().registerEventListener(PlayerReplyRetractionEvent.class, this::onPlayerReplyRetraction);
        gameConnection.getEventBus().registerEventListener(PlayerGiveUpEvent.class, this::onPlayerGiveUp);
        gameConnection.getEventBus().registerEventListener(PlayerExitEvent.class, this::onPlayerExit);
        gameConnection.getEventBus().registerEventListener(PlayerChatMessageEvent.class, this::onPlayerSendChatMessage);
    }

    public GameConnection getGameConnection() {
        return gameConnection;
    }

    public EventBus<GuiEvent> getGuiEventBus() {
        return guiEventBus;
    }

    public EventBus<GameEvent> getGameEventBus() {
        return gameEventBus;
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public Player getRemotePlayer() {
        return remotePlayer;
    }

    public Player getOnTurnPlayer() {
        switch (gameState) {
            case ON_TURN:
                return localPlayer;
            case WAIT_FOR_TURN:
                return remotePlayer;
            default:
                return null;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public Player getWinner() {
        return winner;
    }

    public void reset(boolean localFirst, ChessSide localSide) {
        winner = null;
        localPlayer.setSide(localSide);
        remotePlayer.setSide(localSide.getReverse());
        gameState = localFirst ? GameState.ON_TURN : GameState.WAIT_FOR_TURN;
        chessBoard.reset(localSide);
        guiEventBus.broadcastEvent(new ResetGuiEvent());
    }

    private void onPlayerMoveChess(PlayerMoveChessEvent event) {
        MoveResult moveResult = chessBoard.moveChess(event.getFrom(), event.getTo());

        String message = String.format("%s将棋子【%s】从 %s 移到了 %s",
                event.getPlayer().equals(localPlayer) ? "你" : "对方",
                moveResult.getMovedChess().getDisplayName(),
                event.getFrom(),
                event.getTo());
        Chess targetChess = moveResult.getTargetChess();
        if (targetChess != null) {
            message = String.format("%s 并吃掉了%s的【%s】",
                    message,
                    event.getPlayer().equals(localPlayer) ? "对方" : "你",
                    targetChess.getDisplayName()
            );
        }
        guiEventBus.broadcastEvent(new GameLogGuiEvent(new GameLog(message)));

        if (moveResult.getWinSide() == null) {
            gameState = event.getPlayer().equals(localPlayer) ? GameState.WAIT_FOR_TURN : GameState.ON_TURN;
        } else {
            gameState = GameState.ENDED;
            winner = moveResult.getWinSide() == localPlayer.getSide() ? localPlayer : remotePlayer;
        }

        guiEventBus.broadcastEvent(new UpdateGuiEvent());
    }

    private void onPlayerRequestRetraction(PlayerRequestRetractionEvent event) {
        guiEventBus.broadcastEvent(new GameLogGuiEvent(new GameLog(String.format("%s请求悔棋", event.getPlayer().equals(localPlayer) ? "你" : "对方"))));
        gameState = GameState.WAITING_RETRACTION_REQUEST;
    }

    private void onPlayerReplyRetraction(PlayerReplyRetractionEvent event) {
        guiEventBus.broadcastEvent(new GameLogGuiEvent(
                new GameLog(String.format("%s%s了悔棋请求", event.getPlayer().equals(localPlayer) ? "你" : "对方", event.isAccepted() ? "同意" : "拒绝"))
        ));

        boolean isRequestedByLocalPlayer = event.getPlayer().equals(remotePlayer);
        if (event.isAccepted()) {
            chessBoard.undoLatestTwoAction();
        }

        if (isRequestedByLocalPlayer) {
            gameState = GameState.ON_TURN;
        } else {
            gameState = GameState.WAIT_FOR_TURN;
        }

        guiEventBus.broadcastEvent(new UpdateGuiEvent());
    }

    private void onPlayerGiveUp(PlayerGiveUpEvent event) {
        gameState = GameState.ENDED;
        winner = event.getPlayer().equals(localPlayer) ? remotePlayer : localPlayer;
        guiEventBus.broadcastEvent(new GameLogGuiEvent(new GameLog(String.format("%s认输了", event.getPlayer().equals(localPlayer) ? "你" : "对方"))));
        guiEventBus.broadcastEvent(new UpdateGuiEvent());
    }

    private void onPlayerExit(PlayerExitEvent event) {
        System.out.println("Game: shutting down gui");
        guiEventBus.broadcastEvent(new ShutdownGuiEvent());
        System.out.println("Game: shutting down connection");
        try {
            gameConnection.destroy();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void onPlayerSendChatMessage(PlayerChatMessageEvent event) {
        guiEventBus.broadcastEvent(new GameLogGuiEvent(
                new GameLog(
                        String.format("%s说: %s",
                                event.getPlayer().equals(localPlayer) ? "你" : "对方",
                                event.getMessage()
                        ))
        ));
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameConnection=" + gameConnection +
                ", chessBoard=" + chessBoard +
                ", localPlayer=" + localPlayer +
                ", remotePlayer=" + remotePlayer +
                ", gameState=" + gameState +
                ", winner=" + winner +
                '}';
    }
}
