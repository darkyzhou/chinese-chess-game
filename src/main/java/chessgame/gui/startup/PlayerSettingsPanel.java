package chessgame.gui.startup;

import chessgame.game.player.PlayerFigureNames;
import chessgame.util.DocumentAdapter;
import chessgame.util.ImageResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerSettingsPanel extends JPanel {
    private final JButton nextStepButton;
    private final JTextField playerNameField;
    private final JComboBox<String> playerFigureCombo;
    private final JPanel figurePanel;

    public PlayerSettingsPanel() {
        nextStepButton = new JButton("下一步");
        nextStepButton.setEnabled(false);
        nextStepButton.setPreferredSize(new Dimension(120, 50));

        playerNameField = new JTextField();
        playerNameField.getDocument().addDocumentListener(new DocumentAdapter(e -> {
            nextStepButton.setEnabled(!playerNameField.getText().isEmpty());
        }));

        playerFigureCombo = new JComboBox<>(PlayerFigureNames.figureDisplayNames);
        playerFigureCombo.addActionListener(this::onPlayerFigureSelectionChanged);

        figurePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image figureImage = ImageResources.getImage("figure/" + PlayerFigureNames.figureFileNames[playerFigureCombo.getSelectedIndex()]);
                g.drawImage(figureImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        figurePanel.setPreferredSize(new Dimension(300, 150));
        figurePanel.setMaximumSize(new Dimension(300, 150));

        Box figureBox = new Box(BoxLayout.X_AXIS);
        figureBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        figureBox.add(Box.createHorizontalGlue());
        figureBox.add(figurePanel);
        figureBox.add(Box.createHorizontalGlue());

        Box playerNameBox = new Box(BoxLayout.X_AXIS);
        playerNameBox.add(new JLabel("你的大名: "));
        playerNameBox.add(playerNameField);
        playerNameBox.setMaximumSize(new Dimension(250, 50));
        playerNameBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        Box playerFigureBox = new Box(BoxLayout.X_AXIS);
        playerFigureBox.add(new JLabel("你的形象: "));
        playerFigureBox.add(playerFigureCombo);
        playerFigureBox.setMaximumSize(new Dimension(250, 50));
        playerFigureBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        Box centerBox = new Box(BoxLayout.Y_AXIS);
        centerBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerBox.add(Box.createRigidArea(new Dimension(0, 10)));
        centerBox.add(playerNameBox);
        centerBox.add(Box.createRigidArea(new Dimension(0, 10)));
        centerBox.add(playerFigureBox);
        centerBox.add(Box.createVerticalGlue());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(nextStepButton);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setLayout(new BorderLayout(20, 20));
        add(figureBox, BorderLayout.NORTH);
        add(centerBox, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getPlayerName() {
        return playerNameField.getText();
    }

    public String getPlayerFigureFileName() {
        return PlayerFigureNames.figureFileNames[playerFigureCombo.getSelectedIndex()];
    }

    private void onPlayerFigureSelectionChanged(ActionEvent e) {
        figurePanel.revalidate();
        figurePanel.repaint();

        System.out.println();
    }

    public void onNextStepButtonClick(ActionListener listener) {
        nextStepButton.addActionListener(listener);
    }
}