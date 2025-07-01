package com.sunil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sunil.entity.User;
import com.sunil.services.UserServiceimpl;
 
 

@RestController
@RequestMapping("/admin") // 🧭 All routes prefixed with /admin
public class AdminController {

    @Autowired
    private UserServiceimpl userServiceimpl; // ✅ Interface Injection - avoids circular dependency

    // ✅ GET: All Users (Admin-only access if protected by Spring Security)
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUser() {
        List<User> allUser = userServiceimpl.getAllUser();

        if (allUser != null && !allUser.isEmpty()) {
            return new ResponseEntity<>(allUser, HttpStatus.OK);
        }

        return new ResponseEntity<>("❌ No users found", HttpStatus.NOT_FOUND);
    }

    // ✅ POST: Create Admin User
    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
	userServiceimpl.saveAdmin(user);
        return new ResponseEntity<>("✅ Admin user created successfully", HttpStatus.CREATED);
    }
}
