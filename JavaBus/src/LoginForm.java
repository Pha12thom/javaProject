import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private List<User> users;

    public LoginForm(List<User> users) {
        this.users = users;

        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement login logic here
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check if the username and password match any user
                boolean loginSuccessful = false;
                for (User user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        loginSuccessful = true;
                        break;
                    }
                }

                if (loginSuccessful) {
                    // Successful login
                    JOptionPane.showMessageDialog(null, "Login successful!");

                    // Add logic to proceed to the main bus booking page or any other action
                } else {
                    // Login failed
                    JOptionPane.showMessageDialog(null, "Login failed. Please try again.");
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    // Method to display the login form
    public static void showLoginForm(List<User> users) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm(users);
            }
        });
    }
}
