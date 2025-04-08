package com.lancaster.musicapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import javax.swing.Timer;


/**
 * Main application class for Lancaster's Music Hall application.
 * This is a Swing implementation that doesn't require JavaFX.
 */
public class LancasterMusicApp {

    // Main application components
    private JFrame mainFrame;
    private JPanel welcomePanel;
    private JPanel mainPanel;
    private JPanel navigationPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, JPanel> screens;

    // Color scheme (updated per user requirements)
    private static final Color PRIMARY_COLOR = new Color(26, 37, 48); // Dark blue background
    private static final Color SECONDARY_COLOR = new Color(58, 121, 9); // Dark purple accent (changed from green)
    private static final Color ACCENT_COLOR = new Color(101, 201, 21); // Brighter purple for variety
    private static final Color TEXT_COLOR = new Color(255, 255, 255); // White text
    private static final Color HIGHLIGHT_COLOR = new Color(255, 140, 0); // Orange highlight
    private static final Color BORDER_COLOR = new Color(0, 0, 0); // Black border
    private static final Color GRADIENT_START = new Color(26, 37, 48); // Gradient start color
    private static final Color GRADIENT_END = new Color(45, 65, 85); // Gradient end color
    private static final Color PANEL_ACCENT_COLOR = new Color(147, 112, 219); // Light purple for accents
    private static final Color BUTTON_COLOR = new Color(101, 201, 21); // Green button color as requested

    // Fonts
    private static final Font TITLE_FONT = new Font("Dialog", Font.BOLD, 48);
    private static final Font SUBTITLE_FONT = new Font("Dialog", Font.PLAIN, 24);
    private static final Font HEADER_FONT = new Font("Dialog", Font.BOLD, 24);
    private static final Font BODY_FONT = new Font("Dialog", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Dialog", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Dialog", Font.BOLD, 14);

    // Data structures for bookings and inquiries
    private List<Booking> Bookings = new ArrayList<>();
    private List<Enquiry> inquiries = new ArrayList<>();

    /**
     * Constructor for LancasterMusicApp.
     */
    public LancasterMusicApp() {
        initialize();
    }

    /**
     * Initialize the application.
     */
    private void initialize() {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the main frame
        mainFrame = new JFrame("Lancaster's Music Hall");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1024, 768);
        mainFrame.setLocationRelativeTo(null);

        // Create screens
        screens = new HashMap<>();

        // Create welcome panel
        createWelcomePanel();

        // Create main application panel
        createMainPanel();

        // Set the initial content to the welcome panel
        mainFrame.setContentPane(welcomePanel);

        // Display the frame
        mainFrame.setVisible(true);
    }

    /**
     * Create the welcome panel.
     */
    private void createWelcomePanel() {
        welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(PRIMARY_COLOR);

        // Create animated background
        JPanel backgroundPanel = new JPanel() {
            private List<Star> stars = new ArrayList<>();
            private List<MovingCircle> circles = new ArrayList<>();
            private List<FloatingShape> shapes = new ArrayList<>();
            private Timer animationTimer;

            {
                // Initialize stars
                for (int i = 0; i < 100; i++) {
                    stars.add(new Star(
                            (int)(Math.random() * 1024),
                            (int)(Math.random() * 768),
                            (int)(Math.random() * 3) + 1,
                            (float)(Math.random() * 0.8f) + 0.2f
                    ));
                }
                
                // Initialize floating circles (Patreon-inspired)
                for (int i = 0; i < 15; i++) {
                    circles.add(new MovingCircle(
                            (int)(Math.random() * 1024),
                            (int)(Math.random() * 768),
                            (int)(Math.random() * 40) + 10,
                            new Color(
                                (int)(Math.random() * 100) + 155,
                                (int)(Math.random() * 100) + 155,
                                (int)(Math.random() * 100) + 155,
                                (int)(Math.random() * 80) + 20
                            ),
                            (float)(Math.random() * 0.8f - 0.4f),
                            (float)(Math.random() * 0.8f - 0.4f)
                    ));
                }
                
                // Initialize floating shapes (Patreon-inspired)
                String[] shapeTypes = {"circle", "square", "triangle"};
                for (int i = 0; i < 12; i++) {
                    shapes.add(new FloatingShape(
                            (int)(Math.random() * 1024),
                            (int)(Math.random() * 768),
                            (int)(Math.random() * 30) + 15,
                            new Color(
                                (int)(Math.random() * 100) + 155,
                                (int)(Math.random() * 100) + 155,
                                (int)(Math.random() * 100) + 155,
                                (int)(Math.random() * 60) + 40
                            ),
                            shapeTypes[i % 3],
                            (float)(Math.random() * 0.6f - 0.3f),
                            (float)(Math.random() * 0.6f - 0.3f)
                    ));
                }

                // Set up animation timer
                animationTimer = new Timer(50, e -> {
                    for (Star star : stars) {
                        star.twinkle();
                    }
                    for (MovingCircle circle : circles) {
                        circle.move(getWidth(), getHeight());
                    }
                    for (FloatingShape shape : shapes) {
                        shape.move(getWidth(), getHeight());
                    }
                    repaint();
                });
                animationTimer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, GRADIENT_START,
                        getWidth(), getHeight(), GRADIENT_END);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Draw floating circles (behind stars)
                for (MovingCircle circle : circles) {
                    circle.draw(g2d);
                }
                
                // Draw floating shapes
                for (FloatingShape shape : shapes) {
                    shape.draw(g2d);
                }

                // Draw stars (on top)
                for (Star star : stars) {
                    star.draw(g2d);
                }

                g2d.dispose();
            }

            // Inner class for animated stars
            private class Star {
                private int x, y, size;
                private float brightness;
                private float phase = (float)(Math.random() * 2 * Math.PI);
                private float speed = (float)(Math.random() * 0.1f) + 0.05f;

                public Star(int x, int y, int size, float brightness) {
                    this.x = x;
                    this.y = y;
                    this.size = size;
                    this.brightness = brightness;
                }

                public void twinkle() {
                    phase += speed;
                    if (phase > 2 * Math.PI) {
                        phase -= 2 * Math.PI;
                    }
                }

                public void draw(Graphics2D g2d) {
                    float alpha = (float)(0.3f + 0.7f * Math.sin(phase));
                    g2d.setColor(new Color(1f, 1f, 1f, alpha * brightness));
                    g2d.fillOval(x, y, size, size);
                }
            }
            
            // Inner class for moving circles (Patreon-inspired)
            private class MovingCircle {
                private float x, y;
                private int size;
                private Color color;
                private float dx, dy;
                private float phase = (float)(Math.random() * 2 * Math.PI);
                private float pulseSpeed = (float)(Math.random() * 0.05f) + 0.02f;

                public MovingCircle(int x, int y, int size, Color color, float dx, float dy) {
                    this.x = x;
                    this.y = y;
                    this.size = size;
                    this.color = color;
                    this.dx = dx;
                    this.dy = dy;
                }

                public void move(int maxWidth, int maxHeight) {
                    // Update position
                    x += dx;
                    y += dy;
                    
                    // Bounce off edges
                    if (x < 0) {
                        x = 0;
                        dx = -dx;
                    } else if (x > maxWidth) {
                        x = maxWidth;
                        dx = -dx;
                    }
                    
                    if (y < 0) {
                        y = 0;
                        dy = -dy;
                    } else if (y > maxHeight) {
                        y = maxHeight;
                        dy = -dy;
                    }
                    
                    // Update pulse phase
                    phase += pulseSpeed;
                    if (phase > 2 * Math.PI) {
                        phase -= 2 * Math.PI;
                    }
                }

                public void draw(Graphics2D g2d) {
                    // Pulse size based on phase
                    float pulseFactor = 0.8f + 0.2f * (float)Math.sin(phase);
                    int currentSize = (int)(size * pulseFactor);
                    
                    // Draw circle with semi-transparency
                    g2d.setColor(color);
                    g2d.fillOval((int)x - currentSize/2, (int)y - currentSize/2, currentSize, currentSize);
                }
            }
            
            // Inner class for floating shapes (Patreon-inspired)
            private class FloatingShape {
                private float x, y;
                private int size;
                private Color color;
                private String shapeType;
                private float dx, dy;
                private float rotation = 0;
                private float rotationSpeed = (float)(Math.random() * 0.05f) - 0.025f;

                public FloatingShape(int x, int y, int size, Color color, String shapeType, float dx, float dy) {
                    this.x = x;
                    this.y = y;
                    this.size = size;
                    this.color = color;
                    this.shapeType = shapeType;
                    this.dx = dx;
                    this.dy = dy;
                }

                public void move(int maxWidth, int maxHeight) {
                    // Update position
                    x += dx;
                    y += dy;
                    
                    // Wrap around edges
                    if (x < -size) x = maxWidth + size;
                    if (x > maxWidth + size) x = -size;
                    if (y < -size) y = maxHeight + size;
                    if (y > maxHeight + size) y = -size;
                    
                    // Update rotation
                    rotation += rotationSpeed;
                    if (rotation > 2 * Math.PI) {
                        rotation -= 2 * Math.PI;
                    }
                }

                public void draw(Graphics2D g2d) {
                    Graphics2D g2dRotated = (Graphics2D) g2d.create();
                    g2dRotated.setColor(color);
                    
                    // Translate to shape center for rotation
                    g2dRotated.translate(x, y);
                    g2dRotated.rotate(rotation);
                    
                    // Draw the appropriate shape
                    if (shapeType.equals("circle")) {
                        g2dRotated.fillOval(-size/2, -size/2, size, size);
                    } else if (shapeType.equals("square")) {
                        g2dRotated.fillRect(-size/2, -size/2, size, size);
                    } else if (shapeType.equals("triangle")) {
                        int[] xPoints = {0, -size/2, size/2};
                        int[] yPoints = {-size/2, size/2, size/2};
                        g2dRotated.fillPolygon(xPoints, yPoints, 3);
                    }
                    
                    g2dRotated.dispose();
                }
            }
        };
        backgroundPanel.setOpaque(false);

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(100, 100, 100, 100));

        // Create logo
        ImageIcon logoIcon = createLogoIcon(100, 100);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create title
        JLabel titleLabel = new JLabel("Lancaster's Music Hall");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create subtitle
        JLabel subtitleLabel = new JLabel("A Premier Venue for Music and Events");
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create enter button (enlarged as requested)
        JButton enterButton = createAnimatedButton("Enter");
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterButton.setMaximumSize(new Dimension(300, 80));
        enterButton.setPreferredSize(new Dimension(300, 80));
        enterButton.setFont(new Font(BUTTON_FONT.getName(), Font.BOLD, 24)); // Larger font
        enterButton.addActionListener(e -> {
            mainFrame.setContentPane(mainPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });
        
        // Create staff login button
        JButton staffLoginButton = createAnimatedButton("Staff Login");
        staffLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        staffLoginButton.setMaximumSize(new Dimension(300, 60));
        staffLoginButton.setPreferredSize(new Dimension(300, 60));
        staffLoginButton.setFont(new Font(BUTTON_FONT.getName(), Font.BOLD, 18));
        staffLoginButton.addActionListener(e -> {
            cardLayout.show(this.contentPanel, "staffLogin");
            mainFrame.setContentPane(mainPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // Add components to content panel with spacing
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(logoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(enterButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(staffLoginButton);
        contentPanel.add(Box.createVerticalGlue());

        // Add panels to welcome panel
        welcomePanel.add(backgroundPanel, BorderLayout.CENTER);
        welcomePanel.add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Create the main application panel.
     */
    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create navigation panel
        navigationPanel = createNavigationPanel();
        mainPanel.add(navigationPanel, BorderLayout.WEST);

        // Create content panel with card layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(PRIMARY_COLOR);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Create footer panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Create screens
        createScreens();

        // Show home screen initially
        cardLayout.show(contentPanel, "home");
    }

    /**
     * Create the header panel.
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(SECONDARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Create logo
        ImageIcon logoIcon = createLogoIcon(40, 40);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBorder(new EmptyBorder(0, 0, 0, 10));

        // Create title
        JLabel titleLabel = new JLabel("Lancaster's Music Hall");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);

        // Add components to header
        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    /**
     * Create the navigation panel.
     */
    private JPanel createNavigationPanel() {

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(PRIMARY_COLOR);
        navPanel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 0, 2, BORDER_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));
        navPanel.setPreferredSize(new Dimension(200, 0));
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(createPatreonsNavButton());
        navPanel.add(Box.createVerticalGlue());

        // Create navigation buttons
        JButton homeButton = createNavButton("Home");
        JButton venueButton = createNavButton("Venue Info");
        JButton largeBookingButton = createNavButton("Large Bookings");
        JButton contactButton = createNavButton("Contact");
        JButton marketingButton = createNavButton("Newsletter");
        JButton staffButton = createNavButton("Staff Portal");

        // Add action listeners
        homeButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "home");
            updateNavButtons(homeButton);
        });

        venueButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "venue");
            updateNavButtons(venueButton);
        });

        largeBookingButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "largeBooking");
            updateNavButtons(largeBookingButton);
        });

        contactButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "contact");
            updateNavButtons(contactButton);
        });

        marketingButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "marketing");
            updateNavButtons(marketingButton);
        });
        
        staffButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "staffLogin");
            updateNavButtons(staffButton);
        });

        // Set initial selection
        homeButton.setBackground(HIGHLIGHT_COLOR);
        homeButton.setForeground(Color.BLACK);

        // Add buttons to panel with spacing
        navPanel.add(homeButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(venueButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(largeBookingButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(contactButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(marketingButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(staffButton);
        navPanel.add(Box.createVerticalGlue());

        return navPanel;
    }

    /**
     * Create a navigation button.
     */
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, 40));
        button.setPreferredSize(new Dimension(160, 40));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.getBackground() != HIGHLIGHT_COLOR) {
                    button.setBackground(ACCENT_COLOR);
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.getBackground() != HIGHLIGHT_COLOR) {
                    button.setBackground(PRIMARY_COLOR);
                    button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        return button;
    }

    /**
     * Update the navigation buttons to show the selected screen.
     */
    private void updateNavButtons(JButton selectedButton) {
        // Reset all buttons
        for (Component comp : navigationPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setBackground(PRIMARY_COLOR);
                button.setForeground(TEXT_COLOR);
            }
        }

        // Highlight selected button
        selectedButton.setBackground(HIGHLIGHT_COLOR);
        selectedButton.setForeground(Color.BLACK);
    }

    /**
     * Create the footer panel.
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(SECONDARY_COLOR);
        footerPanel.setPreferredSize(new Dimension(0, 30));
        footerPanel.setBorder(new EmptyBorder(5, 20, 5, 20));

        // Create copyright text
        JLabel copyrightLabel = new JLabel("© 2023 Lancaster's Music Hall. All rights reserved.");
        copyrightLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        copyrightLabel.setForeground(TEXT_COLOR);

        // Add components to footer
        footerPanel.add(copyrightLabel, BorderLayout.WEST);

        return footerPanel;
    }

    /**
     * Create the screens for the application.
     */
    private void createScreens() {
        // Create home screen
        JPanel homeScreen = createHomeScreen();
        screens.put("home", homeScreen);
        contentPanel.add(homeScreen, "home");

        // Create venue info screen
        JPanel venueScreen = createVenueScreen();
        screens.put("venue", venueScreen);
        contentPanel.add(venueScreen, "venue");

        // Create large booking screen with modern calendar
        // Create color and font arrays to pass to ModernLargeBookingPanel
        Color[] colors = {PRIMARY_COLOR, SECONDARY_COLOR, ACCENT_COLOR, TEXT_COLOR, HIGHLIGHT_COLOR, BORDER_COLOR};
        Font[] fonts = {HEADER_FONT, BUTTON_FONT, LABEL_FONT, BODY_FONT};
        
        // Create the new modern large booking panel
        JPanel largeBookingScreen = new ModernLargeBookingPanel(mainFrame, Collections.singletonList(Bookings), Collections.singletonList(inquiries), colors, fonts);
        screens.put("largeBooking", largeBookingScreen);
        contentPanel.add(largeBookingScreen, "largeBooking");

        // Create contact screen
        JPanel contactScreen = createContactScreen();
        screens.put("contact", contactScreen);
        contentPanel.add(contactScreen, "contact");

        // Create marketing screen
        JPanel marketingScreen = createMarketingScreen();
        screens.put("marketing", marketingScreen);
        contentPanel.add(marketingScreen, "marketing");
        
        // Add the Patreons login screen to your screens map
        JPanel patreonsPanel = new PatreonsLoginPanel();
        screens.put("patreons", patreonsPanel);
        contentPanel.add(patreonsPanel, "patreons");
        
        // Add staff login screen
        JPanel staffLoginScreen = new StaffLoginPanel(mainFrame, cardLayout, contentPanel, colors, fonts);
        screens.put("staffLogin", staffLoginScreen);
        contentPanel.add(staffLoginScreen, "staffLogin");
        
        // Add booking management screen
        JPanel bookingManagementScreen = new BookingManagementPanel(mainFrame, cardLayout, contentPanel, colors, fonts);
        screens.put("bookingManagement", bookingManagementScreen);
        contentPanel.add(bookingManagementScreen, "bookingManagement");
    }

    /**
     * Create the home screen.
     */
    private JPanel createHomeScreen() {
        JPanel panel = new GradientPanel(GRADIENT_START, GRADIENT_END, GradientPanel.DIAGONAL);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create welcome label
        JLabel welcomeLabel = createFancyLabel("Welcome to Lancaster's Music Hall", HEADER_FONT, TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create description
        JTextArea descriptionArea = new JTextArea(
            "Lancaster's Music Hall is a premier venue for music and events in the heart of the city. " +
            "We host a variety of performances, from classical concerts to contemporary shows, " +
            "providing an unforgettable experience for all music lovers."
        );
        descriptionArea.setFont(BODY_FONT);
        descriptionArea.setForeground(TEXT_COLOR);
        descriptionArea.setBackground(new Color(0, 0, 0, 0));
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionArea.setMaximumSize(new Dimension(600, 100));

        // Create featured events panel
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));
        eventsPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        eventsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        eventsPanel.setMaximumSize(new Dimension(800, 400));

        // Create events title
        JLabel eventsTitle = new JLabel("Featured Events");
        eventsTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        eventsTitle.setForeground(TEXT_COLOR);
        eventsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create event items
        JPanel event1 = createEventItem(
            "Summer Concert Series",
            "June 15 - August 30, 2023",
            "Join us for our annual Summer Concert Series featuring a diverse lineup of artists and genres."
        );
        
        JPanel event2 = createEventItem(
            "Classical Music Weekend",
            "July 8-10, 2023",
            "Experience the beauty of classical music with performances by renowned orchestras and soloists."
        );
        
        JPanel event3 = createEventItem(
            "Jazz Night",
            "Every Thursday, 8:00 PM",
            "Enjoy the smooth sounds of jazz with our weekly Jazz Night featuring local and visiting jazz musicians."
        );

        // Add components to events panel
        eventsPanel.add(eventsTitle);
        eventsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        eventsPanel.add(event1);
        eventsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        eventsPanel.add(event2);
        eventsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        eventsPanel.add(event3);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create buttons
        JButton bookingsButton = createAnimatedButton("Bookings");
        JButton venueInfoButton = createAnimatedButton("Venue Info");
        JButton contactButton = createAnimatedButton("Contact Us");

        // Add action listeners
        bookingsButton.addActionListener(e -> cardLayout.show(contentPanel, "largeBooking"));
        venueInfoButton.addActionListener(e -> cardLayout.show(contentPanel, "venue"));
        contactButton.addActionListener(e -> cardLayout.show(contentPanel, "contact"));

        // Add buttons to panel
        buttonsPanel.add(bookingsButton);
        buttonsPanel.add(venueInfoButton);
        buttonsPanel.add(contactButton);

        // Add components to main panel
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(descriptionArea);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(eventsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(buttonsPanel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Create an event item panel.
     */
    private JPanel createEventItem(String title, String date, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 150));
        panel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create date
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        dateLabel.setForeground(TEXT_COLOR);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create description
        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(BODY_FONT);
        descriptionArea.setForeground(TEXT_COLOR);
        descriptionArea.setBackground(new Color(0, 0, 0, 0));
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add components to panel
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(dateLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(descriptionArea);

        return panel;
    }

    /**
     * Create the venue info screen.
     */
    private JPanel createVenueScreen() {
        JPanel panel = new GradientPanel(GRADIENT_START, GRADIENT_END, GradientPanel.DIAGONAL);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create title
        JLabel titleLabel = createFancyLabel("Venue Information", HEADER_FONT, TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create venue carousel
        VenueCarouselPanel venueCarousel = new VenueCarouselPanel();
        venueCarousel.setAlignmentX(Component.CENTER_ALIGNMENT);
        venueCarousel.setMaximumSize(new Dimension(800, 300));

        // Create venue details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));
        detailsPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.setMaximumSize(new Dimension(800, 300));

        // Create details title
        JLabel detailsTitle = new JLabel("About Our Venue");
        detailsTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        detailsTitle.setForeground(TEXT_COLOR);
        detailsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create details description
        JTextArea detailsDescription = new JTextArea(
            "Lancaster's Music Hall is a state-of-the-art venue designed to provide the best acoustic experience " +
            "for all types of musical performances. Our main hall can accommodate up to 500 guests with " +
            "comfortable seating and excellent visibility from all angles.\n\n" +
            "The venue is equipped with professional sound and lighting systems, as well as a spacious stage " +
            "that can accommodate large ensembles. We also offer smaller rooms for intimate performances " +
            "and private events."
        );
        detailsDescription.setFont(BODY_FONT);
        detailsDescription.setForeground(TEXT_COLOR);
        detailsDescription.setBackground(new Color(0, 0, 0, 0));
        detailsDescription.setWrapStyleWord(true);
        detailsDescription.setLineWrap(true);
        detailsDescription.setEditable(false);
        detailsDescription.setOpaque(false);
        detailsDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create facilities section
        JLabel facilitiesTitle = new JLabel("Facilities");
        facilitiesTitle.setFont(new Font("Dialog", Font.BOLD, 16));
        facilitiesTitle.setForeground(TEXT_COLOR);
        facilitiesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create facilities list
        String[] facilities = {
            "Main hall with seating for 500 guests",
            "Professional sound and lighting systems",
            "Spacious stage suitable for large ensembles",
            "Smaller rooms for intimate performances",
            "Bar and refreshment areas",
            "Accessible facilities for all guests",
            "Parking available nearby"
        };

        JPanel facilitiesList = new JPanel();
        facilitiesList.setLayout(new BoxLayout(facilitiesList, BoxLayout.Y_AXIS));
        facilitiesList.setOpaque(false);
        facilitiesList.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String facility : facilities) {
            JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
            itemPanel.setOpaque(false);
            itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            itemPanel.setMaximumSize(new Dimension(700, 30));

            // Create bullet point
            JLabel bulletLabel = new JLabel("•");
            bulletLabel.setFont(BODY_FONT);
            bulletLabel.setForeground(TEXT_COLOR);

            // Create item text
            JLabel itemLabel = new JLabel(facility);
            itemLabel.setFont(BODY_FONT);
            itemLabel.setForeground(TEXT_COLOR);

            // Add components to item panel
            itemPanel.add(bulletLabel, BorderLayout.WEST);
            itemPanel.add(itemLabel, BorderLayout.CENTER);

            // Add item panel to facilities list
            facilitiesList.add(itemPanel);
            facilitiesList.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        // Add components to details panel
        detailsPanel.add(detailsTitle);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        detailsPanel.add(detailsDescription);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(facilitiesTitle);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(facilitiesList);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create booking button
        JButton bookingButton = createAnimatedButton("Make a Booking");
        bookingButton.addActionListener(e -> cardLayout.show(contentPanel, "largeBooking"));

        // Add button to panel
        buttonPanel.add(bookingButton);

        // Add components to main panel
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(venueCarousel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(detailsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Create the contact screen.
     */
    private JPanel createContactScreen() {
        JPanel panel = new GradientPanel(GRADIENT_START, GRADIENT_END, GradientPanel.DIAGONAL);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create title
        JLabel titleLabel = createFancyLabel("Contact Us", HEADER_FONT, TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create main content panel
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.X_AXIS));
        mainContent.setOpaque(false);
        mainContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.setMaximumSize(new Dimension(800, 400));

        // Create left panel for contact info
        JPanel contactInfoPanel = new JPanel();
        contactInfoPanel.setLayout(new BoxLayout(contactInfoPanel, BoxLayout.Y_AXIS));
        contactInfoPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));
        contactInfoPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        contactInfoPanel.setPreferredSize(new Dimension(350, 350));

        // Create contact info title
        JLabel contactInfoTitle = new JLabel("Contact Information");
        contactInfoTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        contactInfoTitle.setForeground(TEXT_COLOR);
        contactInfoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create contact info items
        JPanel addressInfo = createContactInfoItem("Address", "123 Music Street, Lancaster, LA1 1AA");
        JPanel phoneInfo = createContactInfoItem("Phone", "+44 (0)1234 567890");
        JPanel emailInfo = createContactInfoItem("Email", "info@lancastersmusichall.com");
        JPanel hoursInfo = createContactInfoItem("Hours", "Mon-Fri: 9am-5pm, Sat: 10am-4pm, Sun: Closed");

        // Create map placeholder
        JPanel mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw map background
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Draw grid lines
                g2d.setColor(new Color(200, 200, 200));
                for (int i = 0; i < getWidth(); i += 20) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight(); i += 20) {
                    g2d.drawLine(0, i, getWidth(), i);
                }

                // Draw roads
                g2d.setColor(new Color(180, 180, 180));
                g2d.fillRect(50, getHeight() / 2 - 10, getWidth() - 100, 20);
                g2d.fillRect(getWidth() / 2 - 10, 50, 20, getHeight() - 100);

                // Draw venue location
                g2d.setColor(HIGHLIGHT_COLOR);
                g2d.fillOval(getWidth() / 2 - 15, getHeight() / 2 - 15, 30, 30);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(getWidth() / 2 - 15, getHeight() / 2 - 15, 30, 30);

                // Draw "You are here" text
                g2d.setFont(new Font("Dialog", Font.BOLD, 12));
                g2d.drawString("Lancaster's Music Hall", getWidth() / 2 - 70, getHeight() / 2 + 40);

                g2d.dispose();
            }
        };
        mapPanel.setPreferredSize(new Dimension(300, 200));
        mapPanel.setBorder(new LineBorder(BORDER_COLOR, 1));
        mapPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add components to contact info panel
        contactInfoPanel.add(contactInfoTitle);
        contactInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contactInfoPanel.add(addressInfo);
        contactInfoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contactInfoPanel.add(phoneInfo);
        contactInfoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contactInfoPanel.add(emailInfo);
        contactInfoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contactInfoPanel.add(hoursInfo);
        contactInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contactInfoPanel.add(mapPanel);

        // Create right panel for contact form
        JPanel contactFormPanel = new JPanel();
        contactFormPanel.setLayout(new BoxLayout(contactFormPanel, BoxLayout.Y_AXIS));
        contactFormPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));
        contactFormPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        contactFormPanel.setPreferredSize(new Dimension(350, 350));

        // Create form title
        JLabel formTitle = new JLabel("Send Us a Message");
        formTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        formTitle.setForeground(TEXT_COLOR);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create form components
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setFont(LABEL_FONT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField nameField = new JTextField(20);
        nameField.setMaximumSize(new Dimension(300, 30));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(TEXT_COLOR);
        emailLabel.setFont(LABEL_FONT);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(300, 30));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setForeground(TEXT_COLOR);
        subjectLabel.setFont(LABEL_FONT);
        subjectLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField subjectField = new JTextField(20);
        subjectField.setMaximumSize(new Dimension(300, 30));
        subjectField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setForeground(TEXT_COLOR);
        messageLabel.setFont(LABEL_FONT);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextArea messageArea = new JTextArea(5, 20);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setMaximumSize(new Dimension(300, 100));
        messageScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton sendButton = createAnimatedButton("Send Message");
        sendButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        sendButton.setMaximumSize(new Dimension(150, 40));

        // Add action listener to send button
        sendButton.addActionListener(e -> {
            if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || messageArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame,
                    "Please fill in all required fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                    "Thank you for your message! We will get back to you soon.",
                    "Message Sent",
                    JOptionPane.INFORMATION_MESSAGE);

                // Clear form fields
                nameField.setText("");
                emailField.setText("");
                subjectField.setText("");
                messageArea.setText("");
            }
        });

        // Add components to form panel
        contactFormPanel.add(formTitle);
        contactFormPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contactFormPanel.add(nameLabel);
        contactFormPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contactFormPanel.add(nameField);
        contactFormPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contactFormPanel.add(emailLabel);
        contactFormPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contactFormPanel.add(emailField);
        contactFormPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contactFormPanel.add(subjectLabel);
        contactFormPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contactFormPanel.add(subjectField);
        contactFormPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contactFormPanel.add(messageLabel);
        contactFormPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contactFormPanel.add(messageScroll);
        contactFormPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contactFormPanel.add(sendButton);

        // Add panels to main content
        mainContent.add(contactInfoPanel);
        mainContent.add(Box.createRigidArea(new Dimension(20, 0)));
        mainContent.add(contactFormPanel);

        // Add components to main panel
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(mainContent);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Create a contact info item with icon.
     */
    private JPanel createContactInfoItem(String label, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create icon panel
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circle background
                g2d.setColor(SECONDARY_COLOR);
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Draw icon symbol based on label
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(new Font("Dialog", Font.BOLD, 12));
                FontMetrics fm = g2d.getFontMetrics();
                String symbol = "";

                switch(label) {
                    case "Address": symbol = "A"; break;
                    case "Phone": symbol = "P"; break;
                    case "Email": symbol = "E"; break;
                    case "Hours": symbol = "H"; break;
                    default: symbol = "?";
                }

                int textWidth = fm.stringWidth(symbol);
                int textHeight = fm.getHeight();
                g2d.drawString(symbol, (getWidth() - textWidth) / 2,
                               (getHeight() + textHeight / 2) / 2);

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(30, 30);
            }
        };

        // Create text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Dialog", Font.BOLD, 14));
        labelText.setForeground(TEXT_COLOR);
        labelText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueText = new JLabel(value);
        valueText.setFont(BODY_FONT);
        valueText.setForeground(TEXT_COLOR);
        valueText.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(labelText);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(valueText);

        // Add components to panel
        panel.add(iconPanel);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(textPanel);

        return panel;
    }

    /**
     * Create the marketing subscription screen.
     */
    private JPanel createMarketingScreen() {
        JPanel panel = new GradientPanel(GRADIENT_START, GRADIENT_END, GradientPanel.DIAGONAL);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create title
        JLabel titleLabel = createFancyLabel("Subscribe to Our Newsletter", HEADER_FONT, TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create description
        JTextArea descriptionArea = new JTextArea(
            "Stay up to date with the latest events, promotions, and news from Lancaster's Music Hall. " +
            "Subscribe to our newsletter to receive updates directly to your inbox."
        );
        descriptionArea.setFont(BODY_FONT);
        descriptionArea.setForeground(TEXT_COLOR);
        descriptionArea.setBackground(new Color(0, 0, 0, 0));
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionArea.setMaximumSize(new Dimension(600, 100));

        // Create main content panel
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.X_AXIS));
        mainContent.setOpaque(false);
        mainContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.setMaximumSize(new Dimension(800, 400));

        // Create left panel for form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));

        // Create form components
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setFont(LABEL_FONT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField nameField = new JTextField(20);
        nameField.setMaximumSize(new Dimension(300, 30));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(TEXT_COLOR);
        emailLabel.setFont(LABEL_FONT);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(300, 30));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create checkboxes for interests
        JLabel interestsLabel = new JLabel("Interests:");
        interestsLabel.setForeground(TEXT_COLOR);
        interestsLabel.setFont(LABEL_FONT);
        interestsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox concertsCheckbox = new JCheckBox("Concerts");
        concertsCheckbox.setForeground(TEXT_COLOR);
        concertsCheckbox.setFont(BODY_FONT);
        concertsCheckbox.setOpaque(false);
        concertsCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox eventsCheckbox = new JCheckBox("Special Events");
        eventsCheckbox.setForeground(TEXT_COLOR);
        eventsCheckbox.setFont(BODY_FONT);
        eventsCheckbox.setOpaque(false);
        eventsCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox promotionsCheckbox = new JCheckBox("Promotions");
        promotionsCheckbox.setForeground(TEXT_COLOR);
        promotionsCheckbox.setFont(BODY_FONT);
        promotionsCheckbox.setOpaque(false);
        promotionsCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox venueCheckbox = new JCheckBox("Venue Updates");
        venueCheckbox.setForeground(TEXT_COLOR);
        venueCheckbox.setFont(BODY_FONT);
        venueCheckbox.setOpaque(false);
        venueCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton subscribeButton = createAnimatedButton("Subscribe");
        subscribeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        subscribeButton.setMaximumSize(new Dimension(150, 40));

        // Add action listener to subscribe button
        subscribeButton.addActionListener(e -> {
            if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame,
                    "Please enter your name and email address.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                    "Thank you for subscribing to our newsletter!",
                    "Subscription Successful",
                    JOptionPane.INFORMATION_MESSAGE);

                // Clear form fields
                nameField.setText("");
                emailField.setText("");
                concertsCheckbox.setSelected(false);
                eventsCheckbox.setSelected(false);
                promotionsCheckbox.setSelected(false);
                venueCheckbox.setSelected(false);
            }
        });

        // Add components to form panel
        formPanel.add(nameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(nameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(emailLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(interestsLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(concertsCheckbox);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(eventsCheckbox);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(promotionsCheckbox);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(venueCheckbox);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(subscribeButton);

        // Create right panel for newsletter preview
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.setOpaque(false);
        previewPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        previewPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));

        // Create preview title
        JLabel previewTitle = new JLabel("Newsletter Preview");
        previewTitle.setFont(new Font("Dialog", Font.BOLD, 18));
        previewTitle.setForeground(TEXT_COLOR);
        previewTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create newsletter preview
        JPanel newsletterPreview = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Draw newsletter background
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Draw header
                g2d.setColor(SECONDARY_COLOR);
                g2d.fillRect(0, 0, getWidth(), 50);

                // Draw header text
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(new Font("Dialog", Font.BOLD, 16));
                g2d.drawString("Lancaster's Music Hall Newsletter", 10, 30);

                // Draw content sections
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Dialog", Font.BOLD, 14));
                g2d.drawString("Upcoming Events", 10, 70);

                g2d.setFont(new Font("Dialog", Font.PLAIN, 12));
                g2d.drawString("• Summer Concert Series - Starting June 15", 20, 90);
                g2d.drawString("• Jazz Night - Every Thursday", 20, 110);
                g2d.drawString("• Classical Music Weekend - July 8-10", 20, 130);

                g2d.setFont(new Font("Dialog", Font.BOLD, 14));
                g2d.drawString("Special Promotions", 10, 160);

                g2d.setFont(new Font("Dialog", Font.PLAIN, 12));
                g2d.drawString("• Early Bird Tickets - 15% off when purchased 30 days in advance", 20, 180);
                g2d.drawString("• Group Discounts - 10% off for groups of 10 or more", 20, 200);

                // Draw footer
                g2d.setColor(SECONDARY_COLOR);
                g2d.fillRect(0, getHeight() - 30, getWidth(), 30);

                g2d.setColor(TEXT_COLOR);
                g2d.setFont(new Font("Dialog", Font.PLAIN, 10));
                g2d.drawString("© 2023 Lancaster's Music Hall. All rights reserved.", 10, getHeight() - 10);

                g2d.dispose();
            }
        };
        newsletterPreview.setPreferredSize(new Dimension(300, 300));
        newsletterPreview.setMaximumSize(new Dimension(300, 300));
        newsletterPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
        newsletterPreview.setBorder(new LineBorder(BORDER_COLOR, 1));

        // Add components to preview panel
        previewPanel.add(previewTitle);
        previewPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        previewPanel.add(newsletterPreview);

        // Add panels to main content
        mainContent.add(formPanel);
        mainContent.add(Box.createRigidArea(new Dimension(20, 0)));
        mainContent.add(previewPanel);

        // Add components to main panel
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(descriptionArea);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(mainContent);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Create an animated button with hover and click effects.
     */
    private JButton createAnimatedButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(BUTTON_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Slightly brighter version of button color for hover
                Color brighterColor = new Color(
                    Math.min(BUTTON_COLOR.getRed() + 20, 255),
                    Math.min(BUTTON_COLOR.getGreen() + 20, 255),
                    Math.min(BUTTON_COLOR.getBlue() + 20, 255)
                );
                button.setBackground(brighterColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
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

    /**
     * Create a fancy label with shadow effect.
     */
    private JLabel createFancyLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw text shadow
                g2d.setFont(getFont());
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(getText(), 2, getHeight() - 4);

                // Draw text
                g2d.setColor(getForeground());
                g2d.drawString(getText(), 0, getHeight() - 6);

                g2d.dispose();
            }
        };
        label.setFont(font);
        label.setForeground(color);

        return label;
    }

    /**
     * Create a logo icon with the specified dimensions.
     *
     * @param width The width of the logo
     * @param height The height of the logo
     * @return The logo icon
     */
    private ImageIcon createLogoIcon(int width, int height) {
        // Create a placeholder logo (purple circle with "LMH" text)
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw circle background
        g2d.setColor(SECONDARY_COLOR);
        g2d.fillOval(0, 0, width, height);

        // Draw border
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(0, 0, width - 1, height - 1);

        // Draw text
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Dialog", Font.BOLD, width / 3));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "LMH";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, (width - textWidth) / 2, height / 2 + textHeight / 4);

        g2d.dispose();

        return new ImageIcon(image);
    }

    /**
     * Create a gradient panel.
     */
    private class GradientPanel extends JPanel {
        public static final int HORIZONTAL = 0;
        public static final int VERTICAL = 1;
        public static final int DIAGONAL = 2;

        private Color startColor;
        private Color endColor;
        private int direction;

        public GradientPanel(Color startColor, Color endColor, int direction) {
            this.startColor = startColor;
            this.endColor = endColor;
            this.direction = direction;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gradient;
            switch (direction) {
                case HORIZONTAL:
                    gradient = new GradientPaint(0, 0, startColor, getWidth(), 0, endColor);
                    break;
                case VERTICAL:
                    gradient = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
                    break;
                case DIAGONAL:
                default:
                    gradient = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
                    break;
            }

            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.dispose();
        }
    }

    /**
     * Create a Patreons login button for the navigation panel.
     */
    private JButton createPatreonsNavButton() {
        JButton button = new JButton("Patreons Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw button background with gradient
                GradientPaint gradient = new GradientPaint(
                        0, 0, PANEL_ACCENT_COLOR,
                        getWidth(), getHeight(), new Color(PANEL_ACCENT_COLOR.getRed(), PANEL_ACCENT_COLOR.getGreen(), PANEL_ACCENT_COLOR.getBlue(), 150)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Draw border
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                // Draw text
                g2d.setFont(getFont());
                g2d.setColor(TEXT_COLOR);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                g2d.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight / 2) / 2);

                g2d.dispose();
            }
        };
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, 40));
        button.setPreferredSize(new Dimension(160, 40));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // Add action listener
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, "patreons");
        });

        return button;
    }

    /**
     * Inner class for Patreons login panel.
     */
    private class PatreonsLoginPanel extends JPanel {
        private JTextField usernameField;
        private JPasswordField passwordField;

        public PatreonsLoginPanel() {
            setLayout(new BorderLayout());
            setBackground(PRIMARY_COLOR);
            setBorder(new EmptyBorder(50, 50, 50, 50));

            // Create title
            JLabel titleLabel = new JLabel("Patreons Login");
            titleLabel.setFont(HEADER_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

            // Create login form panel
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));
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
            JButton loginButton = createAnimatedButton("Login");
            loginButton.addActionListener(e -> {
                // In a real application, this would validate credentials
                JOptionPane.showMessageDialog(mainFrame,
                    "Patreons login functionality is not implemented in this demo.",
                    "Login",
                    JOptionPane.INFORMATION_MESSAGE);
            });

            // Back button
            JButton backButton = createAnimatedButton("Back");
            backButton.addActionListener(e -> cardLayout.show(contentPanel, "home"));

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
    }

    /**
     * Add a booking to the list.
     * 
     * @param booking The booking to add
     */
    public void addBooking(Booking booking) {
        Bookings.add(booking);
        
        // Also add to the BookingManager for staff management
        BookingManager.getInstance().addBooking(
            new BookingManager.BookingEntry(
                booking.getBookingID(),
                booking.getCustomerBooked(),
                booking.getPaymentType(),
                booking.getTicketsBooked(),
                booking.getBookingDate(),
                booking.getRowsHeld(),
                booking.getTotalCost(),
                "Pending"
            )
        );
    }

    /**
     * Data class for storing booking information.
     */
    private class Booking {
        private String bookingID;
        private String customerBooked;
        private String paymentType;
        private String ticketsBooked;
        private Date bookingDate;
        private String rowsHeld;
        private int totalCost;
        public Booking(String bookingID, String customerBooked, String paymentType, String ticketsBooked, Date bookingDate, String rowsHeld, int totalCost) {
            this.bookingID = bookingID;
            this.customerBooked = customerBooked;
            this.paymentType = paymentType;
            this.ticketsBooked = ticketsBooked;
            this.bookingDate = bookingDate;
            this.rowsHeld = rowsHeld;
            this.totalCost = totalCost;
        }

        // Getters and setters
        private String getBookingID() { return bookingID; }
        public void setBookingID(String bookingID) { this.bookingID = bookingID; }

        public String getCustomerBooked() { return customerBooked; }
        public void setCustomerBooked(String customerBooked) { this.customerBooked = customerBooked; }

        public String getPaymentType() { return paymentType; }
        public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

        public String getTicketsBooked() { return ticketsBooked; }
        public void setTicketsBooked(String ticketsBooked) { this.ticketsBooked = ticketsBooked; }

        private Date getBookingDate() { return bookingDate; }
        public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

        public String getRowsHeld() { return rowsHeld; }
        public void setRowsHeld(String rowsHeld) { this.rowsHeld = rowsHeld; }

        public int getTotalCost() { return totalCost; }
        public void setTotalCost(int totalCost) { this.totalCost = totalCost; }

        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d,     yyyy");
            return "Booking: " + customerBooked + " (" + paymentType + "), " +
                   dateFormat.format(rowsHeld) + ", " + totalCost + " attendees";
        }

        //Set bookings from database
        public boolean setBooking(String bookingID, String customerBooked, String paymentType,
                                  String ticketsBooked, Date bookingDate, String rowsHeld,
                                  int totalCost) {
            boolean isInserted = false;

            // SQL query to insert a new booking into the database
            String query = "INSERT INTO BOOKINGS (BOOKING_ID, CUSTOMER_BOOKED, PAYMENT_TYPE, " +
                    "TICKETS_BOOKED, BOOKING_DATE, ROWSHELD, TOTAL_COST, STATUS) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = Database.connection();  // Assuming Database.connection() provides a valid DB connection
                 PreparedStatement ps = conn.prepareStatement(query)) {

                // Set the parameters for the PreparedStatement
                ps.setString(1, bookingID);
                ps.setString(2, customerBooked);
                ps.setString(3, paymentType);
                ps.setString(4, ticketsBooked);
                ps.setDate(5, new java.sql.Date(bookingDate.getTime()));  // Convert to java.sql.Date
                ps.setString(6, rowsHeld);
                ps.setInt(7, totalCost);
                ps.setString(8, "Pending");  // Default status is Pending

                // Execute the update (which will insert the booking)
                int rowsAffected = ps.executeUpdate();

                // Check if the booking was successfully inserted
                if (rowsAffected > 0) {
                    isInserted = true;
                }

            } catch (SQLException e) {
                e.printStackTrace();  // Handle exceptions properly in production
            }

            return isInserted;  // Returns true if the booking was added successfully, false otherwise
        }

        //Load bookings from database
        public Booking loadSpecificBooking(String bookingID) {
            Booking booking = null;
            String query = "SELECT * FROM BOOKINGS WHERE BOOKING_ID = ?";

            try (Connection conn = Database.connection();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, bookingID);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    booking = new Booking(
                        rs.getString("BOOKING_ID"),
                        rs.getString("CUSTOMER_BOOKED"),
                        rs.getString("PAYMENT_TYPE"),
                        rs.getString("TICKETS_BOOKED"),
                        rs.getDate("BOOKING_DATE"),
                        rs.getString("ROWSHELD"),
                        rs.getInt("TOTAL_COST")
                    );
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return booking;
        }
    }

    /**
     * Data class for storing enquiry information.
     */
    private class Enquiry {
        private String enquiryID;
        private String customerName;
        private String contactEmail;
        private String contactPhone;
        private String enquiryType;
        private String enquiryDetails;
        private Date enquiryDate;
        private boolean isResolved;

        public Enquiry(String enquiryID, String customerName, String contactEmail, String contactPhone,
                      String enquiryType, String enquiryDetails, Date enquiryDate, boolean isResolved) {
            this.enquiryID = enquiryID;
            this.customerName = customerName;
            this.contactEmail = contactEmail;
            this.contactPhone = contactPhone;
            this.enquiryType = enquiryType;
            this.enquiryDetails = enquiryDetails;
            this.enquiryDate = enquiryDate;
            this.isResolved = isResolved;
        }

        // Getters and setters
        public String getEnquiryID() { return enquiryID; }
        public void setEnquiryID(String enquiryID) { this.enquiryID = enquiryID; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getContactEmail() { return contactEmail; }
        public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

        public String getContactPhone() { return contactPhone; }
        public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

        public String getEnquiryType() { return enquiryType; }
        public void setEnquiryType(String enquiryType) { this.enquiryType = enquiryType; }

        public String getEnquiryDetails() { return enquiryDetails; }
        public void setEnquiryDetails(String enquiryDetails) { this.enquiryDetails = enquiryDetails; }

        public Date getEnquiryDate() { return enquiryDate; }
        public void setEnquiryDate(Date enquiryDate) { this.enquiryDate = enquiryDate; }

        public boolean isResolved() { return isResolved; }
        public void setResolved(boolean resolved) { isResolved = resolved; }

        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
            return "Enquiry: " + customerName + " (" + enquiryType + "), " +
                   dateFormat.format(enquiryDate) + ", " + (isResolved ? "Resolved" : "Pending");
        }
    }

    /**
     * Inner class for institution group bookings.
     */
    private class InstitutionGroupBookings {
        private String institutionName;
        private String contactPerson;
        private String contactEmail;
        private String contactPhone;
        private int groupSize;
        private Date bookingDate;
        private String specialRequirements;
        private boolean isConfirmed;

        public InstitutionGroupBookings(String institutionName, String contactPerson, String contactEmail,
                                       String contactPhone, int groupSize, Date bookingDate,
                                       String specialRequirements, boolean isConfirmed) {
            this.institutionName = institutionName;
            this.contactPerson = contactPerson;
            this.contactEmail = contactEmail;
            this.contactPhone = contactPhone;
            this.groupSize = groupSize;
            this.bookingDate = bookingDate;
            this.specialRequirements = specialRequirements;
            this.isConfirmed = isConfirmed;
        }

        // Getters and setters
        public String getInstitutionName() { return institutionName; }
        public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }

        public String getContactPerson() { return contactPerson; }
        public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

        public String getContactEmail() { return contactEmail; }
        public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

        public String getContactPhone() { return contactPhone; }
        public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

        public int getGroupSize() { return groupSize; }
        public void setGroupSize(int groupSize) { this.groupSize = groupSize; }

        public Date getBookingDate() { return bookingDate; }
        public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

        public String getSpecialRequirements() { return specialRequirements; }
        public void setSpecialRequirements(String specialRequirements) { this.specialRequirements = specialRequirements; }

        public boolean isConfirmed() { return isConfirmed; }
        public void setConfirmed(boolean confirmed) { isConfirmed = confirmed; }

        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
            return "Institution Booking: " + institutionName + " (" + contactPerson + "), " +
                   dateFormat.format(bookingDate) + ", " + groupSize + " attendees, " +
                   (isConfirmed ? "Confirmed" : "Pending");
        }
    }

    /**
     * Main method to start the application.
     */
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> new LancasterMusicApp());
    }
}
