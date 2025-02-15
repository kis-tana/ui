import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        // Create the main application window
        JFrame frame = new JFrame("University Food Ordering System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a card layout to switch between panels
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Create panels for different screens
        LoginPanel loginPanel = new LoginPanel(cardLayout, mainPanel);
        RegisterPanel registerPanel = new RegisterPanel(cardLayout, mainPanel);
        CustomerPanel customerPanel = new CustomerPanel();
        VendorPanel vendorPanel = new VendorPanel();
        AdminPanel adminPanel = new AdminPanel();

        // Add panels to the main panel
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registerPanel, "REGISTER");
        mainPanel.add(customerPanel, "CUSTOMER");
        mainPanel.add(vendorPanel, "VENDOR");
        mainPanel.add(adminPanel, "ADMIN");

        // Show the login panel by default
        cardLayout.show(mainPanel, "LOGIN");

        // Add the main panel to the frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}