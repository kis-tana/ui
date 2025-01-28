import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public LoginPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new GridLayout(4, 2));

        // Add components
        JLabel userLabel = new JLabel("User ID:");
        JTextField userField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        add(userLabel);
        add(userField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userField.getText();
                String password = new String(passwordField.getPassword());

                // Simulate login logic
                if (userID.equals("kistana") && password.equals("admin1")) {
                    cardLayout.show(mainPanel, "CUSTOMER");
                } else if (userID.equals("vend1") && password.equals("pizza123")) {
                    cardLayout.show(mainPanel, "VENDOR");
                } else if (userID.equals("admin1") && password.equals("admin123")) {
                    cardLayout.show(mainPanel, "ADMIN");
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid credentials!");
                }
            }
        });

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "REGISTER");
            }
        });
    }
}