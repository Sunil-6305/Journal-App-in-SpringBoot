package com.sunil.controller;

import com.sunil.entity.JournalEntry;
import com.sunil.entity.User;
import com.sunil.services.JournalEnteryServiceimpl;
import com.sunil.services.UserServiceimpl;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
	User user = userServiceimpl.findByUserName(userName);
	if (user == null) {
	    return ResponseEntity.notFound().build();
	}
	List<JournalEntry> journalEntries = user.getJournalEntries();
	if (journalEntries != null && !journalEntries.isEmpty()) {
	    return ResponseEntity.ok(journalEntries);
	}
	return ResponseEntity.status(404).body("No journal entries found for user: " + userName);
    }

    // 🔹 POST: /journal/{userName}
    @PostMapping("/{userName}")
    public ResponseEntity<?> createEntryOfUser(@RequestBody JournalEntry journalEntry, @PathVariable String userName) {
	try {
	    
	    jenteryServiceimpl.saveEntryWithUser(journalEntry, userName);
	    return ResponseEntity.status(201).body(journalEntry);
	} catch (Exception e) {
	    return ResponseEntity.status(500).body("Error while creating journal entry");
	}
    }

     //🔹 GET: /journal/id/{id}
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
	Optional<JournalEntry> entry = jenteryServiceimpl.findByID(id);
	if (entry == null) {
	    return ResponseEntity.notFound().build();
	}
	return ResponseEntity.ok(entry);
    }

     //🔹 PUT: /journal/id/{userName}/{id}
    @PutMapping("/id/{userName}/{id}")
    public ResponseEntity<?> updateJEofUser(
	    @PathVariable String userName,
	    @PathVariable int id,
	    @RequestBody JournalEntry updatedEntry) {
	JournalEntry oldEntry = jenteryServiceimpl.findByIdAndUserName(id, userName);
	if (oldEntry != null) {
	    if (updatedEntry.getTitle() != null && !updatedEntry.getTitle().isBlank()) {
		oldEntry.setTitle(updatedEntry.getTitle());
	    }
	    if (updatedEntry.getContent() != null && !updatedEntry.getContent().isBlank()) {
		oldEntry.setContent(updatedEntry.getContent());
	    }
	    jenteryServiceimpl.updateEntry(oldEntry);
	    return ResponseEntity.ok(oldEntry);
	}
	return ResponseEntity.notFound().build();
    }

    
     //🔹 DELETE: /journal/id/{id}
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            jenteryServiceimpl.deleteById(id);
            return ResponseEntity.ok("Deleted Successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
