import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private HashMap<String, String> users;
    private BufferedImage backgroundImage;

    public LoginForm() {
        setTitle("Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("/home/leonard/Pictures/project-main/pictures/pexels-marieke-schonfeld-1309710-2514035.jpg")); // Update path as needed
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        users = loadUsers();

        JLabel titleLabel = new JLabel("Your Innovative Transport System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        backgroundPanel.add(titleLabel, gbc);

        JLabel emailLabel = new JLabel("Enter your email:");
        emailLabel.setForeground(Color.WHITE);
        emailField = new JTextField(15);
        emailField.setBackground(new Color(200, 200, 200));
        emailField.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        backgroundPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        backgroundPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Enter your password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField(15);
        passwordField.setBackground(new Color(200, 200, 200));
        passwordField.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        backgroundPanel.add(passwordField, gbc);

        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setForeground(Color.WHITE);
        rememberMe.setOpaque(false);
        gbc.gridx = 1;
        gbc.gridy = 3;
        backgroundPanel.add(rememberMe, gbc);

        RoundedButton loginButton = new RoundedButton("Login Now", new Color(70, 130, 180), Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 4;
        loginButton.addActionListener(e -> loginUser());
        backgroundPanel.add(loginButton, gbc);

        RoundedButton signUpButton = new RoundedButton("Sign Up", new Color(60, 179, 113), Color.WHITE);
        gbc.gridy = 5;
        signUpButton.addActionListener(e -> openRegistrationForm());
        backgroundPanel.add(signUpButton, gbc);

        RoundedButton aboutButton = new RoundedButton("About", new Color(255, 165, 0), Color.WHITE);
        gbc.gridy = 6;
        aboutButton.addActionListener(e -> showAboutInfo());
        backgroundPanel.add(aboutButton, gbc);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void loginUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (users.containsKey(email) && users.get(email).equals(password)) {
            showWelcomeSplash();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.");
        }
    }

    private void showWelcomeSplash() {
        JFrame splashFrame = new JFrame();
        splashFrame.setSize(400, 200);
        splashFrame.setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome to Innovative Transport System!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        splashFrame.add(welcomeLabel);

        splashFrame.setVisible(true);

        Timer timer = new Timer(3000, e -> {
            splashFrame.dispose();
            new MainMenu(0.0);
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void openRegistrationForm() {
        new RegistrationForm();
        this.dispose();
    }

    private void showAboutInfo() {
        JOptionPane.showMessageDialog(this, "Innovative Transport System\nVersion 1.0\nDeveloped by Lenard Hlabangwana", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private HashMap<String, String> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.jfile"))) {
            return (HashMap<String, String>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}