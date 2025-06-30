package com.sunil.entity;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "journal_entries")
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
	//
	public LocalDate getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}
	public LocalTime getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(LocalTime entryTime) {
		this.entryTime = entryTime;
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
