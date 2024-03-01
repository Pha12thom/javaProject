import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SignupForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signupButton;
    private List<User> users;

    public SignupForm(List<User> users) {
        this.users = users;

        setTitle("Signup Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        signupButton = new JButton("Signup");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(signupButton);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Perform signup logic here
                if (validateSignup(username, password)) {
                    // Successful signup
                    JOptionPane.showMessageDialog(null, "Signup successful!");

                    // Close the signup form
                    dispose();

                    // Open the login form
                    LoginForm.showLoginForm(users);
                } else {
                    // Signup failed
                    JOptionPane.showMessageDialog(null, "Signup failed. Please try again.");
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    private boolean validateSignup(String username, String password) {
        // Validate if the username is not already taken
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(null, "Username already taken. Please choose another.");
                return false;
            }
        }

        // If the username is unique, add the new user
        users.add(new User(username, password));
        return true;
    }

    public static void main(String[] args) {
        List<User> users = new ArrayList<>(); // Create a new list to store users
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SignupForm(users);
            }
        });
    }
}
