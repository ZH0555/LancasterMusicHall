package com.lancaster.musicapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ModernCalendar - A completely redesigned calendar component with smooth transitions
 * This class provides a modern calendar UI with animations and visual effects.
 */
public class ModernCalendar extends JPanel {
    // Constants for appearance
    private static final Color HEADER_BG = new Color(75, 0, 130);
    private static final Color HEADER_TEXT = new Color(255, 255, 255);
    private static final Color DAY_NAMES_BG = new Color(90, 20, 150);
    private static final Color DAYS_BG = new Color(26, 37, 48);
    private static final Color SELECTED_BG = new Color(255, 140, 0);
    private static final Color TODAY_BG = new Color(138, 43, 226, 100);
    private static final Color HOVER_BG = new Color(138, 43, 226, 50);
    private static final Color WEEKEND_BG = new Color(45, 65, 85);
    private static final Color BORDER_COLOR = new Color(0, 0, 0);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color OTHER_MONTH_TEXT = new Color(150, 150, 150);
    
    // Animation constants
    private static final int ANIMATION_DURATION = 300; // milliseconds
    private static final int ANIMATION_STEPS = 30;
    
    /**
     * Calculate adaptive timing based on system performance.
     */
    private int calculateAdaptiveTiming() {
        // Simple approach based on available processors
        int processors = Runtime.getRuntime().availableProcessors();
        return Math.max(5, ANIMATION_DURATION / (ANIMATION_STEPS * Math.max(1, processors / 2)));
    }
    
    // UI components
    private JPanel headerPanel;
    private JPanel daysPanel;
    private JLabel monthYearLabel;
    private JButton prevButton;
    private JButton nextButton;
    private JButton yearButton;
    private JPanel yearSelectionPanel;
    private JPanel contentPanel; // New container panel for CENTER content
    private JPanel[][] dayPanels;
    private JLabel[][] dayLabels;
    
    // Calendar state
    private Calendar calendar;
    private Calendar selectedDate;
    private Calendar displayedMonth;
    private AtomicBoolean animating = new AtomicBoolean(false); // Using AtomicBoolean for thread safety
    private int animationDirection = 0; // -1 for prev, 1 for next
    private int animationStep = 0;
    private javax.swing.Timer animationTimer;
    private AtomicBoolean yearSelectionVisible = new AtomicBoolean(false); // Using AtomicBoolean for thread safety
    
    // Day cell tracking
    private int hoveredRow = -1;
    private int hoveredCol = -1;
    private int selectedRow = -1;
    private int selectedCol = -1;
    
    // Mouse event debouncing
    private javax.swing.Timer hoverDebounceTimer;
    private static final int HOVER_DEBOUNCE_DELAY = 20; // milliseconds
    
    /**
     * Constructor for ModernCalendar.
     */
    public ModernCalendar() {
        // Initialize calendars
        calendar = Calendar.getInstance();
        selectedDate = (Calendar) calendar.clone();
        displayedMonth = (Calendar) calendar.clone();
        
        // Set up panel
        setLayout(new BorderLayout(0, 0));
        setBorder(new LineBorder(BORDER_COLOR, 1));
        setBackground(DAYS_BG);
        setDoubleBuffered(true); // Ensure double buffering for smoother rendering
        
        // Create content panel to hold both daysPanel and yearSelectionPanel
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(DAYS_BG);
        contentPanel.setDoubleBuffered(true);
        
        // Create components
        createHeaderPanel();
        createDaysPanel();
        createYearSelectionPanel();
        
        // Add content panel to main panel
        add(contentPanel, BorderLayout.CENTER);
        
        // Set up animation timer with adaptive timing based on system performance
        int adaptiveStepDuration = calculateAdaptiveTiming();
        animationTimer = new javax.swing.Timer(adaptiveStepDuration, e -> {
            animationStep++;
            if (animationStep >= ANIMATION_STEPS) {
                animationStep = 0;
                
                // Update displayed month after animation completes
                synchronized (displayedMonth) {
                    if (animationDirection < 0) {
                        displayedMonth.add(Calendar.MONTH, -1);
                    } else if (animationDirection > 0) {
                        displayedMonth.add(Calendar.MONTH, 1);
                    }
                    updateCalendarDisplay();
                }
                
                animating.set(false);
                animationTimer.stop();
                // Use more targeted repaint
                repaint(0, headerPanel.getHeight(), getWidth(), getHeight() - headerPanel.getHeight());
            } else {
                // Use more targeted repaint
                repaint(0, headerPanel.getHeight(), getWidth(), getHeight() - headerPanel.getHeight());
            }
        });
        
        // Set up hover debounce timer with more targeted repaint
        hoverDebounceTimer = new javax.swing.Timer(HOVER_DEBOUNCE_DELAY, e -> {
            // Use more targeted repaint for hover effects
            repaint(0, headerPanel.getHeight(), getWidth(), getHeight() - headerPanel.getHeight());
            hoverDebounceTimer.stop();
        });
        hoverDebounceTimer.setRepeats(false);
        
        // Initial update
        updateCalendarDisplay();
        
        // Set preferred size to ensure visibility
        setPreferredSize(new Dimension(400, 350));
    }
    
    /**
     * Create the header panel with month/year display and navigation buttons.
     */
    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create month/year label
        monthYearLabel = new JLabel("", SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        monthYearLabel.setForeground(HEADER_TEXT);
        
        // Create navigation buttons
        prevButton = createNavigationButton("◀");
        nextButton = createNavigationButton("▶");
        yearButton = createNavigationButton("▼");
        
        // Add action listeners
        prevButton.addActionListener(e -> {
            if (!animating.get() && !yearSelectionVisible.get()) {
                animating.set(true);
                animationDirection = -1;
                animationStep = 0;
                animationTimer.start();
            }
        });
        
        nextButton.addActionListener(e -> {
            if (!animating.get() && !yearSelectionVisible.get()) {
                animating.set(true);
                animationDirection = 1;
                animationStep = 0;
                animationTimer.start();
            }
        });
        
        yearButton.addActionListener(e -> {
            toggleYearSelection();
        });
        
        // Create button panels
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftButtonPanel.setOpaque(false);
        leftButtonPanel.add(prevButton);
        
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightButtonPanel.setOpaque(false);
        rightButtonPanel.add(yearButton);
        rightButtonPanel.add(nextButton);
        
        // Add components to header panel
        headerPanel.add(leftButtonPanel, BorderLayout.WEST);
        headerPanel.add(monthYearLabel, BorderLayout.CENTER);
        headerPanel.add(rightButtonPanel, BorderLayout.EAST);
        
        // Add header panel to main panel
        add(headerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Create a navigation button with hover effects.
     */
    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setForeground(HEADER_TEXT);
        button.setBackground(HEADER_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(SELECTED_BG);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(HEADER_TEXT);
            }
        });
        
        return button;
    }
    
    /**
     * Create the days panel with day names and day cells.
     */
    private void createDaysPanel() {
        daysPanel = new JPanel(new BorderLayout(0, 0));
        daysPanel.setBackground(DAYS_BG);
        
        // Create day names panel
        JPanel dayNamesPanel = new JPanel(new GridLayout(1, 7));
        dayNamesPanel.setBackground(DAY_NAMES_BG);
        dayNamesPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        // Add day name labels
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : dayNames) {
            JLabel label = new JLabel(dayName, SwingConstants.CENTER);
            label.setFont(new Font("Dialog", Font.BOLD, 12));
            label.setForeground(HEADER_TEXT);
            dayNamesPanel.add(label);
        }
        
        // Create days grid panel
        JPanel daysGridPanel = new JPanel(new GridLayout(6, 7, 2, 2));
        daysGridPanel.setBackground(BORDER_COLOR); // Use border color for grid lines
        daysGridPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        
        // Create day panels and labels
        dayPanels = new JPanel[6][7];
        dayLabels = new JLabel[6][7];
        
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                // Create panel for this day
                JPanel dayPanel = new JPanel(new BorderLayout());
                dayPanel.setBackground(DAYS_BG);
                dayPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
                dayPanel.setPreferredSize(new Dimension(30, 30)); // Ensure minimum size
                
                // Create label for this day
                JLabel dayLabel = new JLabel("", SwingConstants.CENTER);
                dayLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                dayLabel.setForeground(TEXT_COLOR);
                
                // Add label to panel
                dayPanel.add(dayLabel, BorderLayout.CENTER);
                
                // Add panel to grid
                daysGridPanel.add(dayPanel);
                
                // Store references
                dayPanels[row][col] = dayPanel;
                dayLabels[row][col] = dayLabel;
                
                // Add mouse listeners for hover and selection effects
                final int finalRow = row;
                final int finalCol = col;
                
                dayPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!animating.get() && !yearSelectionVisible.get()) {
                            hoveredRow = finalRow;
                            hoveredCol = finalCol;
                            // Use debounce timer instead of direct repaint
                            if (!hoverDebounceTimer.isRunning()) {
                                hoverDebounceTimer.restart();
                            }
                        }
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!animating.get() && !yearSelectionVisible.get()) {
                            hoveredRow = -1;
                            hoveredCol = -1;
                            // Use debounce timer instead of direct repaint
                            if (!hoverDebounceTimer.isRunning()) {
                                hoverDebounceTimer.restart();
                            }
                        }
                    }
                    
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!animating.get() && !yearSelectionVisible.get()) {
                            // Get the day number from the label
                            String dayText = dayLabels[finalRow][finalCol].getText();
                            if (!dayText.isEmpty()) {
                                try {
                                    int day = Integer.parseInt(dayText);
                                    
                                    // Update selected date
                                    Calendar newDate = (Calendar) displayedMonth.clone();
                                    
                                    // Improved logic for determining if day belongs to previous/next month
                                    boolean isPreviousMonth = false;
                                    boolean isNextMonth = false;
                                    
                                    // Check if this is a day from the previous month
                                    if (finalRow == 0) {
                                        Calendar firstDayOfMonth = (Calendar) displayedMonth.clone();
                                        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
                                        int firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1;
                                        
                                        if (finalCol < firstDayOfWeek) {
                                            isPreviousMonth = true;
                                        }
                                    }
                                    
                                    // Check if this is a day from the next month
                                    if (!isPreviousMonth) {
                                        int daysInMonth = displayedMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
                                        Calendar firstDayOfMonth = (Calendar) displayedMonth.clone();
                                        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
                                        int firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1;
                                        
                                        int dayPosition = finalRow * 7 + finalCol;
                                        int firstDayPosition = firstDayOfWeek;
                                        int lastDayPosition = firstDayPosition + daysInMonth - 1;
                                        
                                        if (dayPosition > lastDayPosition) {
                                            isNextMonth = true;
                                        }
                                    }
                                    
                                    // Adjust month if day is from previous/next month
                                    if (isPreviousMonth) {
                                        newDate.add(Calendar.MONTH, -1);
                                    } else if (isNextMonth) {
                                        newDate.add(Calendar.MONTH, 1);
                                    }
                                    
                                    newDate.set(Calendar.DAY_OF_MONTH, day);
                                    selectedDate = newDate;
                                    
                                    // Update displayed month if necessary
                                    if (isPreviousMonth) {
                                        synchronized (displayedMonth) {
                                            displayedMonth.add(Calendar.MONTH, -1);
                                            updateCalendarDisplay();
                                        }
                                    } else if (isNextMonth) {
                                        synchronized (displayedMonth) {
                                            displayedMonth.add(Calendar.MONTH, 1);
                                            updateCalendarDisplay();
                                        }
                                    } else {
                                        // Just update selection
                                        selectedRow = finalRow;
                                        selectedCol = finalCol;
                                        repaint();
                                    }
                                    
                                    // Fire property change event
                                    firePropertyChange("calendar", null, selectedDate);
                                } catch (NumberFormatException ex) {
                                    // Handle invalid day text
                                    System.err.println("Invalid day format: " + dayText);
                                }
                            }
                        }
                    }
                });
            }
        }
        
        // Add components to days panel
        daysPanel.add(dayNamesPanel, BorderLayout.NORTH);
        daysPanel.add(daysGridPanel, BorderLayout.CENTER);
        
        // Add days panel to content panel
        contentPanel.add(daysPanel, "days");
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "days");
    }
    
    /**
     * Create the year selection panel.
     */
    private void createYearSelectionPanel() {
        yearSelectionPanel = new JPanel();
        yearSelectionPanel.setLayout(new BorderLayout());
        yearSelectionPanel.setBackground(DAYS_BG);
        
        // Create year selection grid
        JPanel yearGrid = new JPanel(new GridLayout(5, 4, 5, 5));
        yearGrid.setBackground(BORDER_COLOR);
        yearGrid.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Get current year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        
        // Create year buttons
        for (int i = currentYear - 10; i <= currentYear + 9; i++) {
            final int year = i;
            JButton yearButton = new JButton(String.valueOf(year));
            yearButton.setFont(new Font("Dialog", Font.PLAIN, 14));
            yearButton.setForeground(TEXT_COLOR);
            yearButton.setBackground(DAYS_BG);
            yearButton.setBorderPainted(false);
            yearButton.setFocusPainted(false);
            yearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Highlight current year
            if (year == currentYear) {
                yearButton.setBackground(TODAY_BG);
                yearButton.setFont(new Font("Dialog", Font.BOLD, 14));
            }
            
            // Highlight selected year
            if (year == selectedDate.get(Calendar.YEAR)) {
                yearButton.setBackground(SELECTED_BG);
            }
            
            // Add hover effect
            yearButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (year != selectedDate.get(Calendar.YEAR) && year != currentYear) {
                        yearButton.setBackground(HOVER_BG);
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (year != selectedDate.get(Calendar.YEAR) && year != currentYear) {
                        yearButton.setBackground(DAYS_BG);
                    }
                }
            });
            
            // Add action listener
            yearButton.addActionListener(e -> {
                // Update displayed month
                synchronized (displayedMonth) {
                    displayedMonth.set(Calendar.YEAR, year);
                    updateCalendarDisplay();
                }
                
                // Hide year selection panel
                toggleYearSelection();
            });
            
            yearGrid.add(yearButton);
        }
        
        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Dialog", Font.BOLD, 14));
        closeButton.setForeground(TEXT_COLOR);
        closeButton.setBackground(HEADER_BG);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> toggleYearSelection());
        
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        closePanel.setBackground(HEADER_BG);
        closePanel.add(closeButton);
        
        // Add components to year selection panel
        yearSelectionPanel.add(yearGrid, BorderLayout.CENTER);
        yearSelectionPanel.add(closePanel, BorderLayout.SOUTH);
        
        // Add year selection panel to content panel
        contentPanel.add(yearSelectionPanel, "yearSelection");
    }
    
    /**
     * Toggle the year selection panel visibility.
     */
    private void toggleYearSelection() {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        
        if (yearSelectionVisible.get()) {
            cl.show(contentPanel, "days");
            yearSelectionVisible.set(false);
        } else {
            // Recreate year selection panel to update with current year
            contentPanel.remove(yearSelectionPanel);
            createYearSelectionPanel();
            cl.show(contentPanel, "yearSelection");
            yearSelectionVisible.set(true);
        }
    }
    
    /**
     * Update the calendar display with the current month.
     */
    private void updateCalendarDisplay() {
        // Update month/year label
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
        monthYearLabel.setText(monthYearFormat.format(displayedMonth.getTime()));
        
        // Get first day of month and total days
        Calendar firstDayOfMonth = (Calendar) displayedMonth.clone();
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1; // 0 = Sunday
        int daysInMonth = displayedMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // Get days in previous month
        Calendar prevMonth = (Calendar) displayedMonth.clone();
        prevMonth.add(Calendar.MONTH, -1);
        int daysInPrevMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // Clear all day labels
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                dayLabels[row][col].setText("");
                dayPanels[row][col].setBackground(DAYS_BG);
                
                // Set weekend background
                if (col == 0 || col == 6) {
                    dayPanels[row][col].setBackground(WEEKEND_BG);
                }
            }
        }
        
        // Fill days from previous month
        for (int i = 0; i < firstDayOfWeek; i++) {
            int day = daysInPrevMonth - firstDayOfWeek + i + 1;
            dayLabels[0][i].setText(String.valueOf(day));
            dayLabels[0][i].setForeground(OTHER_MONTH_TEXT);
        }
        
        // Fill days from current month
        int row = 0;
        int col = firstDayOfWeek;
        
        for (int day = 1; day <= daysInMonth; day++) {
            dayLabels[row][col].setText(String.valueOf(day));
            dayLabels[row][col].setForeground(TEXT_COLOR);
            
            // Highlight today
            Calendar today = Calendar.getInstance();
            if (displayedMonth.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                displayedMonth.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                day == today.get(Calendar.DAY_OF_MONTH)) {
                dayPanels[row][col].setBackground(TODAY_BG);
            }
            
            // Highlight selected date
            if (displayedMonth.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                displayedMonth.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                day == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                dayPanels[row][col].setBackground(SELECTED_BG);
                selectedRow = row;
                selectedCol = col;
            }
            
            // Move to next cell
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
        
        // Fill days from next month
        int nextMonthDay = 1;
        while (row < 6) {
            dayLabels[row][col].setText(String.valueOf(nextMonthDay));
            dayLabels[row][col].setForeground(OTHER_MONTH_TEXT);
            
            nextMonthDay++;
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }
    
    /**
     * Get the selected date as a Calendar object.
     * 
     * @return The selected date
     */
    public Calendar getCalendar() {
        return (Calendar) selectedDate.clone();
    }
    
    /**
     * Get the selected date as a Date object.
     * 
     * @return The selected date as a java.util.Date
     */
    public java.util.Date getSelectedDate() {
        return selectedDate.getTime();
    }
    
    /**
     * Get the selected date formatted as a string.
     * 
     * @return The formatted date string
     */
    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(selectedDate.getTime());
    }
    
    /**
     * Set the selected date.
     * 
     * @param date The date to select
     */
    public void setSelectedDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        selectedDate = calendar;
        
        // Update displayed month to match selected date
        displayedMonth.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR));
        displayedMonth.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH));
        
        updateCalendarDisplay();
        repaint();
        
        // Fire property change event
        firePropertyChange("calendar", null, selectedDate);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Apply hover effect if a cell is being hovered over
        if (hoveredRow >= 0 && hoveredCol >= 0 && !animating.get() && !yearSelectionVisible.get()) {
            // Get the day panel being hovered over
            JPanel hoveredPanel = dayPanels[hoveredRow][hoveredCol];
            
            // Only apply hover effect if the panel is visible and contains a day
            if (hoveredPanel.isVisible() && !dayLabels[hoveredRow][hoveredCol].getText().isEmpty()) {
                // Save original background color
                Color originalColor = hoveredPanel.getBackground();
                
                // Apply hover effect only if not already selected
                if (!(hoveredRow == selectedRow && hoveredCol == selectedCol)) {
                    hoveredPanel.setBackground(HOVER_BG);
                }
                
                // Repaint the specific panel
                hoveredPanel.repaint();
            }
        }
        
        // Only paint animation if active
        if (animating.get() && !yearSelectionVisible.get()) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Get content area
            int x = 0;
            int y = headerPanel.getHeight();
            int width = getWidth();
            int height = getHeight() - headerPanel.getHeight();
            
            // Calculate animation progress (0.0 to 1.0)
            float progress = (float) animationStep / ANIMATION_STEPS;
            
            // Create offscreen images for current and next/prev month
            BufferedImage currentMonth = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            BufferedImage otherMonth = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D currentG = currentMonth.createGraphics();
            Graphics2D otherG = otherMonth.createGraphics();
            
            // Enable anti-aliasing
            currentG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            otherG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw current month
            daysPanel.paint(currentG);
            
            // Draw next/prev month (temporarily modify displayedMonth)
            synchronized (displayedMonth) {
                // Save current state
                Calendar savedMonth = (Calendar) displayedMonth.clone();
                
                // Modify to show next/prev month
                if (animationDirection < 0) {
                    displayedMonth.add(Calendar.MONTH, -1);
                } else {
                    displayedMonth.add(Calendar.MONTH, 1);
                }
                
                // Update display for other month
                updateCalendarDisplay();
                
                // Draw other month
                daysPanel.paint(otherG);
                
                // Restore original state
                displayedMonth = savedMonth;
                updateCalendarDisplay();
            }
            
            // Clean up graphics contexts
            currentG.dispose();
            otherG.dispose();
            
            // Calculate slide positions
            int slideOffset = (int) (width * progress) * animationDirection;
            
            // Draw current month sliding out
            g2d.drawImage(currentMonth, x - slideOffset, y, null);
            
            // Draw other month sliding in
            if (animationDirection < 0) {
                g2d.drawImage(otherMonth, x - width + slideOffset, y, null);
            } else {
                g2d.drawImage(otherMonth, x + width - slideOffset, y, null);
            }
            
            g2d.dispose();
        }
    }
}
