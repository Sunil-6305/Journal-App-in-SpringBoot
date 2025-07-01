package com.sunil.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sunil.entity.User;
import com.sunil.repo.UserRepo;

@Component // या @Service
public class UserServiceimpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    public void saveUEntry(User user) {

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

	if (user == null) {
	    throw new UsernameNotFoundException("User not found: " + username);
	}

	return new org.springframework.security.core.userdetails.User(user.getUserName(), // username
		user.getPassword(), // encoded password
		new ArrayList<>() // authorities (roles) – अभी खाली
	);
    }

}