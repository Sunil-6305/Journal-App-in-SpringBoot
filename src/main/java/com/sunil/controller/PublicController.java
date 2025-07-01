package com.sunil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunil.entity.User;
import com.sunil.services.UserServiceimpl;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserServiceimpl userService;

    PublicController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/health-check")
    public String healthCheck() { return "Ok"; }

    @PostMapping("/create-user")
    public User createUser(@RequestBody User user) {
        // 1. Password encode करो
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2. Save करो via service layer
          userService.saveUEntry(user);
	return user;
    }

}