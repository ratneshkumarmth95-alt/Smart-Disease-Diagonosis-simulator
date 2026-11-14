package com.smartdiagnosis.model;

import java.time.LocalDateTime;

/**
 * JournalEntry - User's health journal entry.
 * Demonstrates: Encapsulation, Date/Time handling
 */
public class JournalEntry {
    private int id;
    private int userId;
    private LocalDateTime entryDate;
    private String title;
    private String content;
    private String mood; // GREAT, GOOD, OKAY, BAD, TERRIBLE

    public JournalEntry() {
        this.entryDate = LocalDateTime.now();
    }

    public JournalEntry(int userId, String title, String content, String mood) {
        this();
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.mood = mood;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDateTime entryDate) { this.entryDate = entryDate; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getMoodEmoji() {
        if (mood == null) return "😐";
        switch (mood) {
            case "GREAT":    return "😄";
            case "GOOD":     return "🙂";
            case "OKAY":     return "😐";
            case "BAD":      return "😕";
            case "TERRIBLE": return "😢";
            default:         return "😐";
        }
    }
}
