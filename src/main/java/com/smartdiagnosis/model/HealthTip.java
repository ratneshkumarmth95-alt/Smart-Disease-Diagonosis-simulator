package com.smartdiagnosis.model;

/**
 * HealthTip - A rotating health tip shown to users on the dashboard.
 * Demonstrates: Simple encapsulation, data class pattern
 */
public class HealthTip {
    private int id;
    private String category; // HYDRATION, SLEEP, NUTRITION, EXERCISE, STRESS, HYGIENE, SEASONAL, STUDY, EYECARE
    private String title;
    private String content;
    private String icon;

    public HealthTip() {}

    public HealthTip(int id, String category, String title, String content, String icon) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.icon = icon;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    @Override
    public String toString() { return icon + " " + title; }
}
