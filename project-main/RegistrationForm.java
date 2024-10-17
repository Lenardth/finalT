import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;

public class RegistrationForm extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JLabel profilePictureLabel;
    private String profilePicturePath = null;  // Store path of profile picture
    private HashMap<String, String> users;

    public RegistrationForm() {
        setTitle("Registration");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        users = loadUsers();  // Load users from file

        JPanel registrationPanel = new GradientPanel();  // Use custom JPanel here
        registrationPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name Field
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setForeground(Color.WHITE);
        nameField = new JTextField(15);
        nameField.setBackground(new Color(200, 200, 200));
        nameField.setForeground(Color.BLACK);
        nameField.setToolTipText("Enter your full name");

        gbc.gridx = 0;
        gbc.gridy = 0;
        registrationPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        registrationPanel.add(nameField, gbc);

        // Email Field
        JLabel emailLabel = new JLabel("Enter your email:");
        emailLabel.setForeground(Color.WHITE);
        emailField = new JTextField(15);
        emailField.setBackground(new Color(200, 200, 200));
        emailField.setForeground(Color.BLACK);
        emailField.setToolTipText("Enter a valid email address");

        gbc.gridx = 0;
        gbc.gridy = 1;
        registrationPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        registrationPanel.add(emailField, gbc);

        // Password Field
        JLabel passwordLabel = new JLabel("Create a password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField(15);
        passwordField.setBackground(new Color(200, 200, 200));
        passwordField.setForeground(Color.BLACK);
        passwordField.setToolTipText("Create a strong password");

        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        registrationPanel.add(passwordField, gbc);

        // Confirm Password Field
        JLabel confirmPasswordLabel = new JLabel("Confirm password:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setBackground(new Color(200, 200, 200));
        confirmPasswordField.setForeground(Color.BLACK);
        confirmPasswordField.setToolTipText("Retype your password");

        gbc.gridx = 0;
        gbc.gridy = 3;
        registrationPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        registrationPanel.add(confirmPasswordField, gbc);

        // Register Button with Rounded Corners and Hover Effect
        RoundedButton registerButton = new RoundedButton("Register Now", Color.BLUE, Color.WHITE);
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setToolTipText("Click to complete registration");

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(124, 252, 0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(60, 179, 113));
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 4;
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        registrationPanel.add(registerButton, gbc);

        setContentPane(registrationPanel);
        setVisible(true);
    }

    // Method to handle registration
    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        users.put(email, password);
        saveUsers();
        JOptionPane.showMessageDialog(this, "Registration successful!");
        new LoginForm();
        this.dispose();  // Close registration form
    }

    // Save users to .ser file
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.jfile"))) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load users from .ser file
    private HashMap<String, String> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.jfile"))) {
            return (HashMap<String, String>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();  // No users file yet
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
