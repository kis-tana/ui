import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerPanel extends JPanel {
    private List<String> orderItems = new ArrayList<>(); // To store selected items

    public CustomerPanel() {
        setLayout(new BorderLayout());

        // Add components
        JLabel titleLabel = new JLabel("Customer Dashboard", SwingConstants.CENTER);
        JButton viewMenuButton = new JButton("View Menu");
        JButton placeOrderButton = new JButton("Place Order");
        JButton viewHistoryButton = new JButton("View Order History");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonPanel.add(viewMenuButton);
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(viewHistoryButton);
        buttonPanel.add(logoutButton);

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // View menu button action
        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMenu();
            }
        });

        // Place order button action
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });

        // View history button action
        viewHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewOrderHistory();
            }
        });

        // Logout button action
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((CardLayout) getParent().getLayout()).show(getParent(), "LOGIN");
            }
        });
    }

    private void displayMenu() {
        // Define the menu items
        String[] menuItems = {"Pizza - $10", "Burger - $5", "Pasta - $8", "Salad - $6"};
        String selectedItem = (String) JOptionPane.showInputDialog(
                this,
                "Select an item to add to your order:",
                "Menu",
                JOptionPane.PLAIN_MESSAGE,
                null,
                menuItems,
                menuItems[0]
        );

        if (selectedItem != null) {
            orderItems.add(selectedItem); // Add selected item to the order
            JOptionPane.showMessageDialog(this, "Added to order: " + selectedItem);
        }
    }

    private void placeOrder() {
        if (orderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your order is empty. Please add items from the menu.");
            return;
        }

        // Display the order summary
        StringBuilder orderSummary = new StringBuilder("Your Order:\n");
        for (String item : orderItems) {
            orderSummary.append(item).append("\n");
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                orderSummary.toString() + "\nConfirm order?",
                "Confirm Order",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Save the order to a file
            try (FileWriter writer = new FileWriter("orders.txt", true)) {
                for (String item : orderItems) {
                    writer.write(item + "\n");
                }
                writer.write("-----\n"); // Separator between orders
                JOptionPane.showMessageDialog(this, "Order placed successfully!");
                orderItems.clear(); // Clear the order after placing it
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving order!");
            }
        }
    }

    private void viewOrderHistory() {
        // Read and display order history from the file
        try {
            java.util.Scanner scanner = new java.util.Scanner(new java.io.File("orders.txt"));
            StringBuilder history = new StringBuilder("Order History:\n");
            while (scanner.hasNextLine()) {
                history.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
            JOptionPane.showMessageDialog(this, history.toString());
        } catch (java.io.FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No order history found.");
        }
    }
}