package com.sunil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.sunil.entity.LoginRequest;
import com.sunil.entity.User;
import com.sunil.repo.UserRepo;
import com.sunil.services.UserServiceimpl;
import com.sunil.utilis.ApiResponse;
import com.sunil.utilis.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    
        @Autowired
        private UserServiceimpl userServiceImpl;
    
        @Autowired
        private JwtUtil jwtUtilis;

    PublicController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }

    @GetMapping("/log-test")
    public String testLog() {
        log.info("🚀 Logging test working perfectly!");
        log.warn("⚠️ Warning log test");
        log.error("❌ Error log test"); // ✅ FIXED
        return "Log created";
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse> createUser(@RequestBody User user) {

        if (user.getUserName() == null || user.getPassword() == null || user.getUserName().isBlank() || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "❌ Username or password is empty", null));
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Save user
        User savedUser = userServiceImpl.saveUEntry(user);

        // Generate token for the user
        String token = jwtUtilis.generateToken(user);

        // Return response
        return ResponseEntity.ok(new ApiResponse(true, "✅ User created successfully", token));
    }
    
   

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userServiceImpl.findByUserName(loginRequest.getUserName());
            System.out.println("Raw Password: " + loginRequest.getPassword());
            System.out.println("Encoded Password in DB: " + user.getPassword());
            System.out.println("Password Match: " + passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));
         
           
            // Attempt authentication using Spring Security
            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
                )
            );

            // If authentication passes, load user details
            UserDetails userDetails = userServiceImpl.loadUserByUsername(loginRequest.getUserName());

            // Generate JWT token
            
            User fullUser = userServiceImpl.findByUserName(userDetails.getUsername());
            String jwt = jwtUtilis.generateToken(fullUser);


            return ResponseEntity.ok(new ApiResponse(true, "✅ Login successful", jwt));

        } catch (BadCredentialsException e) {
            log.error("❌ Login failed - Bad credentials", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Incorrect Username or Password");
        } catch (Exception e) {
            log.error("❌ Login failed - Other error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Server error during login");
        }
    }


    
}
