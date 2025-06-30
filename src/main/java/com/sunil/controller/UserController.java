package com.sunil.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sunil.entity.User;
import com.sunil.services.UserServiceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceimpl userServiceimpl;

    /**
     * ================================================================
     * ✅ [POST] /journal
     * ---------------------------------------------------------------
     * 🔹 Purpose:
     *     - Naye User ko create karne ke liye use hota hai.
     *     - Agar user ke saath journalEntries bhi aayein to wo bhi save hoti hain.
     *
     * 🔹 Flow:
     *     1. @RequestBody se User object JSON ke form me input liya jata hai.
     *     2. Service layer ke through User ko database me save kiya jata hai.
     *
     * 🔹 Returns:
     *     - 201 CREATED: Agar User successfully save ho jaye.
     *     - 400 BAD REQUEST: Agar koi error aa jaye saving ke dauraan.
     * ================================================================
     */
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody User user) {
        try {
            userServiceimpl.saveUEntry(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * ================================================================
     * ✅ [GET] /journal
     * ---------------------------------------------------------------
     * 🔹 Purpose:
     *     - Saare Users ko list ke form me fetch karna.
     *
     * 🔹 Flow:
     *     1. Service layer ke method getAllUser() se user list fetch hoti hai.
     *     2. Agar list non-empty hai to 200 OK return hota hai.
     *     3. Agar list empty hai to 404 Not Found return hota hai.
     *
     * 🔹 Returns:
     *     - 200 OK + List<User>: Agar users mil jayein.
     *     - 404 NOT FOUND: Agar koi user nahi mila.
     * ================================================================
     */
    @GetMapping
    public ResponseEntity<?> getSimpalAll() {
        List<User> allUsers = userServiceimpl.getAllUser();
        if (allUsers != null && !allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * ================================================================
     * ✅ [GET] /journal/id/{myId}
     * ---------------------------------------------------------------
     * 🔹 Purpose:
     *     - Specific ID ke user ko fetch karna.
     *
     * 🔹 Flow:
     *     1. URL ke path se ID fetch hota hai.
     *     2. Service layer ke through user ko search kiya jata hai.
     *     3. Agar user mil jaye to return karo 200 OK.
     *     4. Agar user nahi mila to return karo 404 NOT FOUND.
     *
     * 🔹 Returns:
     *     - 200 OK + User Object: Agar user mila.
     *     - 404 NOT FOUND: Agar user nahi mila.
     * ================================================================
     */
    @GetMapping("id/{myId}")
    public ResponseEntity<?> getUserId(@PathVariable int myId) {
        Optional<User> user = userServiceimpl.findByID(myId);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found with ID: " + myId, HttpStatus.NOT_FOUND);
    }

    /**
     * ================================================================
     * ✅ [DELETE] /journal/id/{myId}
     * ---------------------------------------------------------------
     * 🔹 Purpose:
     *     - Kisi bhi user ko uske ID ke through delete karna.
     *
     * 🔹 Flow:
     *     1. PathVariable se ID liya jaata hai.
     *     2. Service ke method se user exist karta hai ya nahi, check hota hai.
     *     3. Agar user milta hai to delete karo.
     *     4. Agar nahi milta to 404 error bhejna.
     *
     * 🔹 Returns:
     *     - 200 OK: Agar delete successful ho jaye.
     *     - 404 NOT FOUND: Agar user nahi mila.
     * ================================================================
     */
    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> DeleteUser(@PathVariable int myId) {
        Optional<User> user = userServiceimpl.findByID(myId);
        if (user.isPresent()) {
            userServiceimpl.deletById(myId);
            return ResponseEntity.ok("✅ Deleted Ueser with ID: " + myId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ user not found with ID: " + myId);
        }
    }

    /**
     * ================================================================
     * ✅ [PUT] /journal/{userName}
     * ---------------------------------------------------------------
     * 🔹 Purpose:
     *     - Username ke basis par user ka password update karna.
     *
     * 🔹 Flow:
     *     1. @PathVariable se username liya jata hai.
     *     2. Us user ko DB se fetch kiya jata hai.
     *     3. Agar user milta hai:
     *          - Password ko update karo.
     *          - Save the updated user.
     *     4. Agar user nahi mila:
     *          - 404 error return karo.
     *
     * 🔹 Returns:
     *     - 204 NO CONTENT: Agar update successful ho jaye.
     *     - 404 NOT FOUND: Agar user exist nahi karta.
     * ================================================================
     */
    @PutMapping("/{userName}")
    public ResponseEntity<?> update_User(@RequestBody User user, @PathVariable String userName) {
        User userInDb = userServiceimpl.findByUserName(userName);
        if (userInDb != null) {
            userInDb.setPassword(user.getPassword()); // ✅ Sirf password update ho raha hai
            userServiceimpl.saveUEntry(userInDb);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("User not found with username: " + userName, HttpStatus.NOT_FOUND);
    }

}
