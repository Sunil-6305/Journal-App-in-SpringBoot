package com.sunil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.sunil.entity.User;
import com.sunil.repo.UserRepo;
import com.sunil.services.UserServiceimpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/user")
public class UserController {

    private final PasswordEncoder passwordEncoder;

	@Autowired
	private UserServiceimpl userService;

	@Autowired
	private UserRepo userRepo;

    UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

	
	/*
	 * ===============================================================
	 * ✏️ 3.1: Update User
	 * ✅ Purpose: जो currently logged-in user है, उसकी details update करना
	 * ✅ URL: PUT /user
	 * ✅ Access: 🔒 Authenticated users only (Based on logged-in username)
	 * ✅ Input: RequestBody में नया User object (updated data)
	 * ✅ Logic:
	 *    - Authentication से username निकाला जाता है
	 *    - उसी username से database में user को fetch किया जाता है
	 *    - फिर updated username और password से update किया जाता है
	 * ===============================================================
	 */
	@PutMapping
	public ResponseEntity<?> update_User(@RequestBody User user) {

		// 🔐 Get currently logged-in user’s username
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();

		// 🔎 Fetch user from DB by username
		User userInDb = userService.findByUserName(userName);

		if (userInDb != null) {
			// ✏️ Update details
			userInDb.setUserName(user.getUserName());
			userInDb.setPassword(passwordEncoder.encode(user.getPassword())); // ⚠️ Encrypt before saving (if needed)
			 
			
			userService.saveUEntry(userInDb);
		}

		// 🔁 No Content means successful update with no body
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/*
	 * ===============================================================
	 * 🗑️ 3.2: Delete User
	 * ✅ Purpose: Logged-in user को delete करना
	 * ✅ URL: DELETE /user
	 * ✅ Access: 🔒 Authenticated users only
	 * ✅ Input: RequestBody में user object (हालांकि username auth से लिया जा रहा है)
	 * ✅ Logic:
	 *    - Logged-in user का username निकालो
	 *    - Repo से उसे delete करो
	 * ===============================================================
	 */
	@DeleteMapping
	public ResponseEntity<?> deleteUserById(@RequestBody User user) {

		// 🔐 Get logged-in user’s username
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 🗑️ Delete from DB using username
		userRepo.deleteByUserName(authentication.getName());


		// 🔁 Return success (204 No Content)
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
