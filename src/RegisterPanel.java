import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.regex.Pattern;

public class RegisterPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public RegisterPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create role selection
        JLabel roleLabel = new JLabel("Register as:");
        String[] roles = {"User", "Admin", "Delivery"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        // Add components
        JLabel userLabel = new JLabel("User ID:");
        JTextField userField = new JTextField(20);
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

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
        add(emailLabel, gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(registerButton, gbc);
        gbc.gridy = 5;
        add(backButton, gbc);

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());
                String selectedRole = (String) roleComboBox.getSelectedItem();

                // Validate input
                if (userID.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "All fields are required!");
                    return;
                }

                // Validate email format
                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Invalid email format!");
                    return;
                }

                // Get the appropriate registration file based on role
                String registrationFile;
                switch (selectedRole) {
                    case "Admin":
                        registrationFile = "adminlogin.txt";
                        break;
                    case "Delivery":
                        registrationFile = "deliverylogin.txt";
                        break;
                    default:
                        registrationFile = "userslogin.txt";
                        break;
                }

                // Check if user already exists
                try (BufferedReader reader = new BufferedReader(new FileReader(registrationFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.trim().split(",");
                        if (parts.length >= 1 && parts[0].trim().equals(userID)) {
                            JOptionPane.showMessageDialog(RegisterPanel.this, "User ID already exists!");
                            return;
                        }
                    }
                } catch (IOException ex) {
                    // If file doesn't exist, create it
                    try {
                        new File(registrationFile).createNewFile();
                    } catch (IOException createEx) {
                        JOptionPane.showMessageDialog(RegisterPanel.this, "Error creating registration file!");
                        return;
                    }
                }

                // Register user
                try (FileWriter fw = new FileWriter(registrationFile, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter writer = new PrintWriter(bw)) {

                    writer.println(userID + "," + email + "," + password);
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Registration successful!");

                    // Clear fields
                    userField.setText("");
                    emailField.setText("");
                    passwordField.setText("");

                    // Go back to login
                    cardLayout.show(mainPanel, "LOGIN");
                } catch (IOException ex) {
                    ex.printStackTrace(); // For debugging
                    JOptionPane.showMessageDialog(RegisterPanel.this, "Error registering user: " + ex.getMessage());
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