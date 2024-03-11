package javaapplication10;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchResultsPage extends JFrame {

    private JList<String> resultList;
    private DefaultListModel<String> listModel;
    private JButton bookButton;

    public SearchResultsPage(List<String> busDetails) {
        initComponents(busDetails);
         
    }

    private void initComponents(List<String> busDetails) {
        setTitle("Search Results");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Create a background label
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new BorderLayout());
        ImageIcon backgroundImageIcon = new ImageIcon("C:\\Users\\ADmin\\Downloads\\damir-kopezhanov-R1WD3mMjGPA-unsplash.jpg");
        Image backgroundImage = backgroundImageIcon.getImage();
        backgroundLabel.setIcon(new ImageIcon(backgroundImage.getScaledInstance(400, 300, Image.SCALE_SMOOTH)));


        JPanel panel = new JPanel();
         panel.setOpaque(false); // Make the panel transparent
        panel.setLayout(new BorderLayout());

        // Components
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultList.setVisibleRowCount(10);

        JScrollPane scrollPane = new JScrollPane(resultList);
        panel.add(scrollPane, BorderLayout.CENTER);

        bookButton = new JButton("Book Ticket");
        bookButton.addActionListener(e -> bookTicket());
        panel.add(bookButton, BorderLayout.SOUTH);

       // Add the panel to the background label
        backgroundLabel.add(panel, BorderLayout.CENTER);
        // Set the background label as the content pane
        setContentPane(backgroundLabel);

        // Display search results
        displaySearchResults(busDetails);
    }

    private void displaySearchResults(List<String> busDetails) {
        if (busDetails != null && !busDetails.isEmpty()) {
            for (String bus : busDetails) {
                listModel.addElement(bus);
            }
        } else {
            listModel.addElement("No matching buses found.");
            bookButton.setEnabled(false);
        }
    }

    private void bookTicket() {
        NewJFrame n = new  NewJFrame();
         n.setVisible(true);
        // Implement your logic for booking a ticket based on the selected bus details
        String selectedBus = resultList.getSelectedValue();
        if (selectedBus != null) {
            String[] details = selectedBus.split(",");

            String fromLocation = details[1];
            String toLocation = details[2];
            Date travelDate = parseDate(details[3]);

            // Create an instance of NewJFrame and pass the details
            SwingUtilities.invokeLater(() -> {
                NewJFrame newJFrame = new NewJFrame();
                newJFrame.setBusDetails(fromLocation, toLocation, (java.sql.Date) travelDate);
                newJFrame.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, "Please select a bus to book a ticket.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        List<String> sampleResults = new ArrayList<>();

        SwingUtilities.invokeLater(() -> {
            new SearchResultsPage(sampleResults).setVisible(true);
        });
    }
}
