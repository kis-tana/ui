import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public LoginPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create role selection
        JLabel roleLabel = new JLabel("Login as:");
        String[] roles = {"User", "Admin", "Delivery"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        // Add components
        JLabel userLabel = new JLabel("User ID:");
        JTextField userField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Add components with GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        add(roleLabel, gbc);
        gbc.gridx = 1;
        add(roleComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(userLabel, gbc);
        gbc.gridx = 1;
        add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(loginButton, gbc);
        gbc.gridx = 1;
        add(registerButton, gbc);

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userField.getText();
                String password = new String(passwordField.getPassword());
                String selectedRole = (String) roleComboBox.getSelectedItem();

                // Get the appropriate login file based on role
                String loginFile;
                String targetPanel;
                switch (selectedRole) {
                    case "Admin":
                        loginFile = "adminlogin.txt";
                        targetPanel = "ADMIN";
                        break;
                    case "Delivery":
                        loginFile = "deliverylogin.txt";
                        targetPanel = "DELIVERY";
                        break;
                    default:
                        loginFile = "userslogin.txt";
                        targetPanel = "CUSTOMER";
                        break;
                }

                // Validate credentials
                boolean isValidUser = false;
                try (BufferedReader reader = new BufferedReader(new FileReader(loginFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.trim().split(",");
                        if (parts.length >= 3) {
                            String storedUsername = parts[0].trim();
                            String storedPassword = parts[2].trim();

                            if (userID.equals(storedUsername) && password.equals(storedPassword)) {
                                isValidUser = true;
                                break;
                            }
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Error reading credentials!");
                    return;
                }

                if (isValidUser) {
                    cardLayout.show(mainPanel, targetPanel);
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

    private void initializeDefaultUsers() {
        // Check if admin exists, if not create default admin
        boolean adminExists = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("adminlogin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length >= 3 && parts[0].equals("admin")) {
                    adminExists = true;
                    break;
                }
            }
        } catch (IOException ex) {
            // File doesn't exist or error reading
        }

        if (!adminExists) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("adminlogin.txt", true))) {
                // Add default admin
                writer.println("admin,admin@system.com,admin123");
            } catch (IOException ex) {
                System.err.println("Error creating default admin: " + ex.getMessage());
            }
        }

        // Check if delivery exists, if not create default delivery
        boolean deliveryExists = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("deliverylogin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length >= 3 && parts[0].equals("delivery1")) {
                    deliveryExists = true;
                    break;
                }
            }
        } catch (IOException ex) {
            // File doesn't exist or error reading
        }

        if (!deliveryExists) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("deliverylogin.txt", true))) {
                // Add default delivery
                writer.println("delivery1,delivery@system.com,delivery123");
            } catch (IOException ex) {
                System.err.println("Error creating default delivery: " + ex.getMessage());
            }
        }
    }
}