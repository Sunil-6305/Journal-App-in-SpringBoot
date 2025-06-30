package com.sunil.entity;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "journal_entries")
@Data  //Bas ek @Data lagaya — aur sab kuch auto ho gaya! no seter geter
public class JournalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ Auto-increment
	private int id;
	private String title;
	private String content;
	private LocalDate entryDate;   // 🔹 Sirf Date ke liye (YYYY-MM-DD)
	private LocalTime entryTime;   // 🔹 Sirf Time ke liye (HH:MM:SS)
	//
	public JournalEntry() {
	    this.entryDate = LocalDate.now();
	    this.entryTime = LocalTime.now();
	}
	
}
