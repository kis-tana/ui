import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class AdminPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private JTable userTable;
    private JTable deliveryTable;
    private JTable paymentTable;
    private DefaultTableModel userModel;
    private DefaultTableModel deliveryModel;
    private DefaultTableModel paymentModel;
    private JLabel totalRevenueLabel;
    private JLabel pendingPaymentsLabel;
    private JLabel completedOrdersLabel;

    public AdminPanel() {
        setLayout(new BorderLayout());

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Create tabs
        JPanel userManagementPanel = createUserManagementPanel();
        JPanel deliveryManagementPanel = createDeliveryManagementPanel();
        JPanel financialOverviewPanel = createFinancialOverviewPanel();

        // Add tabs to tabbed pane
        tabbedPane.addTab("User Management", userManagementPanel);
        tabbedPane.addTab("Delivery Management", deliveryManagementPanel);
        tabbedPane.addTab("Financial Overview", financialOverviewPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Initial data load
        loadUserData();
        loadDeliveryData();
        loadPaymentData();
        updateFinancialSummary();
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create table model with columns
        userModel = new DefaultTableModel(
                new Object[]{"User ID", "Email", "Actions"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only actions column is editable
            }
        };

        userTable = new JTable(userModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addUserBtn = new JButton("Add User");
        JButton removeUserBtn = new JButton("Remove Selected");
        buttonPanel.add(addUserBtn);
        buttonPanel.add(removeUserBtn);

        // Add button actions
        addUserBtn.addActionListener(e -> showAddUserDialog());
        removeUserBtn.addActionListener(e -> removeSelectedUsers());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDeliveryManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create table model with columns
        deliveryModel = new DefaultTableModel(
                new Object[]{"Delivery ID", "Email", "Actions"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only actions column is editable
            }
        };

        deliveryTable = new JTable(deliveryModel);
        JScrollPane scrollPane = new JScrollPane(deliveryTable);

        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addDeliveryBtn = new JButton("Add Delivery Personnel");
        JButton removeDeliveryBtn = new JButton("Remove Selected");
        buttonPanel.add(addDeliveryBtn);
        buttonPanel.add(removeDeliveryBtn);

        // Add button actions
        addDeliveryBtn.addActionListener(e -> showAddDeliveryDialog());
        removeDeliveryBtn.addActionListener(e -> removeSelectedDelivery());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFinancialOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        totalRevenueLabel = new JLabel("Total Revenue: $0.00");
        pendingPaymentsLabel = new JLabel("Pending Payments: $0.00");
        completedOrdersLabel = new JLabel("Completed Orders: 0");
        summaryPanel.add(totalRevenueLabel);
        summaryPanel.add(pendingPaymentsLabel);
        summaryPanel.add(completedOrdersLabel);

        // Create payment table
        paymentModel = new DefaultTableModel(
                new Object[]{"Order ID", "User ID", "Amount", "Date", "Status"}, 0
        );
        paymentTable = new JTable(paymentModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);

        // Add components
        panel.add(summaryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void showAddUserDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Add User", true);
        dialog.setLayout(new GridLayout(4, 2, 5, 5));

        JTextField userIdField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        dialog.add(new JLabel("User ID:"));
        dialog.add(userIdField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            String userId = userIdField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (userId.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!");
                return;
            }

            addUser(userId, email, password);
            dialog.dispose();
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(saveBtn);
        dialog.add(cancelBtn);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddDeliveryDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Add Delivery Personnel", true);
        dialog.setLayout(new GridLayout(4, 2, 5, 5));

        JTextField userIdField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        dialog.add(new JLabel("Delivery ID:"));
        dialog.add(userIdField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            String userId = userIdField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (userId.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!");
                return;
            }

            addDeliveryPerson(userId, email, password);
            dialog.dispose();
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(saveBtn);
        dialog.add(cancelBtn);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addUser(String userId, String email, String password) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("userslogin.txt", true))) {
            writer.println(userId + "," + email + "," + password);
            loadUserData(); // Refresh table
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage());
        }
    }

    private void addDeliveryPerson(String userId, String email, String password) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("deliverylogin.txt", true))) {
            writer.println(userId + "," + email + "," + password);
            loadDeliveryData(); // Refresh table
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error adding delivery person: " + ex.getMessage());
        }
    }

    private void removeSelectedUsers() {
        int[] selectedRows = userTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select users to remove");
            return;
        }

        // Get selected user IDs
        Set<String> userIdsToRemove = new HashSet<>();
        for (int row : selectedRows) {
            userIdsToRemove.add((String)userModel.getValueAt(row, 0));
        }

        // Read and write back filtered users
        try {
            ArrayList<String> remainingUsers = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("userslogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String userId = line.split(",")[0];
                if (!userIdsToRemove.contains(userId)) {
                    remainingUsers.add(line);
                }
            }
            reader.close();

            // Write back remaining users
            PrintWriter writer = new PrintWriter(new FileWriter("userslogin.txt"));
            for (String user : remainingUsers) {
                writer.println(user);
            }
            writer.close();

            loadUserData(); // Refresh table
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error removing users: " + ex.getMessage());
        }
    }

    private void removeSelectedDelivery() {
        int[] selectedRows = deliveryTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select delivery personnel to remove");
            return;
        }

        // Get selected delivery IDs
        Set<String> idsToRemove = new HashSet<>();
        for (int row : selectedRows) {
            idsToRemove.add((String)deliveryModel.getValueAt(row, 0));
        }

        // Read and write back filtered delivery personnel
        try {
            ArrayList<String> remainingDelivery = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("deliverylogin.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String deliveryId = line.split(",")[0];
                if (!idsToRemove.contains(deliveryId)) {
                    remainingDelivery.add(line);
                }
            }
            reader.close();

            // Write back remaining delivery personnel
            PrintWriter writer = new PrintWriter(new FileWriter("deliverylogin.txt"));
            for (String delivery : remainingDelivery) {
                writer.println(delivery);
            }
            writer.close();

            loadDeliveryData(); // Refresh table
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error removing delivery personnel: " + ex.getMessage());
        }
    }

    private void loadUserData() {
        userModel.setRowCount(0); // Clear existing data
        try (BufferedReader reader = new BufferedReader(new FileReader("userslogin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    userModel.addRow(new Object[]{parts[0], parts[1], "Remove"});
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + ex.getMessage());
        }
    }

    private void loadDeliveryData() {
        deliveryModel.setRowCount(0); // Clear existing data
        try (BufferedReader reader = new BufferedReader(new FileReader("deliverylogin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    deliveryModel.addRow(new Object[]{parts[0], parts[1], "Remove"});
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading delivery data: " + ex.getMessage());
        }
    }

    private void loadPaymentData() {
        paymentModel.setRowCount(0); // Clear existing data
        try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("//")) continue; // Skip comment lines
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    paymentModel.addRow(parts);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading payment data: " + ex.getMessage());
        }
    }

    private void updateFinancialSummary() {
        double totalRevenue = 0;
        double pendingPayments = 0;
        int completedOrders = 0;

        for (int i = 0; i < paymentModel.getRowCount(); i++) {
            String status = (String) paymentModel.getValueAt(i, 4);
            double amount = Double.parseDouble(((String) paymentModel.getValueAt(i, 2)).replace("$", ""));

            if (status.equals("COMPLETED")) {
                totalRevenue += amount;
                completedOrders++;
            } else if (status.equals("PENDING")) {
                pendingPayments += amount;
            }
        }

        totalRevenueLabel.setText(String.format("Total Revenue: $%.2f", totalRevenue));
        pendingPaymentsLabel.setText(String.format("Pending Payments: $%.2f", pendingPayments));
        completedOrdersLabel.setText("Completed Orders: " + completedOrders);
    }
}
