import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public RegisterPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new GridLayout(5, 2));

        // Add components
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
        add(backButton);

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // Store user information in a text file
                try (FileWriter writer = new FileWriter("users.txt", true)) {
                    writer.write(name + "," + email + "," + password + "\n");
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Registration successful!");
                    cardLayout.show(mainPanel, "LOGIN");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Error saving user information!");
                }
            }
        });

        // Back button action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "LOGIN");
            }
        });
    }
}