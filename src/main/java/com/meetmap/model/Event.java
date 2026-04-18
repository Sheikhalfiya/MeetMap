package com.meetmap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String category;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
    private LocalDateTime dateTime;
    
    private String venue;
    private String city;
    private String mapCoordinates;
    
    @Column(length = 1000)
    private String imageUrl;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean isTopEvent = false;

    public Event() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getMapCoordinates() { return mapCoordinates; }
    public void setMapCoordinates(String mapCoordinates) { this.mapCoordinates = mapCoordinates; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsTopEvent() { return isTopEvent; }
    public void setIsTopEvent(Boolean isTopEvent) { this.isTopEvent = isTopEvent; }
}
