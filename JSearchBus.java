package javaapplication10;


import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchBusesPage extends JFrame {

    private JTextField fromField;
    private JTextField toField;
    private JDateChooser dateChooser;
    private JButton searchButton;
    private JButton backButton;
    private JButton exitButton;

    public SearchBusesPage() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Search Buses");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size to 3/4 of the screen width and height
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.75);
        int height = (int) (screenSize.getHeight() * 0.75);
        setSize(width, height);

        // Center the window on the screen
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        // Components
        JLabel fromLabel = new JLabel("From:");
        JLabel toLabel = new JLabel("To:");

        fromField = new JTextField();
        toField = new JTextField();
        dateChooser = new JDateChooser();
        searchButton = createStyledButton("Search Buses", "search.png", e -> searchBuses());
        backButton = createStyledButton("Back", "back.png", e -> goBack());
        exitButton = createStyledButton("Exit", "exit.png", e -> exitApplication());

        // Adding components to the panel
        panel.add(fromLabel);
        panel.add(fromField);
        panel.add(toLabel);
        panel.add(toField);
        panel.add(new JLabel("Date:"));
        panel.add(dateChooser);
        panel.add(new JLabel()); // Placeholder
        panel.add(searchButton);
        panel.add(backButton);
        panel.add(exitButton);

        // Add padding around the panel
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        paddedPanel.add(panel, BorderLayout.CENTER);

        // Add padding to the buttons
        searchButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        backButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        exitButton.setBorder(new EmptyBorder(10, 20, 10, 20));

        add(paddedPanel);
    }

    private JButton createStyledButton(String text, String iconFileName, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);

        // Set button icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconFileName));
            button.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Style the button
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 152, 219));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));

        return button;
    }

    private void searchBuses() {
        String from = fromField.getText();
        String to = toField.getText();
        Date selectedDate = dateChooser.getDate();

        if (validateInput(from, to, selectedDate)) {
            try {
                // Database connection
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/book", "root", "");

                // Search query
                String searchQuery = "SELECT * FROM bus_details WHERE from_location=? AND to_location=? AND travel_date=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {
                    preparedStatement.setString(1, from);
                    preparedStatement.setString(2, to);
                    preparedStatement.setDate(3, new java.sql.Date(selectedDate.getTime()));

                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Process search results
                    List<String> searchResults = new ArrayList<>();
                    while (resultSet.next()) {
                        String fromLocation = resultSet.getString("from_location");
                        String toLocation = resultSet.getString("to_location");
                        Date travelDate = resultSet.getDate("travel_date");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = dateFormat.format(travelDate);

                        searchResults.add("," + fromLocation + "," + toLocation + "," + formattedDate);
                    }

                    // Open SearchResultsPage with the results
                    SwingUtilities.invokeLater(() -> {
                        new SearchResultsPage(searchResults).setVisible(true);
                    });

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to execute search query", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database connection error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input. Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBack() {
        ////
    }

    private void exitApplication() {
        System.exit(0);
    }

    private boolean validateInput(String from, String to, Date date) {
        return !from.isEmpty() && !to.isEmpty() && date != null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SearchBusesPage().setVisible(true);
        });
    }
}



