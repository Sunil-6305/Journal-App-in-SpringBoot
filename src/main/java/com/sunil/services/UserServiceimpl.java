package com.sunil.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sunil.entity.User;
import com.sunil.repo.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service // या @Service
@Slf4j
public class UserServiceimpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    public UserServiceimpl(PasswordEncoder passwordEncoder) {
	this.passwordEncoder = passwordEncoder;
    }

    // private static final org.slf4j.Logger logger =
    // LoggerFactory.getLogger(UserService.class);Add commentMore actions

    public User saveUEntry(User user) {
	try {
	    if (!user.getPassword().startsWith("$2a$")) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	    }

	    // Set default role
	    user.setRoles(Arrays.asList("USER"));

	    // Save and return saved user
	    User savedUser = userRepo.save(user);
	    return savedUser;

	} catch (Exception e) {
	    log.error("Error occurred while saving user: {}", e.getMessage(), e);
	    return null;
	}
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
	    return org.springframework.security.core.userdetails.User.builder().username(user.getUserName())
		    .password(user.getPassword()).roles(user.getRoles().toArray(new String[0])).build();
	}

	throw new UsernameNotFoundException("User not found with username: " + username);
    }

}