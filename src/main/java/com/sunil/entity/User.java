package com.sunil.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @JsonAlias({"userName", "UserName", "username"})  // ✅ Accept all casing variations
    private String userName;

    @NonNull
    @JsonAlias({"password", "Password"})
    private String password;
    private String email;
    private String provider;
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JournalEntry> journalEntries = new ArrayList<>();
    
    @NonNull
    private List<String> roles;
}
