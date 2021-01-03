package chessgame.gui.game;

import chessgame.game.guievent.GameLog;
import chessgame.game.event.GameEvent;
import chessgame.game.event.PlayerExitEvent;
import chessgame.game.event.PlayerMoveChessEvent;
import chessgame.game.Game;
import chessgame.game.GameState;
import chessgame.game.chessboard.ChessBoard;
import chessgame.game.chessboard.MoveChessAction;
import chessgame.game.chess.Chess;
import chessgame.game.chess.ChessSide;
import chessgame.game.event.PlayerRequestRestartEvent;
import chessgame.game.guievent.GameLogGuiEvent;
import chessgame.game.guievent.UpdateGuiEvent;
import chessgame.game.guievent.ResetGuiEvent;
import chessgame.util.ImageResources;
import chessgame.util.Position;
import chessgame.util.Tuple;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardPanel extends JPanel {
    private final Image chessBoardBackgroundImage;
    private final Box buttonsBox;
    private final JButton restartButton;
    private final JButton exitButton;

    private final Game game;
    private final List<Tuple<Position, ChessSide>> highlightedPositions;

    private Chess lastSelectedOwnChess = null;
    private Position lastSelectedOwnChessPosition = null;

    private PlayerRequestRestartEvent localRestartRequest = null;
    private PlayerRequestRestartEvent remoteRestartRequest = null;

    public ChessBoardPanel(Game game) {
        chessBoardBackgroundImage = ImageResources.getImage("chess/chess-board.jpg");
        setLayout(null);

        restartButton = new JButton("再来一局");
        restartButton.addActionListener(this::onRestartGameButtonClick);

        exitButton = new JButton("离开");
        exitButton.addActionListener(this::onExitButtonClick);

        buttonsBox = new Box(BoxLayout.X_AXIS);
        buttonsBox.add(Box.createHorizontalGlue());
        buttonsBox.add(restartButton);
        buttonsBox.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsBox.add(exitButton);
        buttonsBox.add(Box.createHorizontalGlue());
        buttonsBox.setBounds(0, 400, 550, 60);
        buttonsBox.setVisible(false);
        add(buttonsBox);

        this.game = game;
        highlightedPositions = new ArrayList<>();

        game.getGameConnection().getEventBus().registerEventListener(PlayerRequestRestartEvent.class, (e) -> EventQueue.invokeLater(() -> onRemotePlayerRequestRestart(e)));

        game.getGuiEventBus().registerEventListener(UpdateGuiEvent.class, (e) -> EventQueue.invokeLater(this::onChessBoardUpdate));
        game.getGuiEventBus().registerEventListener(ResetGuiEvent.class, (e) -> EventQueue.invokeLater(this::onGameReset));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onChessBoardClick(e);
            }
        });
    }

    private void onExitButtonClick(ActionEvent e) {
        game.getGameEventBus().broadcastEvent(new PlayerExitEvent(game.getLocalPlayer()));
    }

    private void onRestartGameButtonClick(ActionEvent e) {
        if (remoteRestartRequest == null) {
            boolean localFirst = Math.random() < 0.5d;
            localRestartRequest = new PlayerRequestRestartEvent(game.getLocalPlayer(), ChessSide.randomSide(), localFirst);
            game.getGameEventBus().broadcastEvent(localRestartRequest);
            restartButton.setEnabled(false);
            restartButton.setText("申请已发送");
        } else {
            ChessSide localSide = remoteRestartRequest.getSide().getReverse();
            boolean remoteFirst = remoteRestartRequest.isFirst();
            localRestartRequest = new PlayerRequestRestartEvent(game.getLocalPlayer(), localSide, !remoteFirst);
            game.getGameEventBus().broadcastEvent(localRestartRequest);
            resetWithRemoteRequest();
        }
    }

    private void onRemotePlayerRequestRestart(PlayerRequestRestartEvent event) {
        remoteRestartRequest = event;
        if (localRestartRequest == null) {
            restartButton.setText("再来一局（对方已申请）");
        } else {
            resetWithRemoteRequest();
        }
    }

    private void resetWithRemoteRequest() {
        ChessSide localSide = remoteRestartRequest.getSide().getReverse();
        boolean remoteFirst = remoteRestartRequest.isFirst();

        game.reset(!remoteFirst, localSide);
        localRestartRequest = null;
        remoteRestartRequest = null;
        restartButton.setEnabled(true);
        restartButton.setText("再来一局");
        buttonsBox.setVisible(false);

        revalidate();
        repaint();
    }

    private void onGameReset() {
        removeAllHighlightedPositions();
        revalidate();
        repaint();
    }

    private void onChessBoardUpdate() {
        System.out.println("ChessBoardPanel: updating chess board");

        removeAllHighlightedPositions();
        MoveChessAction action = game.getChessBoard().getActions().peekLast();
        if (action == null) {
            System.out.println("ChessBoardPanel: no moved chess found");
            return;
        }

        ChessSide moveSide = action.getMovedChess().getSide();
        ChessSide localSide = game.getLocalPlayer().getSide();
        System.out.println(String.format("ChessBoardPanel: checking moved chess %s(%s->%s)", action.getMovedChess(), action.getFrom(), action.getTo()));
        if (localSide == ChessSide.HAN) {
            highlightAbsolutePosition(action.getFrom(), moveSide);
            highlightAbsolutePosition(action.getTo(), moveSide);
        } else {
            highlightAbsolutePosition(ChessBoard.transformRelativeToChuSide(action.getFrom()), moveSide);
            highlightAbsolutePosition(ChessBoard.transformRelativeToChuSide(action.getTo()), moveSide);
        }

        if (game.getGameState() == GameState.ENDED) {
            buttonsBox.setVisible(true);
        }

        revalidate();
        repaint();
    }

    private void removeHighlightedPositionBySide(ChessSide side) {
        highlightedPositions.removeIf(tuple -> tuple.getSecond() == side);
        revalidate();
        repaint();
    }

    private void removeAllHighlightedPositions() {
        highlightedPositions.clear();
        revalidate();
        repaint();
    }

    private void highlightAbsolutePosition(Position position, ChessSide side) {
        highlightedPositions.add(new Tuple<>(position, side));
        revalidate();
        repaint();
    }

    private void onChessBoardClick(MouseEvent e) {
        if (game.getGameState() != GameState.ON_TURN) {
            return;
        }

        ChessSide currentSide = game.getLocalPlayer().getSide();
        int x = Math.round((e.getX() - 45f) / 58);
        int y = Math.round((e.getY() - 45f) / 57);
        Position absolutePosition = new Position(x, y);
        Position relativePosition = currentSide == ChessSide.HAN ?
                absolutePosition : ChessBoard.transformRelativeToChuSide(absolutePosition);
        Chess selected = game.getChessBoard().getChessAt(relativePosition);
        if (selected == null) {
            if (lastSelectedOwnChess == null) {
                return;
            }
        } else {
            if (lastSelectedOwnChess == null || selected.getSide() == currentSide) {
                lastSelectedOwnChess = selected;
                lastSelectedOwnChessPosition = relativePosition;
                removeHighlightedPositionBySide(currentSide);
                highlightAbsolutePosition(absolutePosition, currentSide);
                return;
            }
        }

        if (!game.getChessBoard().canMoveChess(lastSelectedOwnChessPosition, relativePosition)) {
            game.getGuiEventBus().broadcastEvent(new GameLogGuiEvent(new GameLog(String.format("你不能这么走【%s】", lastSelectedOwnChess.getDisplayName()))));
            return;
        }

        removeAllHighlightedPositions();
        GameEvent event = new PlayerMoveChessEvent(game.getLocalPlayer(), lastSelectedOwnChessPosition, relativePosition);
        game.getGameEventBus().broadcastEvent(event);
        lastSelectedOwnChess = null;
        lastSelectedOwnChessPosition = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(chessBoardBackgroundImage, 0, 0, this);

        ChessSide currentSide = game.getLocalPlayer().getSide();
        for (int i = 0; i < ChessBoard.BOARD_WIDTH; i++) {
            for (int j = 0; j < ChessBoard.BOARD_LENGTH; j++) {
                Position position = new Position(i, j);
                if (currentSide == ChessSide.CHU) {
                    position = ChessBoard.transformRelativeToChuSide(position);
                }
                Chess chess = game.getChessBoard().getChessAt(position);
                if (chess != null) {
                    g2d.drawImage(chess.getImage(), 20 + i * 58, 20 + j * 57, 50, 50, this);
                }
            }
        }

        for (Tuple<Position, ChessSide> tuple : highlightedPositions) {
            Position position = tuple.getFirst();
            ChessSide side = tuple.getSecond();
            int startX = 20 + position.getX() * 58 - 2;
            int startY = 20 + position.getY() * 57 - 1;
            g2d.setColor(side == ChessSide.CHU ? Color.BLUE : Color.RED);
            g2d.setStroke(new BasicStroke(3));

            g2d.drawLine(startX, startY, startX + 20, startY);
            g2d.drawLine(startX, startY, startX, startY + 20);
            g2d.drawLine(startX + 30, startY, startX + 50, startY);
            g2d.drawLine(startX + 50, startY, startX + 50, startY + 20);
            g2d.drawLine(startX, startY + 30, startX, startY + 50);
            g2d.drawLine(startX, startY + 50, startX + 20, startY + 50);
            g2d.drawLine(startX + 50, startY + 30, startX + 50, startY + 50);
            g2d.drawLine(startX + 30, startY + 50, startX + 50, startY + 50);
        }

        if (game.getGameState() == GameState.ENDED) {
            g2d.drawImage(ImageResources.getImage("game/shade.png"), 0, 0, this);
            Image image = ImageResources.getImage(game.getWinner().equals(game.getLocalPlayer()) ?
                    "game/game-victory.png" :
                    "game/game-failure.png");
            g2d.drawImage(image, 75, 200, this);
        }

        g2d.dispose();
    }
}
