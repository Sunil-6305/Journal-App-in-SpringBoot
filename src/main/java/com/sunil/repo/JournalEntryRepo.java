package com.sunil.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sunil.entity.JournalEntry;
public interface JournalEntryRepo extends JpaRepository<JournalEntry, Integer> {
    
    //void saveEntryWithUser(JournalEntry journalEntry, String userName);
    
    Optional<JournalEntry> findByIdAndUser_UserName(int id, String userName);

}
