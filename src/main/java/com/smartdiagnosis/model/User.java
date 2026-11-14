package com.smartdiagnosis.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * User - Represents a registered user of the application.
 * Demonstrates: Encapsulation, Constructors, Getters/Setters, Collections
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String name;
    private int age;
    private String gender;
    private double height; // in cm
    private double weight; // in kg
    private String bloodGroup;
    private LocalDate dateOfBirth;
    private String allergies;
    private String existingConditions;
    private String emergencyContact;
    private LocalDate createdAt;
    private int streakDays;
    private int totalCheckIns;
    private List<String> badges;
    private LocalDate lastCheckIn;

    // Default constructor
    public User() {
        this.badges = new ArrayList<>();
        this.createdAt = LocalDate.now();
    }

    // Parameterized constructor
    public User(String username, String passwordHash, String name, int age, String gender) {
        this();
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    // ===== Getters and Setters =====

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getExistingConditions() { return existingConditions; }
    public void setExistingConditions(String c) { this.existingConditions = c; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public int getStreakDays() { return streakDays; }
    public void setStreakDays(int streakDays) { this.streakDays = streakDays; }

    public int getTotalCheckIns() { return totalCheckIns; }
    public void setTotalCheckIns(int totalCheckIns) { this.totalCheckIns = totalCheckIns; }

    public List<String> getBadges() { return badges; }
    public void setBadges(List<String> badges) { this.badges = badges != null ? badges : new ArrayList<>(); }

    public void addBadge(String badge) {
        if (this.badges == null) this.badges = new ArrayList<>();
        if (!this.badges.contains(badge)) this.badges.add(badge);
    }

    public LocalDate getLastCheckIn() { return lastCheckIn; }
    public void setLastCheckIn(LocalDate lastCheckIn) { this.lastCheckIn = lastCheckIn; }

    // Helper: convert badges list to pipe-separated string for DB storage
    public String getBadgesAsString() {
        if (badges == null || badges.isEmpty()) return "";
        return String.join("|", badges);
    }

    // Helper: parse pipe-separated badges string from DB
    public void setBadgesFromString(String badgesStr) {
        this.badges = new ArrayList<>();
        if (badgesStr != null && !badgesStr.isBlank()) {
            for (String b : badgesStr.split("\\|")) {
                if (!b.isBlank()) this.badges.add(b.trim());
            }
        }
    }

    // Calculate BMI
    public double getBMI() {
        if (height <= 0 || weight <= 0) return 0;
        double heightM = height / 100.0;
        return Math.round((weight / (heightM * heightM)) * 10.0) / 10.0;
    }

    public String getBMICategory() {
        double bmi = getBMI();
        if (bmi == 0) return "N/A";
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal";
        if (bmi < 30.0) return "Overweight";
        return "Obese";
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', age=" + age + ", gender='" + gender + "'}";
    }
}
