import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends JFrame {
    private double calculateEstimatedPrice(String initialPoint, String finalDestination) {
        return Math.random() * 100 + 20;
    }

    private boolean checkTaxiAvailability(String initialPoint, String finalDestination) {
        return Math.random() > 0.5;
    }

    private double walletBalance;
    private JLabel clockLabel;
    private JLabel notificationLabel;
    private JButton showNotificationsButton;
    private JLabel walletLabel;
    private ArrayList<Taxi> taxis;
    private DefaultListModel<String> notificationsModel;
    private JList<String> notificationList;
    private JComboBox<String> taxiComboBox;

    // Color palette based on provided image
    private final Color COLOR_1 = new Color(7, 47, 71);
    private final Color COLOR_2 = new Color(7, 79, 148);
    private final Color COLOR_3 = new Color(8, 113, 201);
    private final Color COLOR_4 = new Color(136, 187, 220);
    private final Color COLOR_5 = new Color(114, 148, 155);

    public MainMenu(double initialBalance) {
        this.walletBalance = initialBalance;
        taxis = new ArrayList<>();
        notificationsModel = new DefaultListModel<>();
        initializeTaxis();
        addSampleNewsNotifications();

        setTitle("Taxi Service Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem themeMenuItem = new JMenuItem("Switch Theme");
        editMenu.add(themeMenuItem);
        menuBar.add(editMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        saveMenuItem.addActionListener(e -> saveTaxiData());
        exitMenuItem.addActionListener(e -> System.exit(0));
        themeMenuItem.addActionListener(e -> switchTheme());
        aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Taxi Service Dashboard v2.0", "About", JOptionPane.INFORMATION_MESSAGE));

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(COLOR_1);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setBackground(COLOR_2);

        JLabel logoLabel = new JLabel("Taxi Service Dashboard", JLabel.LEFT);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 28));
        logoLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel, BorderLayout.WEST);

        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Arial", Font.BOLD, 24));
        clockLabel.setForeground(Color.WHITE);
        headerPanel.add(clockLabel, BorderLayout.CENTER);
        startClock();

        JTextField searchField = new JTextField("Search...");
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setBackground(COLOR_5);
        searchField.setForeground(Color.WHITE);
        searchField.setBorder(BorderFactory.createLineBorder(COLOR_3));
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.setBackground(COLOR_3);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        searchButton.addActionListener(e -> {
            String searchQuery = searchField.getText().trim();
            if (!searchQuery.isEmpty()) {
                StringBuilder result = new StringBuilder();
                for (Taxi taxi : taxis) {
                    if (taxi.getTaxiNumber().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        searchQuery.equalsIgnoreCase(taxi.getStartTime()) ||
                        searchQuery.equalsIgnoreCase(taxi.getEndTime())) {
                        result.append("Taxi Number: ").append(taxi.getTaxiNumber())
                                .append(", Available Start Time: ").append(taxi.getStartTime())
                                .append(", Available End Time: ").append(taxi.getEndTime()).append("\n");
                    }
                }
                if (result.length() == 0) {
                    JOptionPane.showMessageDialog(this, "No taxis found for the given search.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, result.toString(), "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainContainer.add(headerPanel, BorderLayout.NORTH);

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(COLOR_1);
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));

        Color buttonColor = COLOR_3;

        JButton bookTaxiButton = createModernSidebarButton("Book a Taxi", buttonColor);
        JButton nearbyTaxiButton = createModernSidebarButton("See Nearby Taxis", buttonColor);
        JButton taxiRoutesButton = createModernSidebarButton("View Taxi Routes", buttonColor);
        JButton taxiScheduleButton = createModernSidebarButton("Taxi Schedule", buttonColor);
        JButton themeSwitcher = createModernSidebarButton("Switch Theme", buttonColor);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(bookTaxiButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(nearbyTaxiButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(taxiRoutesButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(taxiScheduleButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(themeSwitcher);

        mainContainer.add(sidebarPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setOpaque(false);

        JPanel walletPanel = new JPanel(new GridBagLayout());
        walletPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        walletPanel.setOpaque(false);

        walletLabel = new JLabel(String.format("Wallet Balance: R%.2f", walletBalance), SwingConstants.CENTER);
        walletLabel.setFont(new Font("Arial", Font.BOLD, 28));
        walletLabel.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel inputPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(COLOR_5);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        inputPanel.setLayout(new GridLayout(3, 2, 20, 20));
        inputPanel.setOpaque(false);

        JLabel initialPointLabel = new JLabel("Initial Point:");
        initialPointLabel.setFont(new Font("Arial", Font.BOLD, 20));
        initialPointLabel.setForeground(Color.WHITE);
        JTextField initialPointField = new JTextField();
        initialPointField.setBackground(COLOR_5);
        initialPointField.setForeground(Color.WHITE);
        initialPointField.setBorder(BorderFactory.createLineBorder(COLOR_3));

        JLabel finalDestinationLabel = new JLabel("Final Destination:");
        finalDestinationLabel.setFont(new Font("Arial", Font.BOLD, 20));
        finalDestinationLabel.setForeground(Color.WHITE);
        JTextField finalDestinationField = new JTextField();
        finalDestinationField.setBackground(COLOR_5);
        finalDestinationField.setForeground(Color.WHITE);
        finalDestinationField.setBorder(BorderFactory.createLineBorder(COLOR_3));

        JLabel taxiSelectionLabel = new JLabel("Select Taxi:");
        taxiSelectionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        taxiSelectionLabel.setForeground(Color.WHITE);
        taxiComboBox = new JComboBox<>(new String[]{"Standard Taxi", "Luxury Taxi", "Minibus"});
        taxiComboBox.setBackground(COLOR_5);
        taxiComboBox.setForeground(Color.WHITE);

        inputPanel.add(initialPointLabel);
        inputPanel.add(initialPointField);
        inputPanel.add(finalDestinationLabel);
        inputPanel.add(finalDestinationField);
        inputPanel.add(taxiSelectionLabel);
        inputPanel.add(taxiComboBox);

        gbc.gridx = 0;
        gbc.gridy = 0;
        walletPanel.add(walletLabel, gbc);

        gbc.gridy = 1;
        walletPanel.add(inputPanel, gbc);

        gbc.gridy = 2;
        JPanel walletButtonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        walletButtonPanel.setOpaque(false);
        JButton addMoneyButton = createModernSidebarButton("Add Money", buttonColor);
        JButton withdrawMoneyButton = createModernSidebarButton("Pay", buttonColor);
        JButton showMapButton = createModernSidebarButton("Show Map", buttonColor);

        walletButtonPanel.add(addMoneyButton);
        walletButtonPanel.add(withdrawMoneyButton);
        walletButtonPanel.add(showMapButton);

        walletPanel.add(walletButtonPanel, gbc);

        gbc.gridy = 3;
        JButton calculateButton = createModernSidebarButton("Calculate Fare & Availability", buttonColor);
        walletPanel.add(calculateButton, gbc);

        gbc.gridy = 4;
        JLabel estimatedPriceLabel = new JLabel("Estimated Price: N/A");
        estimatedPriceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        estimatedPriceLabel.setForeground(Color.WHITE);
        walletPanel.add(estimatedPriceLabel, gbc);

        gbc.gridy = 5;
        JLabel taxiAvailabilityLabel = new JLabel("Taxi Availability: N/A");
        taxiAvailabilityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        taxiAvailabilityLabel.setForeground(Color.WHITE);
        walletPanel.add(taxiAvailabilityLabel, gbc);

        contentPanel.add(walletPanel, "walletPanel");
        mainContainer.add(contentPanel, BorderLayout.CENTER);

        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setPreferredSize(new Dimension(300, 300));
        notificationPanel.setBackground(COLOR_1);

        notificationLabel = new JLabel("Notifications", JLabel.LEFT);
        notificationLabel.setFont(new Font("Arial", Font.BOLD, 28));
        notificationLabel.setForeground(Color.WHITE);
        notificationPanel.add(notificationLabel, BorderLayout.NORTH);

        notificationList = new JList<>(notificationsModel);
        notificationList.setBackground(COLOR_5);
        notificationList.setForeground(Color.WHITE);
        notificationList.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane notificationScrollPane = new JScrollPane(notificationList);
        notificationPanel.add(notificationScrollPane, BorderLayout.CENTER);

        JButton hideNotificationsButton = new JButton("Hide Notifications");
        hideNotificationsButton.setFont(new Font("Arial", Font.BOLD, 16));
        hideNotificationsButton.setBackground(COLOR_3);
        hideNotificationsButton.setForeground(Color.WHITE);
        hideNotificationsButton.setFocusPainted(false);
        hideNotificationsButton.addActionListener(e -> {
            notificationPanel.setVisible(false);
            showNotificationsButton.setVisible(true);
        });

        showNotificationsButton = new JButton("Show Notifications");
        showNotificationsButton.setFont(new Font("Arial", Font.BOLD, 16));
        showNotificationsButton.setBackground(COLOR_3);
        showNotificationsButton.setForeground(Color.WHITE);
        showNotificationsButton.setFocusPainted(false);
        showNotificationsButton.setVisible(false);
        showNotificationsButton.addActionListener(e -> {
            notificationPanel.setVisible(true);
            showNotificationsButton.setVisible(false);
        });

        mainContainer.add(showNotificationsButton, BorderLayout.SOUTH);
        notificationPanel.add(hideNotificationsButton, BorderLayout.SOUTH);

        mainContainer.add(notificationPanel, BorderLayout.SOUTH);

        setContentPane(mainContainer);
        setVisible(true);

        bookTaxiButton.addActionListener(e -> {
            String initialPoint = JOptionPane.showInputDialog(this, "Enter initial point:");
            String finalDestination = JOptionPane.showInputDialog(this, "Enter final destination:");
            if (initialPoint != null && finalDestination != null && !initialPoint.isEmpty() && !finalDestination.isEmpty()) {
                double estimatedPrice = calculateEstimatedPrice(initialPoint, finalDestination);
                boolean isTaxiAvailable = checkTaxiAvailability(initialPoint, finalDestination);
                String message = "Estimated Price: R" + String.format("%.2f", estimatedPrice) + "\nTaxi Availability: " + (isTaxiAvailable ? "Available" : "Not Available");
                JOptionPane.showMessageDialog(this, message, "Booking Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both initial and final destinations.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        nearbyTaxiButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "See Nearby Taxis..."));
        taxiRoutesButton.addActionListener(e -> {
            String initialPoint = initialPointField.getText();
            String finalDestination = finalDestinationField.getText();
            if (!initialPoint.isEmpty() && !finalDestination.isEmpty()) {
                try {
                    String googleMapsUrl = "https://www.google.com/maps/dir/?api=1&origin=" + initialPoint + "&destination=" + finalDestination;
                    Desktop.getDesktop().browse(new java.net.URI(googleMapsUrl));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error while opening Google Maps: " + ex.getMessage(), "Map Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both initial and final destinations.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        taxiScheduleButton.addActionListener(e -> showTaxiSchedule());
        themeSwitcher.addActionListener(e -> switchTheme());
        addMoneyButton.addActionListener(e -> adjustWalletBalance(50.0));
        withdrawMoneyButton.addActionListener(e -> adjustWalletBalance(-50.0));
        calculateButton.addActionListener(e -> {
            String initialPoint = initialPointField.getText();
            String finalDestination = finalDestinationField.getText();
            String taxiType = (String) taxiComboBox.getSelectedItem();
            if (!initialPoint.isEmpty() && !finalDestination.isEmpty()) {
                double estimatedPrice = calculateEstimatedPrice(initialPoint, finalDestination);
                boolean isTaxiAvailable = checkTaxiAvailability(initialPoint, finalDestination);
                estimatedPriceLabel.setText("Estimated Price: R" + String.format("%.2f", estimatedPrice) + " (" + taxiType + ")");
                taxiAvailabilityLabel.setText("Taxi Availability: " + (isTaxiAvailable ? "Available" : "Not Available"));
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both initial and final destinations.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        showMapButton.addActionListener(e -> {
            String initialPoint = initialPointField.getText();
            String finalDestination = finalDestinationField.getText();
            if (!initialPoint.isEmpty() && !finalDestination.isEmpty()) {
                try {
                    String googleMapsUrl = "https://www.google.com/maps/dir/?api=1&origin=" + initialPoint + "&destination=" + finalDestination;
                    Desktop.getDesktop().browse(new java.net.URI(googleMapsUrl));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error while opening Google Maps: " + ex.getMessage(), "Map Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both initial and final destinations.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void startClock() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                clockLabel.setText(sdf.format(new Date()));
            }
        }, 0, 1000);
    }

    private JButton createModernSidebarButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showTaxiSchedule() {
        if (taxis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No taxi schedule available.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"Taxi Number", "Available Start Time", "Available End Time"};
        String[][] tableData = new String[taxis.size()][3];
        for (int i = 0; i < taxis.size(); i++) {
            Taxi taxi = taxis.get(i);
            tableData[i][0] = taxi.getTaxiNumber();
            tableData[i][1] = taxi.getStartTime();
            tableData[i][2] = taxi.getEndTime();
        }

        JTable table = new JTable(tableData, columnNames);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(COLOR_3);
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Taxi Schedule", JOptionPane.INFORMATION_MESSAGE);
    }

    private void switchTheme() {
        Color panelBackground;
        Color labelForeground;
        Color buttonBackground;
        Color buttonForeground;

        if (getContentPane().getBackground().equals(COLOR_1)) {
            panelBackground = COLOR_4;
            labelForeground = COLOR_1;
            buttonBackground = COLOR_3;
            buttonForeground = COLOR_1;
        } else {
            panelBackground = COLOR_1;
            labelForeground = Color.WHITE;
            buttonBackground = COLOR_3;
            buttonForeground = Color.WHITE;
        }

        getContentPane().setBackground(panelBackground);
        for (Component component : getContentPane().getComponents()) {
            updateComponentTheme(component, panelBackground, labelForeground, buttonBackground, buttonForeground);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void updateComponentTheme(Component component, Color panelBackground, Color labelForeground, Color buttonBackground, Color buttonForeground) {
        if (component instanceof JPanel) {
            component.setBackground(panelBackground);
            for (Component child : ((JPanel) component).getComponents()) {
                updateComponentTheme(child, panelBackground, labelForeground, buttonBackground, buttonForeground);
            }
        } else if (component instanceof JLabel) {
            component.setForeground(labelForeground);
        } else if (component instanceof JButton) {
            component.setBackground(buttonBackground);
            component.setForeground(buttonForeground);
        } else if (component instanceof JTextField) {
            component.setBackground(COLOR_5);
            component.setForeground(Color.WHITE);
        }
    }

    private void adjustWalletBalance(double amount) {
        walletBalance += amount;
        walletLabel.setText(String.format("Wallet Balance: R%.2f", walletBalance));
        JOptionPane.showMessageDialog(this, String.format("Wallet updated. New Balance: R%.2f", walletBalance), "Wallet Update", JOptionPane.INFORMATION_MESSAGE);
    }

    private void initializeTaxis() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("taxis.ser"))) {
            taxis = (ArrayList<Taxi>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            taxis = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Failed to load taxi data. Starting with an empty list.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveTaxiData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("taxis.ser"))) {
            oos.writeObject(taxis);
            JOptionPane.showMessageDialog(this, "Taxi data saved successfully.", "Save Data", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save taxi data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSampleNewsNotifications() {
        notificationsModel.addElement("Welcome to the Taxi Service App!");
        notificationsModel.addElement("Maintenance on Sunday.");
        notificationsModel.addElement("New routes to Pretoria.");
        notificationsModel.addElement("Holiday discounts available.");
        notificationsModel.addElement("Taxi availability increased on weekends.");
        notificationsModel.addElement("Introducing Luxury Taxi service.");
        notificationsModel.addElement("Safety measures updated.");
        notificationsModel.addElement("New payment options available.");
        notificationsModel.addElement("Service extended to new areas.");
        notificationsModel.addElement("Maintenance scheduled for next week.");
        notificationsModel.addElement("Special fares during rush hours.");
        notificationsModel.addElement("Driver training sessions ongoing.");
        notificationsModel.addElement("Customer satisfaction survey available.");
        notificationsModel.addElement("New partnership with local businesses.");
        notificationsModel.addElement("Emergency contact feature added.");
        notificationsModel.addElement("Real-time location sharing available.");
        notificationsModel.addElement("New app update released.");
        notificationsModel.addElement("Feedback requested for recent rides.");
        notificationsModel.addElement("Referral rewards now available.");
        notificationsModel.addElement("24/7 customer support launched.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu(100.0));
    }
}