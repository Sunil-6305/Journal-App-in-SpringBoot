package com.sunil.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private int id;

    private String title;
    private String content;

    private LocalDate entryDate;  // YYYY-MM-DD
    private LocalTime entryTime;  // HH:MM:SS

    /*
     * ✅ Many-to-One Relationship (Many JournalEntries -> 1 User)
     *
     * @ManyToOne
     * --> Many entries can belong to one user.
     *
     * @JoinColumn(name = "user_id", nullable = false)
     * --> 'user_id' column foreign key होगा users table का.
     * --> nullable = false मतलब हर entry का user जरूरी है।
     *
     * @ToString.Exclude
     * --> Lombok के toString() में इसको exclude किया गया है ताकि circular reference से बचा जा सके।
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private User user;

    // Constructor: automatically sets current date and time
    @PrePersist
    public void prePersist() {
        this.entryDate = LocalDate.now();
        this.entryTime = LocalTime.now();
    }
}
