package com.sunil.controller;

import com.sunil.entity.JournalEntry;
import com.sunil.entity.User;
import com.sunil.services.JournalEnteryServiceimpl;
import com.sunil.services.UserServiceimpl;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private UserServiceimpl  userServiceimpl;

    @Autowired
    private JournalEnteryServiceimpl jenteryServiceimpl;

    // 🔹 GET: /journal/{userName}
    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {

        // 🔐 Step 1: Logged-in user ka username lo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName(); // e.g., "sitaji"

        // ✅ Step 2: Sirf wahi access kar sakta hai jo khud login hai
        if (!loggedInUsername.equals(userName)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("⛔ Access Denied: You can only view your own journal entries");
        }

        // 🔎 Step 3: Fetch user and entries
        User user = userServiceimpl.findByUserName(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ User not found: " + userName);
        }

        List<JournalEntry> journalEntries = user.getJournalEntries();
        if (journalEntries != null && !journalEntries.isEmpty()) {
            return ResponseEntity.ok(journalEntries);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("📭 No journal entries found for user: " + userName);
    }


    /// 🔹 POST: /journal/{userName}
    @PostMapping("/{userName}")
    public ResponseEntity<?> createEntryOfUser(@RequestBody JournalEntry journalEntry, @PathVariable String userName) {
        try {
            // 🔐 Step 1: Get logged-in user's username
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = auth.getName();

            // 🔒 Step 2: Allow only if logged-in user is same as path userName
            if (!loggedInUsername.equals(userName)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("⛔ Access Denied: You can only create entries for your own account");
            }

            // ✅ Step 3: Save entry
            jenteryServiceimpl.saveEntryWithUser(journalEntry, userName);
            return ResponseEntity.status(HttpStatus.CREATED).body(journalEntry);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error while creating journal entry: " + e.getMessage());
        }
    }


     //🔹 GET: /journal/id/{id}
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {

        // 🔐 Step 1: Logged-in user ka username nikalo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        // 🔎 Step 2: Entry database se lao
        Optional<JournalEntry> optionalEntry = jenteryServiceimpl.findByID(id);

        // ❌ Step 3: Agar entry exist nahi karti
        if (optionalEntry.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Entry not found with id: " + id);
        }

        JournalEntry entry = optionalEntry.get();

        // 🔒 Step 4: Check karo ki entry isi user ki hai
        String entryOwnerUsername = entry.getUser().getUserName();
        if (!loggedInUsername.equals(entryOwnerUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("⛔ Access Denied: You can only access your own entries.");
        }

        // ✅ Step 5: Entry return karo
        return ResponseEntity.ok(entry);
    }


 // 🔹 PUT: /journal/id/{userName}/{id}
    @Transactional 
    @PutMapping("/id/{userName}/{id}")
    public ResponseEntity<?> updateJEofUser(
            @PathVariable String userName,
            @PathVariable int id,
            @RequestBody JournalEntry updatedEntry) {

        // 🔐 Step 1: Logged-in user का username निकालो
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        // 🔒 Step 2: Check करो कि userName और logged-in username same हैं
        if (!loggedInUsername.equals(userName)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("⛔ Access Denied: You can only update your own entries.");
        }

        // 🔍 Step 3: Entry find करो userName और id से
        JournalEntry oldEntry = jenteryServiceimpl.findByIdAndUserName(id, userName);

        if (oldEntry != null) {
            if (updatedEntry.getTitle() != null && !updatedEntry.getTitle().isBlank()) {
                oldEntry.setTitle(updatedEntry.getTitle());
            }
            if (updatedEntry.getContent() != null && !updatedEntry.getContent().isBlank()) {
                oldEntry.setContent(updatedEntry.getContent());
            }

            // 💾 Step 4: Save updated entry
            jenteryServiceimpl.updateEntry(oldEntry);
            return ResponseEntity.ok(oldEntry);
        }

        // ❌ Entry ना मिले तो
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("❌ Journal entry not found for id: " + id);
    }


    
 // 🔹 DELETE: /journal/id/{id}
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            // 🔐 Step 1: Get logged-in user's username
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = auth.getName();

            // 🔍 Step 2: Find the entry
            Optional<JournalEntry> optionalEntry = jenteryServiceimpl.findByID(id);
            if (optionalEntry.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Entry not found with id: " + id);
            }

            JournalEntry entry = optionalEntry.get();

            // 🔒 Step 3: Check ownership
            String entryOwnerUsername = entry.getUser().getUserName();
            if (!loggedInUsername.equals(entryOwnerUsername)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("⛔ Access Denied: You can only delete your own entries.");
            }

            // ✅ Step 4: Delete the entry
            jenteryServiceimpl.deleteById(id);
            return ResponseEntity.ok("✅ Deleted Successfully");

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        }
    }


}
