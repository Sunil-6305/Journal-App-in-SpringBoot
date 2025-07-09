package com.sunil.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;



import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sunil.entity.User;
import com.sunil.entity.GoogleTokenRequest;
import com.sunil.repo.UserRepo;
import com.sunil.utilis.JwtUtil;

import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/google")
public class GoogleAuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Value("${google.client.id}")
    private String clientId;

    @PostMapping("/login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleTokenRequest request) {
        // DEBUG: Set a breakpoint on the next line to inspect the request from Postman
        System.out.println("i am in DEBUG: Received GoogleTokenRequest: " + request);
        try {
            GoogleIdToken.Payload payload = verifyGoogleToken(request.getToken());
              System.out.println("DEBUG: GoogleIdToken.Payload: " + payload);  
            if (payload == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

            String email = payload.getEmail();
            User user = userRepo.findByUserName(email);

            if (user == null) {
                user = new User();
                user.setName((String) payload.get("name"));
                user.setEmail(email);
                user.setUserName((String) payload.get("name"));
                user.setProvider("Google");
                userRepo.save(user);
            }

            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            System.out.println("DEBUG: Exception during Google login: " + e.getMessage());  
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    public GoogleIdToken.Payload verifyGoogleToken(String idTokenString) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        return idToken != null ? idToken.getPayload() : null;
    }

}
