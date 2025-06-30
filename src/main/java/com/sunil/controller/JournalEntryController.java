package com.sunil.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sunil.entity.JournalEntry;
import com.sunil.services.JournalEnteryServiceimpl;
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
     * ke through database me save kiya jaata hai.
     */
    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry journalEntry) {
	jEntryService.saveJEntry(journalEntry);
	return true;
    }
    /*
     * ✅ GET Request: /journal Ye method saare JournalEntry objects ko list ke form
     * me return karta hai. Service layer ke through sabhi entries ko fetch kiya
     * jaata hai.
     */
    @GetMapping
    public List<JournalEntry> getSimpalAll() {
	return jEntryService.getAllJe();
    }
    /*
     * ✅ GET Request: /journal/id/{myId} Particular JournalEntry ko fetch karta hai
     * uske ID ke basis pe. Agar entry milti hai to return karta hai, nahi to null
     * return hota hai.
     */
    @GetMapping("id/{myId}")
    public JournalEntry getJournalEntrybyId(@PathVariable int myId) {
	return jEntryService.findByID(myId).orElse(null);
    }
    /*
     * ✅ DELETE Request: /journal/id/{myId} Kisi particular JournalEntry ko delete
     * karta hai uski ID ke basis pe. Delete ke baad true return karta hai.
     */
    @DeleteMapping("id/{myId}")
    public boolean deljournalEntry(@PathVariable int myId) {
	jEntryService.deletById(myId);
	return true;
    }
    /*
     * ✅ PUT Request: /journal/id/{id} Yeh method existing JournalEntry ko update
     * karta hai uski ID ke basis par. Agar title ya content naya diya gaya hai, to
     * wahi update hoga. Purani values ko preserve karta hai agar naye fields null
     * ya blank ho.
     */
    @PutMapping("id/{id}")
    public JournalEntry updatJEById(@PathVariable int id, @RequestBody JournalEntry updatedEntry) {
	JournalEntry oldEntry = jEntryService.findByID(id).orElse(null);
	if (oldEntry != null) {
	    oldEntry.setTitle(
		    updatedEntry.getTitle() != null && !updatedEntry.getTitle().isEmpty() ? updatedEntry.getTitle()
			    : oldEntry.getTitle());
	    oldEntry.setContent(updatedEntry.getContent() != null && !updatedEntry.getContent().isEmpty()
		    ? updatedEntry.getContent()
		    : oldEntry.getContent());
	}
	jEntryService.saveJEntry(oldEntry);
	return oldEntry;
    }
}
