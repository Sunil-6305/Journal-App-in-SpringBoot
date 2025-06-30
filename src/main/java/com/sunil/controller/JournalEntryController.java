package com.sunil.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sunil.entity.JournalEntry;
import com.sunil.services.JournalEnteryServiceimpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEnteryServiceimpl jEntryService;

    /*
     * ✅ POST Request: /journal Is method se naya JournalEntry create hota hai.
     * 
     * @RequestBody ke through client se JSON data aata hai jo JournalEntry ban jata
     * hai. LocalDateTime.now() se current date-time set kiya jaata hai. Fir service
     * ke through database me save kiya jaata hai. 🔹 Returns: - ResponseEntity
     * object with status `201 Created` - Body: Saved JournalEntry object
     */
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry) {
	try {

	    jEntryService.saveJEntry(journalEntry);
	    return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);

	} catch (Exception e) {
	    return new ResponseEntity<>(journalEntry, HttpStatus.BAD_REQUEST);
	}
    }

    /*
     * ✅ GET Request: /journal Ye method saare JournalEntry objects ko list ke form
     * me return karta hai. Service layer ke through sabhi entries ko fetch kiya
     * jaata hai.
     */
    @GetMapping
    public ResponseEntity<?> getSimpalAll() {
	List<JournalEntry> allJe = jEntryService.getAllJe();
	if (allJe != null && !allJe.isEmpty()) {
	    return new ResponseEntity<>(allJe, HttpStatus.OK);
	}
	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * ✅ GET Request: /journal/id/{myId} Particular JournalEntry ko fetch karta hai
     ** 🔹 Flow:
     *  1. @PathVariable ke through client se URL me di gayi ID read hoti hai.
     *  2. Service layer (jEntryService.findByID) se entry fetch hoti hai as 	.
     *  3. Agar entry present hai → 200 OK ke saath return hoti hai.
     *  4. Agar entry nahi mili → 404 NOT FOUND return hota hai.
     */
    @GetMapping("id/{myId}")
    public ResponseEntity<?> getJournalEntrybyId(@PathVariable int myId) {
	Optional<JournalEntry> journalEntry = jEntryService.findByID(myId);

	if (journalEntry.isPresent()) {
		return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
	}

	return new ResponseEntity<>(journalEntry.get(), HttpStatus.NOT_FOUND);
    }

    /*  
     * 🔹 Flow:
     *  1. @PathVariable ke through URL se ObjectId liya jaata hai.
     *  2. Service layer (jEntryService.deletById) ke through entry delete hoti hai.
     *  3. Agar entry delete ho gayi to HTTP 204 (No Content) return kiya jaata hai.
     *
     * 🔹 Returns:
     *      - 204 NO_CONTENT → Jab entry successfully delete ho jaati hai.
     */
    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deljournalEntry(@PathVariable int myId) {
	Optional<JournalEntry> entry = jEntryService.findByID(myId);
	if (entry.isPresent()) {
	    jEntryService.deletById(myId);
	    return ResponseEntity.ok("Deleted entry with ID: " + myId);
	} else {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                         .body("❌ Entry not found with ID: " + myId);
	}
    }


    /*
     * ✅ [PUT] /id/{id}
     *
     * 🔹 Purpose:
     *     Yeh method existing JournalEntry ko update karta hai.
     *
     * 🔹 Flow:
     *   1. URL se ObjectId fetch kiya jaata hai (@PathVariable).
     *   2. jEntryService.findByID(...) se existing entry dhoonda jaata hai.
     *   3. Agar entry milti hai:
     *      - Title aur Content ko check karke update kiya jaata hai (agar null/blank na ho).
     *      - Fir updated entry ko save kiya jaata hai.
     *      - 200 OK ke saath updated object return hota hai.
     *   4. Agar entry nahi milti:
     *      - 404 NOT FOUND return hota hai.
     */
    @PutMapping("id/{id}")
    public ResponseEntity<?> updatJEById(@PathVariable int id, @RequestBody JournalEntry updatedEntry) {

        JournalEntry oldEntry = jEntryService.findByID(id).orElse(null);

        if (oldEntry != null) {
            // Title update if valid
            oldEntry.setTitle(
                updatedEntry.getTitle() != null && !updatedEntry.getTitle().isBlank()
                    ? updatedEntry.getTitle()
                    : oldEntry.getTitle()
            );

            // Content update if valid
            oldEntry.setContent(
                updatedEntry.getContent() != null && !updatedEntry.getContent().isBlank()
                    ? updatedEntry.getContent()
                    : oldEntry.getContent()
            );

            // Save updated entry
            jEntryService.saveJEntry(oldEntry);

            return new ResponseEntity<>(oldEntry, HttpStatus.OK); // 200 OK with updated entry
        }

        return new ResponseEntity<>("❌ Entry not found with ID: " + id, HttpStatus.NOT_FOUND);
    }

}
