package com.sunil.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sunil.entity.User;
import com.sunil.repo.UserRepo;

@Component
public class UserServiceimpl {
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

}