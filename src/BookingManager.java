package com.lancaster.musicapp;

import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.*;

/**
 * BookingManager - A singleton class to manage bookings
 * This class stores bookings in a data structure and provides methods to approve or deny them
 */
public class BookingManager {
    private static BookingManager instance;
    private List<BookingEntry> bookings;
    
    // Private constructor for singleton pattern
    private BookingManager() {
        bookings = new ArrayList<>();
    }
    
    /**
     * Get the singleton instance of BookingManager
     */
    public static synchronized BookingManager getInstance() {
        if (instance == null) {
            instance = new BookingManager();
        }
        return instance;
    }
    
    /**
     * Add a booking to the manager
     * @param booking The booking to add
     */
    public void addBooking(BookingEntry booking) {
        bookings.add(booking);
    }
    
    /**
     * Get all bookings
     * @return List of all bookings
     */
    public List<BookingEntry> getAllBookings() {
        return new ArrayList<>(bookings);
    }
    
    /**
     * Approve a booking by ID
     * @param bookingId The ID of the booking to approve
     * @return true if the booking was found and approved, false otherwise
     */
    public boolean approveBooking(String bookingId) {
        for (BookingEntry booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                booking.setStatus("Approved");
                updateBookingInDatabase(booking);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Deny a booking by ID
     * @param bookingId The ID of the booking to deny
     * @return true if the booking was found and denied, false otherwise
     */
    public boolean denyBooking(String bookingId) {
        for (BookingEntry booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                booking.setStatus("Denied");
                updateBookingInDatabase(booking);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Update a booking's status in the database
     * @param booking The booking to update
     */
    private void updateBookingInDatabase(BookingEntry booking) {
        try (Connection conn = Database.connection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE BOOKINGS SET STATUS = ? WHERE BOOKING_ID = ?")) {
            
            ps.setString(1, booking.getStatus());
            ps.setString(2, booking.getBookingId());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load all bookings from the database
     */
    public void loadBookingsFromDatabase() {
        try (Connection conn = Database.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKINGS")) {
            
            bookings.clear();
            
            while (rs.next()) {
                BookingEntry booking = new BookingEntry(
                    rs.getString("BOOKING_ID"),
                    rs.getString("CUSTOMER_BOOKED"),
                    rs.getString("PAYMENT_TYPE"),
                    rs.getString("TICKETS_BOOKED"),
                    rs.getDate("BOOKING_DATE"),
                    rs.getString("ROWSHELD"),
                    rs.getInt("TOTAL_COST"),
                    rs.getString("STATUS") != null ? rs.getString("STATUS") : "Pending"
                );
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * BookingEntry - A class to represent a booking in the system
     */
    public static class BookingEntry {
        private String bookingId;
        private String customerName;
        private String paymentType;
        private String ticketsBooked;
        private java.util.Date bookingDate;
        private String notes;
        private int totalCost;
        private String status;
        
        public BookingEntry(String bookingId, String customerName, String paymentType, 
                           String ticketsBooked, java.util.Date bookingDate, String notes, 
                           int totalCost, String status) {
            this.bookingId = bookingId;
            this.customerName = customerName;
            this.paymentType = paymentType;
            this.ticketsBooked = ticketsBooked;
            this.bookingDate = bookingDate;
            this.notes = notes;
            this.totalCost = totalCost;
            this.status = status != null ? status : "Pending";
        }
        
        // Getters and setters
        public String getBookingId() { return bookingId; }
        public void setBookingId(String bookingId) { this.bookingId = bookingId; }
        
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        
        public String getPaymentType() { return paymentType; }
        public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
        
        public String getTicketsBooked() { return ticketsBooked; }
        public void setTicketsBooked(String ticketsBooked) { this.ticketsBooked = ticketsBooked; }
        
        public java.util.Date getBookingDate() { return bookingDate; }
        public void setBookingDate(java.util.Date bookingDate) { this.bookingDate = bookingDate; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        
        public int getTotalCost() { return totalCost; }
        public void setTotalCost(int totalCost) { this.totalCost = totalCost; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
            return "Booking: " + customerName + " (" + paymentType + "), " +
                   dateFormat.format(bookingDate) + ", " + ticketsBooked + 
                   ", Status: " + status;
        }
    }
}
