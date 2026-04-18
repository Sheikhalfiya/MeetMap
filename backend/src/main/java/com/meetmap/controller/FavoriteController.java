package com.meetmap.controller;

import com.meetmap.model.Event;
import com.meetmap.model.Favorite;
import com.meetmap.model.User;
import com.meetmap.repository.EventRepository;
import com.meetmap.repository.FavoriteRepository;
import com.meetmap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/user/{userId}")
    public List<Favorite> getUserFavorites(@PathVariable Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<?> toggleFavorite(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long eventId = request.get("eventId");

        Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndEventId(userId, eventId);

        if (existingFavorite.isPresent()) {
            favoriteRepository.delete(existingFavorite.get());
            return ResponseEntity.ok(Map.of("message", "Removed from favorites", "status", "removed"));
        } else {
            User user = userRepository.findById(userId).orElse(null);
            Event event = eventRepository.findById(eventId).orElse(null);

            if (user == null || event == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "User or event not found."));
            }

            Favorite f = new Favorite();
            f.setUser(user);
            f.setEvent(event);
            favoriteRepository.save(f);
            return ResponseEntity.ok(Map.of("message", "Added to favorites", "status", "added"));
        }
    }
}
