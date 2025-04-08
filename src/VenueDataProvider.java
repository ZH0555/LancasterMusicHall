package com.lancaster.musicapp;

import java.util.ArrayList;
import java.util.List;

/**
 * VenueDataProvider - A utility class to provide sample venue data
 */
public class VenueDataProvider {
    
    /**
     * Get a list of sample venues with their information
     */
    public static List<Venue> getSampleVenues() {
        List<Venue> venues = new ArrayList<>();
        
        // Main Hall
        venues.add(new Venue(
            "Main Concert Hall",
            500,
            new String[] {
                "Professional sound system",
                "Stage lighting",
                "Green room for performers",
                "Bar and catering services",
                "Accessible facilities",
                "Parking for 100 vehicles"
            },
            "Lancaster's Main Concert Hall is our premier venue, originally built in 1920 and completely renovated to provide modern amenities while preserving its classic charm and exceptional acoustics.\n\n" +
            "The hall features a spacious stage, professional sound and lighting systems, and flexible seating arrangements to accommodate various event types. Perfect for concerts, performances, and large gatherings.",
            "main_hall.jpg",
            new Venue.RateInfo(250.00, 1800.00, 3200.00, 9500.00)
        ));
        
        // Intimate Theater
        venues.add(new Venue(
            "Intimate Theater",
            150,
            new String[] {
                "Intimate setting with excellent acoustics",
                "Configurable lighting system",
                "Small backstage area",
                "Refreshment service available",
                "Wheelchair accessible",
                "Street parking nearby"
            },
            "Our Intimate Theater provides a close-knit atmosphere perfect for smaller performances, recitals, and theatrical productions. With excellent acoustics and configurable lighting, this space creates a special connection between performers and audience.\n\n" +
            "The theater includes comfortable seating and can be arranged in various configurations to suit your specific needs.",
            "intimate_theater.jpg",
            new Venue.RateInfo(150.00, 1100.00, 1900.00, 5500.00)
        ));
        
        // Conference Room
        venues.add(new Venue(
            "Conference Room",
            80,
            new String[] {
                "Modern AV equipment",
                "High-speed internet",
                "Configurable seating",
                "Whiteboard and projection screen",
                "Coffee and refreshment service",
                "Business center services"
            },
            "The Conference Room is a versatile space ideal for business meetings, workshops, and small seminars. Equipped with modern audiovisual technology and high-speed internet, this room ensures your business events run smoothly.\n\n" +
            "The space can be configured in various layouts including boardroom, classroom, or theater style to accommodate your specific requirements.",
            "conference_room.jpg",
            new Venue.RateInfo(75.00, 500.00, 800.00, 2800.00)
        ));
        
        // Rooftop Terrace
        venues.add(new Venue(
            "Rooftop Terrace",
            200,
            new String[] {
                "Panoramic city views",
                "Outdoor sound system",
                "Weather protection options",
                "Ambient lighting",
                "Bar service available",
                "Elevator access"
            },
            "Our stunning Rooftop Terrace offers breathtaking views of Lancaster and is perfect for receptions, parties, and special events. The open-air space features ambient lighting and an outdoor sound system.\n\n" +
            "Weather protection options are available, and the space can be transformed to suit various themes and occasions. The Rooftop Terrace is particularly popular during spring and summer months.",
            "rooftop_terrace.jpg",
            new Venue.RateInfo(200.00, 1500.00, 2800.00, 8000.00)
        ));
        
        // Rehearsal Studio
        venues.add(new Venue(
            "Rehearsal Studio",
            30,
            new String[] {
                "Soundproofed walls",
                "Basic PA system",
                "Piano available",
                "Mirrored wall",
                "Storage lockers",
                "24-hour access available"
            },
            "The Rehearsal Studio is a practical space designed for musicians, dancers, and performers to practice and prepare. With soundproofed walls and a basic PA system, it provides an ideal environment for creative development.\n\n" +
            "The studio includes a piano and mirrored wall, making it suitable for various types of rehearsals. Storage lockers are available for regular users, and 24-hour access can be arranged for long-term bookings.",
            "rehearsal_studio.jpg",
            new Venue.RateInfo(40.00, 280.00, 450.00, 1600.00)
        ));
        
        return venues;
    }
}
