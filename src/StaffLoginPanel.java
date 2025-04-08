package com.lancaster.musicapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * StaffLoginPanel - A panel for staff login functionality
 * This panel provides a login interface for staff members to access the booking management system
 */
public class StaffLoginPanel extends JPanel {
    // Reference to main application colors and fonts
    private final Color PRIMARY_COLOR;
    private final Color SECONDARY_COLOR;
    private final Color ACCENT_COLOR;
    private final Color TEXT_COLOR;
    private final Color HIGHLIGHT_COLOR;
    private final Color BORDER_COLOR;
    private final Font HEADER_FONT;
    private final Font LABEL_FONT;
    private final Font BODY_FONT;
    private final Font BUTTON_FONT;
    
    // UI components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;
    
    // Reference to main frame for dialogs and navigation
    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    /**
     * Constructor for StaffLoginPanel
     * 
     * @param mainFrame Reference to the main application frame
     * @param cardLayout Card layout for navigation
     * @param contentPanel Content panel for navigation
     * @param colors Application color scheme
     * @param fonts Application fonts
     */
    public StaffLoginPanel(JFrame mainFrame, CardLayout cardLayout, JPanel contentPanel, 
                          Color[] colors, Font[] fonts) {
        this.mainFrame = mainFrame;
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        
        // Initialize colors and fonts
        this.PRIMARY_COLOR = colors[0];
        this.SECONDARY_COLOR = colors[1];
        this.ACCENT_COLOR = colors[2];
        this.TEXT_COLOR = colors[3];
        this.HIGHLIGHT_COLOR = colors[4];
        this.BORDER_COLOR = colors[5];
        this.HEADER_FONT = fonts[0];
        this.BUTTON_FONT = fonts[1];
        this.LABEL_FONT = fonts[2];
        this.BODY_FONT = fonts[3];
        
        // Set up panel
        setLayout(new BorderLayout());
        setBackground(PRIMARY_COLOR);
        setBorder(new EmptyBorder(50, 50, 50, 50));
        
        // Create components
        createContent();
    }
    
    /**
     * Create the main content of the panel
     */
    private void createContent() {
        // Create title
        JLabel titleLabel = new JLabel("Staff Login");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        // Create login form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), 
                                         SECONDARY_COLOR.getBlue(), 80));
        formPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        formPanel.setMaximumSize(new Dimension(400, 300));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(300, 30));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Login button
        loginButton = createAnimatedButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        
        // Back button
        backButton = createAnimatedButton("Back");
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "welcome"));
        
        // Add components to button panel
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        
        // Add components to form panel
        formPanel.add(usernameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        formPanel.add(buttonPanel);
        
        // Create wrapper panel for centering
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(Box.createVerticalGlue());
        wrapperPanel.add(titleLabel);
        wrapperPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Center the form panel
        JPanel centeringPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centeringPanel.setOpaque(false);
        centeringPanel.add(formPanel);
        wrapperPanel.add(centeringPanel);
        wrapperPanel.add(Box.createVerticalGlue());
        
        // Add wrapper panel to main panel
        add(wrapperPanel, BorderLayout.CENTER);
    }
    
    /**
     * Handle login button click
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // Check credentials
        if ("admin".equals(username) && "123".equals(password)) {
            // Load bookings from database
            BookingManager.getInstance().loadBookingsFromDatabase();
            
            // Show booking management panel
            cardLayout.show(contentPanel, "bookingManagement");
            
            // Clear fields
            usernameField.setText("");
            passwordField.setText("");
        } else {
            // Show error message
            JOptionPane.showMessageDialog(mainFrame,
                "Invalid username or password. Please try again.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Create an animated button with hover and click effects
     */
    private JButton createAnimatedButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(ACCENT_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HIGHLIGHT_COLOR);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setLocation(button.getX(), button.getY() + 2);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setLocation(button.getX(), button.getY() - 2);
            }
        });
        
        return button;
    }
}
