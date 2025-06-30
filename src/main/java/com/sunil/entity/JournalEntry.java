package com.sunil.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "journal_entries")
@Data
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String content;

    private LocalDate entryDate;  // YYYY-MM-DD
    private LocalTime entryTime;  // HH:MM:SS

    // ✅ ManyToOne relation to User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // foreign key
    @ToString.Exclude
    private User user;

    public JournalEntry() {
        this.entryDate = LocalDate.now();
        this.entryTime = LocalTime.now();
    }
}
