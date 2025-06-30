package com.sunil.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sunil.entity.JournalEntry;
import com.sunil.entity.User;
import com.sunil.repo.JournalEntryRepo;
import com.sunil.repo.UserRepo;

@Service
public class JournalEnteryServiceimpl    {
    @Autowired
    private JournalEntryRepo jeRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserServiceimpl userServiceimpl;

    public void saveJEntry(JournalEntry journalEntry) {
	jeRepo.save(journalEntry);
    }

    public List<JournalEntry> getAllJe() {
	return jeRepo.findAll();
    }

    public Optional<JournalEntry> findByID(int id) {
	return jeRepo.findById(id);
    }

    public void deleteById(int id) {
	    Optional<JournalEntry> optional = jeRepo.findById(id);
	    if (optional.isPresent()) {
	        jeRepo.delete(optional.get());
	         
	    }
	     
	}
    
    
    public JournalEntry findByIdAndUserName(int id, String userName) {
        return jeRepo.findByIdAndUser_UserName(id, userName).orElse(null);
    }

    public void updateEntry(JournalEntry entry) {
        jeRepo.save(entry); // will update if ID exists
    }	

    public void saveEntryWithUser(JournalEntry journalEntry, String userName) {
	User user = userServiceimpl.findByUserName(userName);
	if (user != null) {
	    journalEntry.setUser(user);
	    JournalEntry saved = jeRepo.save(journalEntry); // set user before saving
	    user.getJournalEntries().add(saved);
	    userServiceimpl.saveUEntry(user);

	} else {
	    throw new RuntimeException("User not found with username: " + userName);
	}
    }

}
