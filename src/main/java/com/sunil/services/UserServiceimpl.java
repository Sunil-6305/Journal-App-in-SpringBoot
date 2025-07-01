package com.sunil.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sunil.entity.User;
import com.sunil.repo.UserRepo;

import lombok.NonNull;

@Component // या @Service
public class UserServiceimpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;

    UserServiceimpl(PasswordEncoder passwordEncoder) {
	this.passwordEncoder = passwordEncoder;
    }

    public void saveUEntry(User user) {

	userRepo.save(user);

    }

    public void saveAdmin(User user) {
	user.setPassword(passwordEncoder.encode(user.getPassword()));
	user.setRoles(Arrays.asList("USER", "ADMIN"));
	userRepo.save(user);

    }

    public List<User> getAllUser() {

	return userRepo.findAll();
    }

    public Optional<User> findByID(int id) {

	return userRepo.findById(id);

    }

    public void deletById(int id) {
	userRepo.deleteById(id);

    }

    public User findByUserName(String userName) {

	return userRepo.findByUserName(userName);
    }

    // 👇 Spring Security will call this for authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(username);

        if (user != null) {
            // ✅ Convert each role to SimpleGrantedAuthority with "ROLE_" prefix
            List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Add prefix manually
                .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                authorities
            );
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }


}