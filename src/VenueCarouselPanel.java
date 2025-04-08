package com.lancaster.musicapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * VenueCarouselPanel - A panel that displays multiple venues with animated transitions
 * and rate information.
 */
public class VenueCarouselPanel extends JPanel {
    // Constants
    private static final int ANIMATION_DURATION = 500; // milliseconds
    private static final int ANIMATION_STEPS = 20;
    
    // UI Components
    private JPanel carouselPanel;
    private CardLayout cardLayout;
    private JPanel[] venuePanels;
    private JPanel controlPanel;
    private JButton prevButton;
    private JButton nextButton;
    private JLabel pageIndicator;
    private RateDisplayPanel ratePanel;
    private Timer animationTimer;
    
    // Data
    private List<Venue> venues;
    private int currentVenueIndex = 0;
    private boolean isAnimating = false;
    
    // Animation state
    private JPanel slidingPanel;
    private int animationStep = 0;
    private boolean slideRight;
    
    // Reference to colors and fonts
    private final Color PRIMARY_COLOR;
    private final Color SECONDARY_COLOR;
    private final Color ACCENT_COLOR;
    private final Color TEXT_COLOR;
    private final Color HIGHLIGHT_COLOR;
    private final Color BORDER_COLOR;
    private final Font HEADER_FONT;
    private final Font BUTTON_FONT;
    private final Font LABEL_FONT;
    private final Font BODY_FONT;
    
    /**
     * Constructor for VenueCarouselPanel
     */
    public VenueCarouselPanel(List<Venue> venues, Color[] colors, Font[] fonts) {
        this.venues = venues;
        
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
        
        setLayout(new BorderLayout());
        setBackground(PRIMARY_COLOR);
        
        // Create title
        JLabel titleLabel = createFancyLabel("Venue Information", HEADER_FONT, TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create carousel panel
        createCarouselPanel();
        
        // Create control panel
        createControlPanel();
        
        // Create rate panel
        createRatePanel();
        
        // Add components to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(carouselPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(ratePanel, BorderLayout.EAST);
        
        // Initialize animation timer
        animationTimer = new Timer(ANIMATION_DURATION / ANIMATION_STEPS, e -> {
            animationStep++;
            if (animationStep >= ANIMATION_STEPS) {
                // Animation complete
                animationStep = 0;
                isAnimating = false;
                animationTimer.stop();
                
                // Update UI
                updateCarousel();
            } else {
                // Update animation
                updateAnimation();
            }
        });
    }
    
    /**
     * Default constructor for VenueCarouselPanel
     * Creates a panel with default venues and colors
     */
    public VenueCarouselPanel() {
        // Create default venues
        List<Venue> defaultVenues = new ArrayList<>();
        
        // Create default facilities
        String[] facilities1 = {"Stage", "Sound System", "Lighting", "Seating"};
        String[] facilities2 = {"Piano", "Sound System", "Intimate Setting"};
        String[] facilities3 = {"Open Space", "Projector", "Display Panels"};
        
        // Create default rate info objects
        Venue.RateInfo rates1 = new Venue.RateInfo(1000, 1500, 2000, 8000);
        Venue.RateInfo rates2 = new Venue.RateInfo(300, 500, 800, 2500);
        Venue.RateInfo rates3 = new Venue.RateInfo(600, 800, 1200, 4000);
        
        // Create venues with proper constructor parameters
        defaultVenues.add(new Venue("Main Hall", 500, facilities1, 
            "Our largest venue space with capacity for 500 guests", "main_hall.jpg", rates1));
        defaultVenues.add(new Venue("Recital Room", 100, facilities2, 
            "Intimate setting perfect for small performances", "recital_room.jpg", rates2));
        defaultVenues.add(new Venue("Exhibition Space", 200, facilities3, 
            "Versatile area for displays and presentations", "exhibition_space.jpg", rates3));
        
        // Create default colors
        Color[] defaultColors = {
            new Color(26, 37, 48),     // PRIMARY_COLOR
            new Color(58, 121, 9),     // SECONDARY_COLOR
            new Color(101, 201, 21),   // ACCENT_COLOR
            new Color(255, 255, 255),  // TEXT_COLOR
            new Color(255, 140, 0),    // HIGHLIGHT_COLOR
            new Color(0, 0, 0)         // BORDER_COLOR
        };
        
        // Create default fonts
        Font[] defaultFonts = {
            new Font("Dialog", Font.BOLD, 24),    // HEADER_FONT
            new Font("Dialog", Font.BOLD, 16),    // BUTTON_FONT
            new Font("Dialog", Font.BOLD, 14),    // LABEL_FONT
            new Font("Dialog", Font.PLAIN, 14)    // BODY_FONT
        };
        
        // Initialize with default values
        this.venues = defaultVenues;
        
        // Initialize colors and fonts
        this.PRIMARY_COLOR = defaultColors[0];
        this.SECONDARY_COLOR = defaultColors[1];
        this.ACCENT_COLOR = defaultColors[2];
        this.TEXT_COLOR = defaultColors[3];
        this.HIGHLIGHT_COLOR = defaultColors[4];
        this.BORDER_COLOR = defaultColors[5];
        this.HEADER_FONT = defaultFonts[0];
        this.BUTTON_FONT = defaultFonts[1];
        this.LABEL_FONT = defaultFonts[2];
        this.BODY_FONT = defaultFonts[3];
        
        setLayout(new BorderLayout());
        setBackground(PRIMARY_COLOR);
        
        // Create title
        JLabel titleLabel = createFancyLabel("Venue Information", HEADER_FONT, TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create carousel panel
        createCarouselPanel();
        
        // Create control panel
        createControlPanel();
        
        // Create rate panel
        createRatePanel();
        
        // Add components to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(carouselPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(ratePanel, BorderLayout.EAST);
        
        // Initialize animation timer
        animationTimer = new Timer(ANIMATION_DURATION / ANIMATION_STEPS, e -> {
            animationStep++;
            if (animationStep >= ANIMATION_STEPS) {
                // Animation complete
                animationStep = 0;
                isAnimating = false;
                animationTimer.stop();
                
                // Update UI
                updateCarousel();
            } else {
                // Update animation
                updateAnimation();
            }
        });
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
     * Create the carousel panel.
     */
    private void createCarouselPanel() {
        carouselPanel = new JPanel();
        cardLayout = new CardLayout();
        carouselPanel.setLayout(cardLayout);
        carouselPanel.setBackground(PRIMARY_COLOR);
        carouselPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create venue panels
        venuePanels = new JPanel[venues.size()];
        
        for (int i = 0; i < venues.size(); i++) {
            Venue venue = venues.get(i);
            
            // Create panel for this venue
            JPanel venuePanel = new JPanel();
            venuePanel.setLayout(new BoxLayout(venuePanel, BoxLayout.Y_AXIS));
            venuePanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));
            venuePanel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)
            ));
            
            // Create venue name
            JLabel nameLabel = new JLabel(venue.getName());
            nameLabel.setFont(HEADER_FONT);
            nameLabel.setForeground(TEXT_COLOR);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Create venue description
            JTextArea descriptionArea = new JTextArea(venue.getDescription());
            descriptionArea.setFont(BODY_FONT);
            descriptionArea.setForeground(TEXT_COLOR);
            descriptionArea.setBackground(new Color(0, 0, 0, 0));
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setLineWrap(true);
            descriptionArea.setEditable(false);
            descriptionArea.setOpaque(false);
            descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
            descriptionArea.setMaximumSize(new Dimension(500, 100));
            
            // Create capacity info
            JLabel capacityLabel = new JLabel("Capacity: " + venue.getCapacity() + " guests");
            capacityLabel.setFont(LABEL_FONT);
            capacityLabel.setForeground(TEXT_COLOR);
            capacityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Create venue image placeholder
            JPanel imagePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw image placeholder
                    g2d.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 50));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Draw border
                    g2d.setColor(BORDER_COLOR);
                    g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    
                    // Draw venue name
                    g2d.setFont(new Font("Dialog", Font.BOLD, 18));
                    g2d.setColor(TEXT_COLOR);
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = venue.getName();
                    int textWidth = fm.stringWidth(text);
                    g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2);
                    
                    g2d.dispose();
                }
                
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(400, 200);
                }
            };
            imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Add components to venue panel
            venuePanel.add(nameLabel);
            venuePanel.add(Box.createRigidArea(new Dimension(0, 10)));
            venuePanel.add(imagePanel);
            venuePanel.add(Box.createRigidArea(new Dimension(0, 10)));
            venuePanel.add(descriptionArea);
            venuePanel.add(Box.createRigidArea(new Dimension(0, 10)));
            venuePanel.add(capacityLabel);
            
            // Add venue panel to carousel
            carouselPanel.add(venuePanel, "venue" + i);
            venuePanels[i] = venuePanel;
        }
        
        // Show first venue
        if (venues.size() > 0) {
            cardLayout.show(carouselPanel, "venue0");
        }
    }
    
    /**
     * Create the control panel.
     */
    private void createControlPanel() {
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(PRIMARY_COLOR);
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create navigation buttons
        prevButton = createNavigationButton("◀ Previous");
        nextButton = createNavigationButton("Next ▶");
        
        // Create page indicator
        pageIndicator = new JLabel("1 of " + venues.size());
        pageIndicator.setFont(BODY_FONT);
        pageIndicator.setForeground(TEXT_COLOR);
        pageIndicator.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add action listeners
        prevButton.addActionListener(e -> {
            if (!isAnimating && venues.size() > 1) {
                isAnimating = true;
                slideRight = false;
                animationStep = 0;
                
                // Update current index
                currentVenueIndex--;
                if (currentVenueIndex < 0) {
                    currentVenueIndex = venues.size() - 1;
                }
                
                // Start animation
                startAnimation();
            }
        });
        
        nextButton.addActionListener(e -> {
            if (!isAnimating && venues.size() > 1) {
                isAnimating = true;
                slideRight = true;
                animationStep = 0;
                
                // Update current index
                currentVenueIndex++;
                if (currentVenueIndex >= venues.size()) {
                    currentVenueIndex = 0;
                }
                
                // Start animation
                startAnimation();
            }
        });
        
        // Add components to control panel
        controlPanel.add(prevButton, BorderLayout.WEST);
        controlPanel.add(pageIndicator, BorderLayout.CENTER);
        controlPanel.add(nextButton, BorderLayout.EAST);
    }
    
    /**
     * Create the rate panel.
     */
    private void createRatePanel() {
        // Create rate panel with colors and fonts
        ratePanel = new RateDisplayPanel(
            new Color[]{PRIMARY_COLOR, SECONDARY_COLOR, ACCENT_COLOR, TEXT_COLOR, HIGHLIGHT_COLOR, BORDER_COLOR},
            new Font[]{HEADER_FONT, BUTTON_FONT, LABEL_FONT, BODY_FONT}
        );
        
        // Update with first venue if available
        if (venues.size() > 0) {
            updateRatePanel(venues.get(0));
        }
    }
    
    /**
     * Create a navigation button.
     */
    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(ACCENT_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HIGHLIGHT_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Start the carousel animation.
     */
    private void startAnimation() {
        // Create sliding panel
        slidingPanel = new JPanel(new GridLayout(1, 2));
        slidingPanel.setOpaque(false);
        
        // Get current and next venue panels
        JPanel currentPanel = venuePanels[currentVenueIndex];
        
        // Start animation timer
        animationTimer.start();
    }
    
    /**
     * Update the animation.
     */
    private void updateAnimation() {
        // Repaint to show animation progress
        repaint();
    }
    
    /**
     * Update the carousel display.
     */
    private void updateCarousel() {
        // Show current venue
        cardLayout.show(carouselPanel, "venue" + currentVenueIndex);
        
        // Update page indicator
        pageIndicator.setText((currentVenueIndex + 1) + " of " + venues.size());
        
        // Update rate panel
        updateRatePanel(venues.get(currentVenueIndex));
    }
    
    /**
     * Update the rate panel with the specified venue.
     */
    private void updateRatePanel(Venue venue) {
        ratePanel.updateRates(venue.getRates(), venue.getName());
    }
}
