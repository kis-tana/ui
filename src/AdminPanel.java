import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JPanel {
    public AdminPanel() {
        setLayout(new BorderLayout());

        // Add components
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        JButton manageUsersButton = new JButton("Manage Users");
        JButton topUpCreditButton = new JButton("Top-Up Customer Credit");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.add(manageUsersButton);
        buttonPanel.add(topUpCreditButton);
        buttonPanel.add(logoutButton);

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Manage users button action
        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AdminPanel.this, "Managing users...");
            }
        });

        // Top-up credit button action
        topUpCreditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AdminPanel.this, "Topping up customer credit...");
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
