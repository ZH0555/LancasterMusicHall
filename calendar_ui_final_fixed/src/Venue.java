package com.lancaster.musicapp;

/**
 * Venue class to hold information for each venue
 */
public class Venue {
    private String name;
    private int capacity;
    private String[] facilities;
    private String description;
    private String imagePath; // For future implementation with actual images
    private RateInfo rates;
    
    /**
     * Constructor for Venue
     */
    public Venue(String name, int capacity, String[] facilities, String description, String imagePath, RateInfo rates) {
        this.name = name;
        this.capacity = capacity;
        this.facilities = facilities;
        this.description = description;
        this.imagePath = imagePath;
        this.rates = rates;
    }
    
    // Getters
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public String[] getFacilities() { return facilities; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
    public RateInfo getRates() { return rates; }
    
    /**
     * Inner class to hold rate information
     */
    public static class RateInfo {
        private double hourlyRate;
        private double dailyRate;
        private double weekendRate;
        private double weeklyRate;
        
        public RateInfo(double hourlyRate, double dailyRate, double weekendRate, double weeklyRate) {
            this.hourlyRate = hourlyRate;
            this.dailyRate = dailyRate;
            this.weekendRate = weekendRate;
            this.weeklyRate = weeklyRate;
        }
        
        // Getters
        public double getHourlyRate() { return hourlyRate; }
        public double getDailyRate() { return dailyRate; }
        public double getWeekendRate() { return weekendRate; }
        public double getWeeklyRate() { return weeklyRate; }
    }
}
