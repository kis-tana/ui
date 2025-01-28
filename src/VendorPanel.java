import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VendorPanel extends JPanel {
    public VendorPanel() {
        setLayout(new BorderLayout());

        // Add components
        JLabel titleLabel = new JLabel("Vendor Dashboard", SwingConstants.CENTER);
        JButton addItemButton = new JButton("Add Menu Item");
        JButton viewOrdersButton = new JButton("View Orders");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.add(addItemButton);
        buttonPanel.add(viewOrdersButton);
        buttonPanel.add(logoutButton);

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Add item button action
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(VendorPanel.this, "Adding menu item...");
            }
        });

        // View orders button action
        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(VendorPanel.this, "Displaying orders...");
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
}