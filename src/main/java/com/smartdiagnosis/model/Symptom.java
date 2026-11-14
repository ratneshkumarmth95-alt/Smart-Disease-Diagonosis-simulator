package com.smartdiagnosis.model;

/**
 * Symptom - Represents a medical symptom in the knowledge base.
 * Demonstrates: Encapsulation, equals/hashCode override
 */
public class Symptom {
    private int id;
    private String name;
    private String category;
    private boolean isEmergency;
    private String description;
    private int weight; // 1=common, 2=important, 3=highly characteristic

    public Symptom() { this.weight = 1; }

    public Symptom(int id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.weight = 1;
        this.isEmergency = false;
    }

    public Symptom(int id, String name, String category, boolean isEmergency) {
        this(id, name, category);
        this.isEmergency = isEmergency;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isEmergency() { return isEmergency; }
    public void setEmergency(boolean emergency) { isEmergency = emergency; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    @Override
    public String toString() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symptom)) return false;
        Symptom s = (Symptom) o;
        return id == s.id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}
