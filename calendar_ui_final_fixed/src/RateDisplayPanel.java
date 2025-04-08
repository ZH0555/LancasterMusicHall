package com.lancaster.musicapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * RateDisplayPanel - An enhanced panel for displaying venue rates with animations
 */
public class RateDisplayPanel extends JPanel {
    // Constants
    private static final int ANIMATION_DURATION = 400; // milliseconds
    private static final int ANIMATION_STEPS = 15;
    
    // UI Components
    private JLabel titleLabel;
    private JPanel rateCardsPanel;
    private JPanel[] rateCards;
    private Timer animationTimer;
    
    // Reference to colors and fonts
    private final Color PRIMARY_COLOR;
    private final Color SECONDARY_COLOR;
    private final Color ACCENT_COLOR;
    private final Color TEXT_COLOR;
    private final Color HIGHLIGHT_COLOR;
    private final Color BORDER_COLOR;
    private final Font HEADER_FONT;
    private final Font LABEL_FONT;
    private final Font BODY_FONT;
    
    // Animation state
    private int animationStep = 0;
    private boolean isAnimating = false;
    
    /**
     * Constructor for RateDisplayPanel
     */
    public RateDisplayPanel(Color[] colors, Font[] fonts) {
        // Initialize colors and fonts
        this.PRIMARY_COLOR = colors[0];
        this.SECONDARY_COLOR = colors[1];
        this.ACCENT_COLOR = colors[2];
        this.TEXT_COLOR = colors[3];
        this.HIGHLIGHT_COLOR = colors[4];
        this.BORDER_COLOR = colors[5];
        this.HEADER_FONT = fonts[0];
        this.LABEL_FONT = fonts[1];
        this.BODY_FONT = fonts[3];
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 200));
        setBorder(new CompoundBorder(
            new MatteBorder(0, 2, 0, 0, BORDER_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));
        setPreferredSize(new Dimension(250, 0));
        
        // Create title
        titleLabel = new JLabel("Venue Rates");
        titleLabel.setFont(new Font(HEADER_FONT.getName(), HEADER_FONT.getStyle(), HEADER_FONT.getSize() - 6));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create panel for rate cards
        rateCardsPanel = new JPanel();
        rateCardsPanel.setLayout(new BoxLayout(rateCardsPanel, BoxLayout.Y_AXIS));
        rateCardsPanel.setOpaque(false);
        rateCardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to panel
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(rateCardsPanel);
        add(Box.createVerticalGlue());
        
        // Add booking info
        JLabel bookingLabel = new JLabel("For bookings, please contact us");
        bookingLabel.setFont(BODY_FONT);
        bookingLabel.setForeground(TEXT_COLOR);
        bookingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        add(bookingLabel);
        
        // Initialize animation timer
        animationTimer = new Timer(ANIMATION_DURATION / ANIMATION_STEPS, e -> {
            animationStep++;
            if (animationStep >= ANIMATION_STEPS) {
                // Animation complete
                animationTimer.stop();
                isAnimating = false;
                animationStep = 0;
            } else {
                // Update animation
                updateAnimation();
            }
        });
    }
    
    /**
     * Update the panel with new rate information
     */
    public void updateRates(Venue.RateInfo rates, String venueName) {
        if (isAnimating) {
            animationTimer.stop();
        }
        
        // Start fade out animation
        isAnimating = true;
        animationStep = 0;
        animationTimer.start();
        
        // Update title with venue name
        titleLabel.setText(venueName + " Rates");
        
        // Clear existing rate cards
        rateCardsPanel.removeAll();
        
        // Create new rate cards
        rateCards = new JPanel[4];
        rateCards[0] = createRateCard("Hourly Rate", "$" + rates.getHourlyRate() + "/hour");
        rateCards[1] = createRateCard("Daily Rate", "$" + rates.getDailyRate() + "/day");
        rateCards[2] = createRateCard("Weekend Rate", "$" + rates.getWeekendRate() + "/weekend");
        rateCards[3] = createRateCard("Weekly Rate", "$" + rates.getWeeklyRate() + "/week");
        
        // Add rate cards to panel with spacing
        for (int i = 0; i < rateCards.length; i++) {
            rateCardsPanel.add(rateCards[i]);
            if (i < rateCards.length - 1) {
                rateCardsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        
        rateCardsPanel.revalidate();
        rateCardsPanel.repaint();
    }
    
    /**
     * Create a card to display a rate
     */
    private JPanel createRateCard(String rateType, String rateValue) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 100),
                    getWidth(), getHeight(), new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 150)
                );
                g2d.setPaint(gradient);
                
                // Draw rounded rectangle
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Draw border
                g2d.setColor(BORDER_COLOR);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                
                g2d.dispose();
                
                super.paintComponent(g);
            }
        };
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(210, 80));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create rate type label
        JLabel typeLabel = new JLabel(rateType);
        typeLabel.setFont(LABEL_FONT);
        typeLabel.setForeground(TEXT_COLOR);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create rate value label
        JLabel valueLabel = new JLabel(rateValue);
        valueLabel.setFont(new Font(HEADER_FONT.getName(), Font.BOLD, 18));
        valueLabel.setForeground(HIGHLIGHT_COLOR);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to card
        card.add(typeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(valueLabel);
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                    new LineBorder(HIGHLIGHT_COLOR, 2, true),
                    new EmptyBorder(8, 8, 8, 8)
                ));
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(new EmptyBorder(10, 10, 10, 10));
                card.repaint();
            }
        });
        
        return card;
    }
    
    /**
     * Update the animation for each step
     */
    private void updateAnimation() {
        // Calculate progress (0.0 to 1.0)
        float progress = (float) animationStep / ANIMATION_STEPS;
        
        // Apply animation effects
        if (animationStep < ANIMATION_STEPS / 2) {
            // Fade out
            float alpha = 1.0f - (progress * 2);
            // Ensure alpha is within valid range (0-255)
            int alphaValue = Math.max(0, Math.min(255, (int)(200 * alpha)));
            setBackground(new Color(
                SECONDARY_COLOR.getRed(),
                SECONDARY_COLOR.getGreen(),
                SECONDARY_COLOR.getBlue(),
                alphaValue
            ));
        } else {
            // Fade in
            float alpha = (progress - 0.5f) * 2;
            // Ensure alpha is within valid range (0-255)
            int alphaValue = Math.max(0, Math.min(255, (int)(200 * alpha)));
            setBackground(new Color(
                SECONDARY_COLOR.getRed(),
                SECONDARY_COLOR.getGreen(),
                SECONDARY_COLOR.getBlue(),
                alphaValue
            ));
            
            // Apply scale effect to rate cards
            if (rateCards != null) {
                for (int i = 0; i < rateCards.length; i++) {
                    float cardProgress = Math.min(1.0f, (progress - 0.5f) * 2 + (i * 0.1f));
                    rateCards[i].setVisible(cardProgress > 0);
                    if (cardProgress > 0) {
                        setAlpha(rateCards[i], cardProgress);
                    }
                }
            }
        }
        
        repaint();
    }
    
    /**
     * Set the alpha value for a component
     */
    private void setAlpha(JComponent component, float alpha) {
        component.putClientProperty("alpha", alpha);
    }
}
