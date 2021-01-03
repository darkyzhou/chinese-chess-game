package chessgame.gui.startup;

import chessgame.game.connection.ConnectionBootstrapper;
import chessgame.util.DocumentAdapter;
import chessgame.util.Tuple;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.net.InetAddress;
import java.util.function.Consumer;

public class ConnectionSettingsPanel extends JPanel {
    private final JTextField remoteAddressField;
    private final JTextField remotePortField;
    private final JButton connectToRemoteButton;

    public ConnectionSettingsPanel(int randomlyPickedHostPort) {
        JLabel gameAsHostLabel = new JLabel(
                String.format("<html>让对手输入以下地址来进入游戏: <br><b>%s:%d<b></html>", ConnectionBootstrapper.getDefaultAddress().getHostAddress(), randomlyPickedHostPort)
        );
        gameAsHostLabel.setVerticalAlignment(SwingConstants.CENTER);
        gameAsHostLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        connectToRemoteButton = new JButton("连接");
        connectToRemoteButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        remoteAddressField = new JTextField("127.0.0.1");
        remoteAddressField.getDocument().addDocumentListener(new DocumentAdapter(e -> {
            connectToRemoteButton.setEnabled(!remoteAddressField.getText().isEmpty());
        }));
        remotePortField = new JTextField("30000");
        remotePortField.getDocument().addDocumentListener(new DocumentAdapter(e -> {
            connectToRemoteButton.setEnabled(!remotePortField.getText().isEmpty());
        }));

        JPanel gameAsHostPanel = new JPanel();
        gameAsHostPanel.setLayout(new BoxLayout(gameAsHostPanel, BoxLayout.Y_AXIS));
        gameAsHostPanel.setBorder(BorderFactory.createTitledBorder("让对手连接到本机"));
        gameAsHostPanel.setPreferredSize(new Dimension(280, 300));
        gameAsHostPanel.add(Box.createVerticalGlue());
        gameAsHostPanel.add(gameAsHostLabel);
        gameAsHostPanel.add(Box.createVerticalGlue());

        Box remoteAddressBox = new Box(BoxLayout.X_AXIS);
        remoteAddressBox.setMaximumSize(new Dimension(350, 50));
        remoteAddressBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        remoteAddressBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        remoteAddressBox.add(new JLabel("对手IP地址: "));
        remoteAddressBox.add(remoteAddressField);
        remoteAddressBox.add(new JLabel(":"));
        remoteAddressBox.add(remotePortField);

        JPanel gameAsGuestPanel = new JPanel();
        gameAsGuestPanel.setLayout(new BoxLayout(gameAsGuestPanel, BoxLayout.Y_AXIS));
        gameAsGuestPanel.setBorder(BorderFactory.createTitledBorder("连接到对手"));
        gameAsGuestPanel.setPreferredSize(new Dimension(280, 300));
        gameAsGuestPanel.add(Box.createVerticalGlue());
        gameAsGuestPanel.add(remoteAddressBox);
        gameAsGuestPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        gameAsGuestPanel.add(connectToRemoteButton);
        gameAsGuestPanel.add(Box.createVerticalGlue());

        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 100, 100));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.add(gameAsHostPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        centerPanel.add(gameAsGuestPanel);

        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(100, 10, 10, 10));
        JLabel connectMethodLabel = new JLabel("请选择一个与对手连接进行游戏的方式: ");
        connectMethodLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(connectMethodLabel);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void onConnectAsGuestButtonClick(Consumer<Tuple<InetAddress, Integer>> consumer) {
        connectToRemoteButton.addActionListener(e -> {
            InetAddress address;
            int port;
            try {
                address = InetAddress.getByName(remoteAddressField.getText());
                port = Integer.parseInt(remotePortField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "请输入正确的IP地址", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            consumer.accept(new Tuple<>(address, port));
        });
    }
}
