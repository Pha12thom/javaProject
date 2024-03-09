package javaapplication10;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginAsUserButton;
    private JButton loginAsAdminButton;
    private JButton registerOptionButton;
    private JLabel statusLabel;
    private AdminDashboard adminDashboard;

    public LoginPage() {
        initComponents();
    }

    private void initComponents() {
        setTitle("User Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set size to occupy 3/4 of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.75);
        int height = (int) (screenSize.getHeight() * 0.75);
        setSize(width, height);

        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(240, 240, 240));
        setTitle("User Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        // Add two new buttons for login as user and login as admin
        loginAsUserButton = createStyledButton("Login as User");
        loginAsAdminButton = createStyledButton("Login as Admin");

        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);

        registerOptionButton = new JButton("Don't have an account? Register");
        registerOptionButton.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(loginAsUserButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(loginAsAdminButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(statusLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(registerOptionButton, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 80, 130));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginAsUserButton) {
            loginUserAs("User");
        } else if (e.getSource() == loginAsAdminButton) {
            loginUserAs("Admin");
        } else if (e.getSource() == registerOptionButton) {
            openRegistrationPage();
        }
    }

private void loginUserAs(String role) {
    SwingUtilities.invokeLater(() -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password are required");
            return;
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/book";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String selectQuery = "SELECT * FROM register WHERE username = ? AND password = ? AND role = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, role);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    statusLabel.setText("Login successful!");

                    // Check the user role
                    if ("Admin".equals(role)) {
                        openAdminDashboard();
                    } else {
                        openSearchBusesPage();
                    }

                    // Dispose of the form only when login is successful
                    dispose();
                } else {
                    statusLabel.setText("Invalid username, password, or role");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}


    private void openSearchBusesPage() {
        SwingUtilities.invokeLater(() -> {
            SearchBusesPage searchBusesPage = new SearchBusesPage();
            searchBusesPage.setVisible(true);
            dispose();
        });
    }

    private void openAdminDashboard() {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.setVisible(true);
            dispose();
        });
    }

    private void openRegistrationPage() {
        SwingUtilities.invokeLater(() -> {
            RegistrationPage registrationPage = new RegistrationPage();
            registrationPage.setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}
