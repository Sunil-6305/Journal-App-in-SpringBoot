package com.sunil.services;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sunil.entity.JournalEntry;
import com.sunil.repo.JournalEntryRepo;
@Service
public class JournalEnteryServiceimpl  
{
				@Autowired
				private JournalEntryRepo jeRepo;
				public void saveJEntry(JournalEntry journalEntry) {
					jeRepo.save(journalEntry);
				}
				public List<JournalEntry> getAllJe() {
					return jeRepo.findAll();
				}
				public Optional<JournalEntry> findByID(int id) {
					return jeRepo.findById(id);
				}
				public void deletById(int id) {
					jeRepo.deleteById(id);
				}
}
