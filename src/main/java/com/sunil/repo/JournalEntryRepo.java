package com.sunil.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sunil.entity.JournalEntry;
public interface JournalEntryRepo extends JpaRepository<JournalEntry, Integer> {
}
