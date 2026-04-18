package com.meetmap.controller;

import com.meetmap.model.Booking;
import com.meetmap.model.Event;
import com.meetmap.model.User;
import com.meetmap.repository.BookingRepository;
import com.meetmap.repository.EventRepository;
import com.meetmap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Long eventId = Long.valueOf(request.get("eventId").toString());
        Integer tickets = Integer.valueOf(request.get("tickets").toString());
        Double discount = request.containsKey("discount") ? Double.valueOf(request.get("discount").toString()) : 0.0;

        User user = userRepository.findById(userId).orElse(null);
        Event event = eventRepository.findById(eventId).orElse(null);

        if (user == null || event == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "User or event not found."));
        }

        Double totalPrice = event.getPrice() * tickets;
        if (discount > 0) {
            totalPrice = totalPrice - (totalPrice * (discount / 100.0));
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setTickets(tickets);
        booking.setTotalPrice(totalPrice);
        booking.setBookingDate(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(savedBooking);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @GetMapping("/admin")
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
