package chessgame.gui.game;

import chessgame.game.Game;
import chessgame.game.GameState;
import chessgame.game.event.PlayerChatMessageEvent;
import chessgame.game.event.PlayerGiveUpEvent;
import chessgame.game.event.PlayerReplyRetractionEvent;
import chessgame.game.event.PlayerRequestRetractionEvent;
import chessgame.game.guievent.GameLogGuiEvent;
import chessgame.game.guievent.ResetGuiEvent;
import chessgame.game.guievent.UpdateGuiEvent;
import chessgame.util.ImageResources;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.*;

public class GameInfoPanel extends JPanel {
    private final JEditorPane gameLogPane;
    private final JScrollPane gameLogScrollPane;
    private final JTextField messageField;
    private final JButton retractButton;
    private final JButton giveUpButton;
    private final Game game;

    private final StringBuilder logStringBuilder;

    public GameInfoPanel(Game game) {
        this.game = game;

        logStringBuilder = new StringBuilder();

        Image backgroundImage = ImageResources.getImage("frame/log-panel-background.jpg");
        gameLogPane = new JEditorPane() {
            @Override
            protected void paintComponent(Graphics g) {
                for (int i = 0; i < Math.ceil(getHeight() / 240d); i++) {
                    g.drawImage(backgroundImage, 0, 240 * i, this);
                }
                super.paintComponent(g);
            }
        };
        gameLogPane.setEditorKit(new HTMLEditorKit());
        gameLogPane.setEditable(false);
        gameLogPane.setBorder(BorderFactory.createEmptyBorder());
        gameLogPane.setOpaque(false);

        StyleSheet styleSheet = ((HTMLDocument) gameLogPane.getDocument()).getStyleSheet();
        styleSheet.addRule("body { font-size: 8px; color: white; padding: 5px; }");
        styleSheet.addRule("p { margin-top: 0; margin-bottom: 5px; }");

        gameLogScrollPane = new JScrollPane(gameLogPane);
        gameLogScrollPane.setBorder(BorderFactory.createEmptyBorder());
        gameLogScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        messageField = new JTextField("输入文字并按回车发送...");
        messageField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        messageField.setForeground(Color.GRAY);
        messageField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (messageField.getText().equals("输入文字并按回车发送...")) {
                    messageField.setText("");
                    messageField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (messageField.getText().isEmpty()) {
                    messageField.setForeground(Color.GRAY);
                    messageField.setText("输入文字并按回车发送...");
                }
            }
        });
        messageField.addActionListener(this::onMessageFieldEnterKeyPressed);

        JPanel logAndMessagePanel = new JPanel();
        logAndMessagePanel.setLayout(new BorderLayout(0, 0));
        logAndMessagePanel.add(gameLogScrollPane, BorderLayout.CENTER);
        logAndMessagePanel.add(messageField, BorderLayout.SOUTH);

        PlayerInfoPanel localPlayerInfoPanel = new PlayerInfoPanel(game, game.getLocalPlayer());
        localPlayerInfoPanel.setPreferredSize(new Dimension(250, 150));
        localPlayerInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        PlayerInfoPanel remotePlayerInfoPanel = new PlayerInfoPanel(game, game.getRemotePlayer());
        remotePlayerInfoPanel.setPreferredSize(new Dimension(250, 150));
        remotePlayerInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.add(remotePlayerInfoPanel, BorderLayout.NORTH);
        contentPanel.add(logAndMessagePanel, BorderLayout.CENTER);
        contentPanel.add(localPlayerInfoPanel, BorderLayout.SOUTH);

        retractButton = new JButton("悔棋");
        retractButton.setEnabled(false);
        retractButton.addActionListener(this::onRetractButtonClick);

        giveUpButton = new JButton("认输");
        giveUpButton.setEnabled(false);
        giveUpButton.addActionListener(this::onGiveUpButtonClick);

        Box buttonsBox = new Box(BoxLayout.X_AXIS);
        buttonsBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonsBox.add(Box.createHorizontalGlue());
        buttonsBox.add(retractButton);
        buttonsBox.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsBox.add(giveUpButton);
        buttonsBox.add(Box.createHorizontalGlue());

        setLayout(new BorderLayout(0, 0));
        add(contentPanel, BorderLayout.CENTER);
        add(buttonsBox, BorderLayout.SOUTH);

        game.getGuiEventBus().registerEventListener(GameLogGuiEvent.class, e -> EventQueue.invokeLater(() -> onGameLogEvent(e)));
        game.getGameConnection().getEventBus().registerEventListener(PlayerReplyRetractionEvent.class, e -> EventQueue.invokeLater(() -> onRemotePlayerReplyRetraction(e)));
        game.getGuiEventBus().registerEventListener(ResetGuiEvent.class, e -> EventQueue.invokeLater(this::resetGameLogPaneText));
        game.getGuiEventBus().registerEventListener(UpdateGuiEvent.class, e -> EventQueue.invokeLater(this::onPlayerMoveChess));

        resetGameLogPaneText();
    }

    private void onRetractButtonClick(ActionEvent e) {
        retractButton.setText("等待同意");
        retractButton.setEnabled(false);
        game.getGameEventBus().broadcastEvent(new PlayerRequestRetractionEvent(game.getLocalPlayer()));
    }

    private void onRemotePlayerReplyRetraction(PlayerReplyRetractionEvent event) {
        retractButton.setText("悔棋");
        retractButton.setEnabled(true);
    }

    private void onPlayerMoveChess() {
        retractButton.setEnabled(game.getGameState() == GameState.ON_TURN);
        giveUpButton.setEnabled(game.getGameState() == GameState.ON_TURN || game.getGameState() == GameState.WAIT_FOR_TURN);
    }

    private void onGiveUpButtonClick(ActionEvent e) {
        game.getGameEventBus().broadcastEvent(new PlayerGiveUpEvent(game.getLocalPlayer()));
    }

    private void onMessageFieldEnterKeyPressed(ActionEvent e) {
        if (messageField.getText().isEmpty()) {
            return;
        }
        game.getGameEventBus().broadcastEvent(new PlayerChatMessageEvent(game.getLocalPlayer(), messageField.getText()));
        messageField.setText("");
    }

    private void appendGameLogPaneText(String text) {
        logStringBuilder.append("<p>").append(text).append("</p>");
        gameLogPane.setText(logStringBuilder.toString());
        JScrollBar scrollBar = gameLogScrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }

    private void resetGameLogPaneText() {
        appendGameLogPaneText("--- 对局开始 ---");
        appendGameLogPaneText("先手: " + game.getOnTurnPlayer().getName());
    }

    private void onGameLogEvent(GameLogGuiEvent event) {
        System.out.println("GameInfoPanel: received new log: " + event.getGameLog().getContent());
        appendGameLogPaneText(event.getGameLog().getContent());
    }
}
