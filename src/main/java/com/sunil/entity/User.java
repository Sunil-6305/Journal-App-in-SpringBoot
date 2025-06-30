package com.sunil.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private int id;

    @NonNull
    //@Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    @NonNull
    //@Column(nullable = false)
    private String password;

    /*
     * ✅ One-to-Many Relationship (1 User -> Many JournalEntries)
     *
     * mappedBy = "user"
     * --> "user" field JournalEntry entity में है, वही foreign key hold कर रहा है।
     *
     * cascade = CascadeType.ALL
     * --> अगर User को save/delete करोगे तो उसकी entries भी automatically save/delete होंगी।
     *
     * orphanRemoval = true
     * --> अगर किसी User से उसकी JournalEntry हटा दी गई, तो वो DB से भी delete हो जाएगी।
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JournalEntry> journalEntries = new ArrayList<>();
}
