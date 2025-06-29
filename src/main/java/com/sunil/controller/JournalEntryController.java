package com.sunil.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunil.entity.JournalEntry;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

	// Yeh ek Map hai jismein hum JournalEntry object ko memory mein temporarily
	// store kar rahe hain.
	// Key = Long (ID), Value = JournalEntry object
	private Map<Long, JournalEntry> journalEntries = new HashMap<>();
	private long currentId = 1;

	/*
	 * Jab user GET request karega bina kisi path ke ("/"), to yeh method saari
	 * journal entries list ke form mein return karega. List<JournalEntry>: ye
	 * method ek List return karega, jisme JournalEntry type ke objects honge.
	 * .values(): iska matlab map ke andar jitni bhi values hain (sirf JournalEntry
	 * objects), unko le lo. new ArrayList<>(...): un values ko ek nayi list me daal
	 * do. Map ke andar jitni bhi Journal Entries hain, unko ek List me daal ke
	 * return karega.
	 */
	@GetMapping
	public List<JournalEntry> getSimpalAll() {
		return new ArrayList<>(journalEntries.values());
	}

	/*
	 * Jab user POST request bhejta hai JSON data ke saath, to yeh method us JSON ko
	 * JournalEntry object mein convert karke map mein save kar dega.
	 * 
	 * @RequestBody: JSON body ko JournalEntry object me convert karne ke liye use
	 * hota hai. journalEntries.put(): ID ke basis pe object ko map me store kar
	 * deta hai.
	 */
	@PostMapping
	public boolean createEntry(@RequestBody JournalEntry journalEntry) {
		journalEntry.setId(currentId++);
		journalEntries.put(journalEntry.getId(), journalEntry); // ID ke basis pe entry store ho jaegi
		return true;
	}

	/*
	 * Jab user GET request karta hai "/id/{myId}" ke saath, to yeh method us ID
	 * wali ek specific journal entry return karega.
	 * 
	 * @PathVariable: URL se ID uthakar method me pass karta hai.
	 * journalEntries.get(): Map se directly us ID ke object ko fetch karta hai.
	 */
	@GetMapping("id/{myId}")
	public JournalEntry getJournalEntrybyId(@PathVariable Long myId) {
		return journalEntries.get(myId);
	}

	/*
	 * Jab user DELETE request karta hai "/id/{myId}" ke saath, to yeh method us ID
	 * wali entry ko map se hata dega aur delete ki gayi entry ko return karega.
	 * journalEntries.remove(): us ID ke object ko map se delete kar deta hai.
	 */
	@DeleteMapping("id/{myId}")
	public JournalEntry deljournalEntry(@PathVariable Long myId) {
		return journalEntries.remove(myId);
	}

	// ====================[ PUT by ID - Entry update karne ke liye
	// ]======================
	/*
	 * Jab user PUT request karta hai "/id/{id}" ke saath, aur body mein updated
	 * entry deta hai, to yeh method purani entry ko nayi entry se replace kar deta
	 * hai. journalEntries.put(): purani entry overwrite ho jaati hai nayi entry se.
	 * 
	 * @RequestBody: updated entry ko JSON se object banata hai.
	 */
	@PutMapping("id/{id}")
	public JournalEntry updatJournalEntryByEntryId(@PathVariable Long id, @RequestBody JournalEntry myEntry) {
		return journalEntries.put(id, myEntry);
	}

}
