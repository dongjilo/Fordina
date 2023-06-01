package components.Custom;

import components.KapeGUI;
import components.Custom.Icons.Icons;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {
    /**
     * Initializes components necessary for the login window.
     */
    public void initLogin() {

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(400, 250);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setIconImage(Icons.LOGO);
        this.setTitle("Login");

        JLabel titleLabel = new JLabel("Fordina Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel userLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        JTextField userField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("LOGIN");
        ImageIcon userIcon = new ImageIcon(Icons.user);
        ImageIcon passIcon = new ImageIcon(Icons.pass);

        // Create a JLabel for the user icon and set it as the icon for the userLabel
        JLabel userIconLabel = new JLabel(userIcon);
        userLabel.setIconTextGap(5); // Add some spacing between the icon and the label text
        userLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        userLabel.setVerticalTextPosition(SwingConstants.CENTER);
        userLabel.setIcon(userIcon);

        JLabel passIconLabel = new JLabel(passIcon);
        userLabel.setIconTextGap(5); // Add some spacing between the icon and the label text
        userLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        userLabel.setVerticalTextPosition(SwingConstants.CENTER);
        passwordLabel.setIcon(passIcon);

        JPanel loginPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(titleLabel, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 200);
        loginPanel.add(userLabel, gbc);

        gbc.insets = new Insets(10, 100, 10, 0);
        loginPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 10, 200);
        loginPanel.add(passwordLabel, gbc);

        gbc.insets = new Insets(10, 100, 10, 0);
        loginPanel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 10, 0);
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> openMainWindow());

        this.add(loginPanel);
        getRootPane().setDefaultButton(loginButton);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Opens the main window KapeGUI and disposes the login window.
     */
    private void openMainWindow() {
        KapeGUI mainWindow = new KapeGUI();
        mainWindow.init();
        this.dispose();
    }
}
