package com.meetmap.controller;

import com.meetmap.model.Event;
import com.meetmap.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<Event> getAllEvents(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {

        List<Event> events = eventRepository.findAll();

        if (city != null && !city.isEmpty()) {
            events = events.stream().filter(e -> e.getCity() != null && e.getCity().equalsIgnoreCase(city)).toList();
        }
        if (category != null && !category.isEmpty()) {
            events = events.stream().filter(e -> e.getCategory() != null && e.getCategory().equalsIgnoreCase(category))
                    .toList();
        }
        if (search != null && !search.isEmpty()) {
            events = events.stream()
                    .filter(e -> e.getTitle() != null && e.getTitle().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        return events;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin endpoints
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return eventRepository.findById(id).map(event -> {
            event.setTitle(eventDetails.getTitle());
            event.setDescription(eventDetails.getDescription());
            event.setCategory(eventDetails.getCategory());
            event.setPrice(eventDetails.getPrice());
            event.setDateTime(eventDetails.getDateTime());
            event.setVenue(eventDetails.getVenue());
            event.setCity(eventDetails.getCity());
            event.setMapCoordinates(eventDetails.getMapCoordinates());
            event.setImageUrl(eventDetails.getImageUrl());
            if (eventDetails.getIsTopEvent() != null) {
                event.setIsTopEvent(eventDetails.getIsTopEvent());
            }
            Event updatedEvent = eventRepository.save(event);
            return ResponseEntity.ok(updatedEvent);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
