package components.Custom;

import components.KapeGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    public void initLogin() {

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(400, 250);
        this.setLocationRelativeTo(null);
        this.setTitle("Login");

        JLabel titleLabel = new JLabel("Fordina Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel userLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");

        JTextField userField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("LOGIN");

        JPanel loginPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,2,2,2);
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(titleLabel, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,10,200);
        loginPanel.add(userLabel, gbc);

        gbc.insets = new Insets(10,100,10,0);
        loginPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10,0,10,200);
        loginPanel.add(passwordLabel, gbc);

        gbc.insets = new Insets(10,100,10,0);
        loginPanel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(10,0,10,0);
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMainWindow();
            }
        });


        this.add(loginPanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }

    private void openMainWindow() {
        KapeGUI mainWindow = new KapeGUI();
        mainWindow.init();
        this.dispose();
    }
}
