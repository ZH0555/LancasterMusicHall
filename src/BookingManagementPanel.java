package com.lancaster.musicapp;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * BookingManagementPanel - A panel for staff to manage bookings
 * This panel displays all bookings and allows staff to approve or deny them
 */
public class BookingManagementPanel extends JPanel {
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
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private JButton approveButton;
    private JButton denyButton;
    private JButton refreshButton;
    private JButton logoutButton;
    
    // Reference to main frame for dialogs and navigation
    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    /**
     * Constructor for BookingManagementPanel
     * 
     * @param mainFrame Reference to the main application frame
     * @param cardLayout Card layout for navigation
     * @param contentPanel Content panel for navigation
     * @param colors Application color scheme
     * @param fonts Application fonts
     */
    public BookingManagementPanel(JFrame mainFrame, CardLayout cardLayout, JPanel contentPanel, 
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
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create components
        createContent();
    }
    
    /**
     * Create the main content of the panel
     */
    private void createContent() {
        // Create title
        JLabel titleLabel = new JLabel("Booking Management");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(SECONDARY_COLOR.getRed(), SECONDARY_COLOR.getGreen(), 
                                          SECONDARY_COLOR.getBlue(), 80));
        tablePanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Create table model with columns
        String[] columnNames = {"ID", "Customer", "Date", "Tickets", "Cost", "Notes", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        // Create table
        bookingsTable = new JTable(tableModel);
        bookingsTable.setFont(BODY_FONT);
        bookingsTable.setRowHeight(25);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingsTable.getTableHeader().setFont(LABEL_FONT);
        bookingsTable.getTableHeader().setBackground(SECONDARY_COLOR);
        bookingsTable.getTableHeader().setForeground(TEXT_COLOR);
        
        // Set column widths
        bookingsTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        bookingsTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Customer
        bookingsTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Date
        bookingsTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Tickets
        bookingsTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Cost
        bookingsTable.getColumnModel().getColumn(5).setPreferredWidth(200); // Notes
        bookingsTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Create buttons
        approveButton = createAnimatedButton("Approve");
        approveButton.addActionListener(e -> handleApprove());
        
        denyButton = createAnimatedButton("Deny");
        denyButton.addActionListener(e -> handleDeny());
        
        refreshButton = createAnimatedButton("Refresh");
        refreshButton.addActionListener(e -> refreshBookings());
        
        logoutButton = createAnimatedButton("Logout");
        logoutButton.addActionListener(e -> cardLayout.show(contentPanel, "welcome"));
        
        // Add buttons to panel
        buttonPanel.add(approveButton);
        buttonPanel.add(denyButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);
        
        // Add components to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load bookings initially
        refreshBookings();
    }
    
    /**
     * Refresh the bookings table with data from BookingManager
     */
    public void refreshBookings() {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        // Get bookings from manager
        List<BookingManager.BookingEntry> bookings = BookingManager.getInstance().getAllBookings();
        
        // Format date for display
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        // Add bookings to table
        for (BookingManager.BookingEntry booking : bookings) {
            Object[] rowData = {
                booking.getBookingId(),
                booking.getCustomerName(),
                dateFormat.format(booking.getBookingDate()),
                booking.getTicketsBooked(),
                "$" + booking.getTotalCost(),
                booking.getNotes(),
                booking.getStatus()
            };
            tableModel.addRow(rowData);
        }
        
        // Update UI
        bookingsTable.repaint();
    }
    
    /**
     * Handle approve button click
     */
    private void handleApprove() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String bookingId = (String) tableModel.getValueAt(selectedRow, 0);
            if (BookingManager.getInstance().approveBooking(bookingId)) {
                refreshBookings();
                JOptionPane.showMessageDialog(mainFrame,
                    "Booking approved successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                    "Failed to approve booking.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame,
                "Please select a booking to approve.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Handle deny button click
     */
    private void handleDeny() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String bookingId = (String) tableModel.getValueAt(selectedRow, 0);
            if (BookingManager.getInstance().denyBooking(bookingId)) {
                refreshBookings();
                JOptionPane.showMessageDialog(mainFrame,
                    "Booking denied successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                    "Failed to deny booking.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame,
                "Please select a booking to deny.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
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
