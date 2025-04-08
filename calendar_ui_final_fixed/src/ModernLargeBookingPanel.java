package com.lancaster.musicapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

/**
 * ModernLargeBookingPanel - A completely redesigned Large Bookings panel with a modern calendar
 * This class replaces the original LargeBookingPanel while maintaining the same functionality
 * and integration with the existing navigation structure.
 */
public class ModernLargeBookingPanel extends JPanel {
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
    private ModernCalendar calendar;
    private JTextField dateField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JSpinner attendeesSpinner;
    private JTextArea notesArea;
    private JTabbedPane tabbedPane;
    
    // Data structures - using Object type to avoid direct dependency on private inner classes
    private List<Object> bookings;
    private List<Object> inquiries;
    
    // Reference to main frame for dialogs
    private JFrame mainFrame;
    
    // Button animation state tracking
    private Map<JButton, Point> buttonOriginalPositions = new HashMap<>();
    
    /**
     * Constructor for ModernLargeBookingPanel.
     * 
     * @param mainFrame Reference to the main application frame
     * @param bookings Reference to the bookings list
     * @param inquiries Reference to the inquiries list
     * @param colors Application color scheme
     * @param fonts Application fonts
     */
    public ModernLargeBookingPanel(JFrame mainFrame, List<Object> bookings, List<Object> inquiries, 
                                  Color[] colors, Font[] fonts) {
        this.mainFrame = mainFrame;
        this.bookings = bookings;
        this.inquiries = inquiries;
        
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
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create components
        createContent();
    }
    
    /**
     * Create the main content of the panel.
     */
    private void createContent() {
        // Create title
        JLabel titleLabel = createAnimatedTitleLabel("Large Bookings", HEADER_FONT, TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(BUTTON_FONT);
        tabbedPane.setBackground(PRIMARY_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
        
        // Create tabs
        JPanel calendarPanel = createCalendarPanel();
        tabbedPane.addTab("Calendar", calendarPanel);
        
        JPanel bookingPanel = createBookingPanel();
        tabbedPane.addTab("Make a Booking", bookingPanel);
        
        JPanel inquiryPanel = createInquiryPanel();
        tabbedPane.addTab("Company/Institution Inquiry", inquiryPanel);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setOpaque(false);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Add main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
        
        // Set preferred size with relative sizing for better responsiveness
        setPreferredSize(new Dimension(
            (int)(Toolkit.getDefaultToolkit().getScreenSize().width * 0.7),
            (int)(Toolkit.getDefaultToolkit().getScreenSize().height * 0.7)
        ));
    }
    
    /**
     * Create an animated title label with floating effect.
     */
    private JLabel createAnimatedTitleLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text) {
            private float offset = 0;
            private javax.swing.Timer animationTimer;
            
            {
                // Set up animation timer
                animationTimer = new javax.swing.Timer(50, e -> {
                    offset += 0.1f;
                    if (offset > 2 * Math.PI) {
                        offset -= 2 * Math.PI;
                    }
                    repaint();
                });
                animationTimer.start();
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Calculate vertical offset for floating effect
                float yOffset = (float) (Math.sin(offset) * 3);
                
                // Draw text shadow
                g2d.setFont(getFont());
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(getText(), 2, getHeight() - 4 + (int) yOffset);
                
                // Draw text
                g2d.setColor(getForeground());
                g2d.drawString(getText(), 0, getHeight() - 6 + (int) yOffset);
                
                g2d.dispose();
            }
        };
        label.setFont(font);
        label.setForeground(color);
        
        return label;
    }
    
    /**
     * Create the calendar panel.
     */
    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create calendar
        calendar = new ModernCalendar();
        
        // Create events panel (placeholder for future enhancement)
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));
        eventsPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        eventsPanel.setPreferredSize(new Dimension(200, 300));
        
        // Create events title
        JLabel eventsTitle = new JLabel("Events for Selected Date");
        eventsTitle.setFont(LABEL_FONT);
        eventsTitle.setForeground(TEXT_COLOR);
        eventsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create events list (placeholder)
        JLabel noEventsLabel = new JLabel("No events scheduled for this date.");
        noEventsLabel.setFont(BODY_FONT);
        noEventsLabel.setForeground(TEXT_COLOR);
        noEventsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to events panel
        eventsPanel.add(eventsTitle);
        eventsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        eventsPanel.add(noEventsLabel);
        
        // Add components to panel
        panel.add(calendar, BorderLayout.CENTER);
        panel.add(eventsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Create the booking panel.
     */
    private JPanel createBookingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));
        formPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Create form title
        JLabel formTitle = new JLabel("Book the Venue") {
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
        formTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        formTitle.setForeground(TEXT_COLOR);
        formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create form components
        JPanel formFieldsPanel = new JPanel(new GridLayout(0, 2, 20, 15));
        formFieldsPanel.setOpaque(false);
        
        // Date field
        JLabel dateLabel = createFormLabel("Date:");
        dateField = new JTextField(20);
        dateField.setEditable(false);
        
        // Add property change listener to calendar
        calendar.addPropertyChangeListener("calendar", evt -> {
            dateField.setText(calendar.getFormattedDate());
        });
        
        // Date picker button
        JButton datePickerButton = createAnimatedButton("Select Date");
        datePickerButton.addActionListener(e -> {
            tabbedPane.setSelectedIndex(0); // Switch to calendar tab
        });
        
        JPanel datePanel = new JPanel(new BorderLayout(10, 0));
        datePanel.setOpaque(false);
        datePanel.add(dateField, BorderLayout.CENTER);
        datePanel.add(datePickerButton, BorderLayout.EAST);
        
        // Name field
        JLabel nameLabel = createFormLabel("Name:");
        nameField = new JTextField(20);
        
        // Email field
        JLabel emailLabel = createFormLabel("Email:");
        emailField = new JTextField(20);
        
        // Phone field
        JLabel phoneLabel = createFormLabel("Phone:");
        phoneField = new JTextField(20);
        
        // Attendees field
        JLabel attendeesLabel = createFormLabel("Number of Attendees:");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(50, 1, 500, 1);
        attendeesSpinner = new JSpinner(spinnerModel);
        attendeesSpinner.setEditor(new JSpinner.NumberEditor(attendeesSpinner, "#"));
        
        // Notes field
        JLabel notesLabel = createFormLabel("Additional Notes:");
        notesArea = new JTextArea(5, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        
        // Add components to form fields panel
        formFieldsPanel.add(dateLabel);
        formFieldsPanel.add(datePanel);
        formFieldsPanel.add(nameLabel);
        formFieldsPanel.add(nameField);
        formFieldsPanel.add(emailLabel);
        formFieldsPanel.add(emailField);
        formFieldsPanel.add(phoneLabel);
        formFieldsPanel.add(phoneField);
        formFieldsPanel.add(attendeesLabel);
        formFieldsPanel.add(attendeesSpinner);
        
        // Create notes panel
        JPanel notesPanel = new JPanel(new BorderLayout(0, 5));
        notesPanel.setOpaque(false);
        notesPanel.add(notesLabel, BorderLayout.NORTH);
        notesPanel.add(notesScroll, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton submitButton = createAnimatedButton("Submit Booking");
        buttonPanel.add(submitButton);
        
        // Add action listener to submit button
        submitButton.addActionListener(e -> {
            if (dateField.getText().isEmpty() || nameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                showErrorDialog("Please fill in all required fields.", "Missing Information");
            } else {
                try {
                    // Generate a unique booking ID
                    String bookingID = "BK" + System.currentTimeMillis();
                    
                    // Get the date from the calendar
                    Date bookingDate = calendar.getSelectedDate();
                    
                    // Get the number of attendees
                    int attendees = (Integer) attendeesSpinner.getValue();
                    
                    // Create a new booking using reflection to access the private Booking class
                    // First, get the parent class that contains the Booking class
                    Class<?> parentClass = Class.forName("com.lancaster.musicapp.LancasterMusicApp");
                    
                    // Get the Booking inner class
                    Class<?> bookingClass = Class.forName("com.lancaster.musicapp.LancasterMusicApp$Booking");
                    
                    // Get the constructor for the Booking class
                    Constructor<?> constructor = bookingClass.getDeclaredConstructor(
                        parentClass, String.class, String.class, String.class, String.class, 
                        Date.class, String.class, int.class);
                    constructor.setAccessible(true);
                    
                    // Create a new instance of LancasterMusicApp to use as the outer class
                    Object appInstance = parentClass.getDeclaredConstructor().newInstance();
                    
                    // Create a new booking instance
                    Object bookingInstance = constructor.newInstance(
                        appInstance,
                        bookingID,
                        nameField.getText(),
                        "Credit Card", // Default payment type
                        attendees + " attendees",
                        bookingDate,
                        notesArea.getText(),
                        calculateCost(attendees)
                    );
                    
                    // Call the setBooking method to store in database
                    Method setBookingMethod = bookingClass.getDeclaredMethod("setBooking", 
                        String.class, String.class, String.class, String.class, 
                        Date.class, String.class, int.class);
                    setBookingMethod.setAccessible(true);
                    
                    Boolean result = (Boolean) setBookingMethod.invoke(
                        bookingInstance,
                        bookingID,
                        nameField.getText(),
                        "Credit Card", // Default payment type
                        attendees + " attendees",
                        bookingDate,
                        notesArea.getText(),
                        calculateCost(attendees)
                    );
                    
                    if (result) {
                        // Add the booking to the list
                        if (bookings != null) {
                            bookings.add(bookingInstance);
                        }
                        
                        // Also add to the BookingManager for staff management
                        BookingManager.getInstance().addBooking(
                            new BookingManager.BookingEntry(
                                bookingID,
                                nameField.getText(),
                                "Credit Card",
                                attendees + " attendees",
                                bookingDate,
                                notesArea.getText(),
                                calculateCost(attendees),
                                "Pending"
                            )
                        );
                        
                        // Show success message
                        showSuccessDialog("Your booking has been submitted successfully!", "Booking Confirmed");
                        
                        // Clear form fields
                        nameField.setText("");
                        emailField.setText("");
                        phoneField.setText("");
                        attendeesSpinner.setValue(50);
                        notesArea.setText("");
                    } else {
                        showErrorDialog("There was an error submitting your booking. Please try again.", "Booking Error");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showErrorDialog("An error occurred: " + ex.getMessage(), "System Error");
                }
            }
        });
        
        // Add components to form panel
        formPanel.add(formTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(formFieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(notesPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonPanel);
        
        // Add form panel to main panel
        panel.add(formPanel);
        
        return panel;
    }
    
    /**
     * Create the inquiry panel.
     */
    private JPanel createInquiryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), SECONDARY_COLOR.getBlue(), 80));
        formPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Create form title
        JLabel formTitle = new JLabel("Company/Institution Inquiry") {
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
        formTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        formTitle.setForeground(TEXT_COLOR);
        formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create form components
        JPanel formFieldsPanel = new JPanel(new GridLayout(0, 2, 20, 15));
        formFieldsPanel.setOpaque(false);
        
        // Company name field
        JLabel companyLabel = createFormLabel("Company/Institution Name:");
        JTextField companyField = new JTextField(20);
        
        // Contact name field
        JLabel contactNameLabel = createFormLabel("Contact Person:");
        JTextField contactNameField = new JTextField(20);
        
        // Email field
        JLabel emailLabel = createFormLabel("Email:");
        JTextField emailField = new JTextField(20);
        
        // Phone field
        JLabel phoneLabel = createFormLabel("Phone:");
        JTextField phoneField = new JTextField(20);
        
        // Event type field
        JLabel eventTypeLabel = createFormLabel("Event Type:");
        String[] eventTypes = {"Conference", "Concert", "Exhibition", "Workshop", "Other"};
        JComboBox<String> eventTypeComboBox = new JComboBox<>(eventTypes);
        
        // Expected attendees field
        JLabel attendeesLabel = createFormLabel("Expected Attendees:");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(100, 1, 1000, 10);
        JSpinner attendeesSpinner = new JSpinner(spinnerModel);
        attendeesSpinner.setEditor(new JSpinner.NumberEditor(attendeesSpinner, "#"));
        
        // Preferred dates field
        JLabel datesLabel = createFormLabel("Preferred Dates:");
        JTextField datesField = new JTextField(20);
        
        // Inquiry details field
        JLabel detailsLabel = createFormLabel("Inquiry Details:");
        JTextArea detailsArea = new JTextArea(5, 20);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        
        // Add components to form fields panel
        formFieldsPanel.add(companyLabel);
        formFieldsPanel.add(companyField);
        formFieldsPanel.add(contactNameLabel);
        formFieldsPanel.add(contactNameField);
        formFieldsPanel.add(emailLabel);
        formFieldsPanel.add(emailField);
        formFieldsPanel.add(phoneLabel);
        formFieldsPanel.add(phoneField);
        formFieldsPanel.add(eventTypeLabel);
        formFieldsPanel.add(eventTypeComboBox);
        formFieldsPanel.add(attendeesLabel);
        formFieldsPanel.add(attendeesSpinner);
        formFieldsPanel.add(datesLabel);
        formFieldsPanel.add(datesField);
        
        // Create details panel
        JPanel detailsPanel = new JPanel(new BorderLayout(0, 5));
        detailsPanel.setOpaque(false);
        detailsPanel.add(detailsLabel, BorderLayout.NORTH);
        detailsPanel.add(detailsScroll, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton submitButton = createAnimatedButton("Submit Inquiry");
        buttonPanel.add(submitButton);
        
        // Add action listener to submit button
        submitButton.addActionListener(e -> {
            if (companyField.getText().isEmpty() || contactNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                datesField.getText().isEmpty() || detailsArea.getText().isEmpty()) {
                showErrorDialog("Please fill in all required fields.", "Missing Information");
            } else {
                // Generate a unique inquiry ID
                String inquiryID = "INQ" + System.currentTimeMillis();
                
                // Create a new inquiry
                // In a real application, this would be saved to a database
                
                // Show success message
                showSuccessDialog("Your inquiry has been submitted successfully! Our team will contact you shortly.", "Inquiry Submitted");
                
                // Clear form fields
                companyField.setText("");
                contactNameField.setText("");
                emailField.setText("");
                phoneField.setText("");
                eventTypeComboBox.setSelectedIndex(0);
                attendeesSpinner.setValue(100);
                datesField.setText("");
                detailsArea.setText("");
            }
        });
        
        // Add components to form panel
        formPanel.add(formTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(formFieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(detailsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonPanel);
        
        // Add form panel to main panel
        panel.add(formPanel);
        
        return panel;
    }
    
    /**
     * Create a form label.
     */
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    /**
     * Create an animated button with hover and press effects.
     */
    private JButton createAnimatedButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(ACCENT_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        
        // Store original position
        button.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                if (!buttonOriginalPositions.containsKey(button)) {
                    buttonOriginalPositions.put(button, button.getLocation());
                }
            }
        });
        
        // Add hover and press effects
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
                Point originalPos = buttonOriginalPositions.get(button);
                if (originalPos != null) {
                    button.setLocation(originalPos.x, originalPos.y + 2);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                Point originalPos = buttonOriginalPositions.get(button);
                if (originalPos != null) {
                    button.setLocation(originalPos.x, originalPos.y);
                }
            }
        });
        
        return button;
    }
    
    /**
     * Show an error dialog.
     */
    private void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show a success dialog.
     */
    private void showSuccessDialog(String message, String title) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Calculate the cost based on the number of attendees.
     */
    private int calculateCost(int attendees) {
        // Base cost is £500
        int baseCost = 500;
        
        // Add £10 per attendee
        int attendeeCost = attendees * 10;
        
        return baseCost + attendeeCost;
    }
}
