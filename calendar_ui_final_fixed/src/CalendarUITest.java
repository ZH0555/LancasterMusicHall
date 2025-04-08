package com.lancaster.musicapp;

import javax.swing.*;
import java.awt.*;

/**
 * Test class for the new ModernCalendar implementation.
 * This class creates a standalone window to test the calendar functionality.
 */
public class CalendarUITest {

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show the test window on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Modern Calendar Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        
        // Set dark background to match the application theme
        frame.getContentPane().setBackground(new Color(26, 37, 48));
        
        // Create a panel to hold the calendar with some padding
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(26, 37, 48));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create the modern calendar
        ModernCalendar calendar = new ModernCalendar();
        
        // Add a label to display the selected date
        JLabel selectedDateLabel = new JLabel("No date selected");
        selectedDateLabel.setForeground(Color.WHITE);
        selectedDateLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        selectedDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add property change listener to update the label when a date is selected
        calendar.addPropertyChangeListener("calendar", evt -> {
            java.util.Calendar selectedDate = calendar.getCalendar();
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMMM d, yyyy");
            selectedDateLabel.setText("Selected Date: " + dateFormat.format(selectedDate.getTime()));
        });
        
        // Add components to the main panel
        mainPanel.add(calendar, BorderLayout.CENTER);
        mainPanel.add(selectedDateLabel, BorderLayout.SOUTH);
        
        // Add the main panel to the frame
        frame.add(mainPanel);
        
        // Display the frame
        frame.setVisible(true);
    }
}
