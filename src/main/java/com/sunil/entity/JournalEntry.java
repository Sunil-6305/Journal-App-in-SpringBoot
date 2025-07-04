package com.sunil.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonAlias({"title", "Title"})
    private String title;

    @JsonAlias({"content", "Content"})
    private String content;

    @JsonAlias({"entryDate", "EntryDate"})
    private LocalDate entryDate;

    @JsonAlias({"entryTime", "EntryTime"})
    private LocalTime entryTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private User user;

    @PrePersist
    public void prePersist() {
        this.entryDate = LocalDate.now();
        this.entryTime = LocalTime.now();
    }
}
