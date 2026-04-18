package com.meetmap.controller;

import com.meetmap.model.Admin;
import com.meetmap.model.User;
import com.meetmap.repository.AdminRepository;
import com.meetmap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email already in use."));
        }
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            User user = userOptional.get();
            // Returning simple success response with user data instead of JWT for
            // simplicity
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("role", "USER");
            response.put("city", user.getCity());
            response.put("dateOfBirth", user.getDateOfBirth());
            response.put("areaOfInterest", user.getAreaOfInterest());
            response.put("ageGroup", user.getAgeGroup());
            response.put("phoneNumber", user.getPhoneNumber());

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials."));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }
        
        User existingUser = userOptional.get();
        if (updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
        if (updatedUser.getCity() != null) existingUser.setCity(updatedUser.getCity());
        if (updatedUser.getDateOfBirth() != null) existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        if (updatedUser.getAreaOfInterest() != null) existingUser.setAreaOfInterest(updatedUser.getAreaOfInterest());
        if (updatedUser.getAgeGroup() != null) existingUser.setAgeGroup(updatedUser.getAgeGroup());
        if (updatedUser.getPhoneNumber() != null) existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        
        User savedUser = userRepository.save(existingUser);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedUser.getId());
        response.put("name", savedUser.getName());
        response.put("email", savedUser.getEmail());
        response.put("role", "USER");
        response.put("city", savedUser.getCity());
        response.put("dateOfBirth", savedUser.getDateOfBirth());
        response.put("areaOfInterest", savedUser.getAreaOfInterest());
        response.put("ageGroup", savedUser.getAgeGroup());
        response.put("phoneNumber", savedUser.getPhoneNumber());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        // Hardcode a default admin logic or allow them from admin table
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);

        // Let's ensure a default admin exists
        if (adminRepository.count() == 0 && email.equals("admin@meetmap.com") && password.equals("admin123")) {
            Admin admin = new Admin();
            admin.setName("Super Admin");
            admin.setEmail("admin@meetmap.com");
            admin.setPassword("admin123");
            adminRepository.save(admin);
            adminOptional = Optional.of(admin);
        }

        if (adminOptional.isPresent() && adminOptional.get().getPassword().equals(password)) {
            Admin admin = adminOptional.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", admin.getId());
            response.put("name", admin.getName());
            response.put("email", admin.getEmail());
            response.put("role", "ADMIN");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid admin credentials."));
    }
}
