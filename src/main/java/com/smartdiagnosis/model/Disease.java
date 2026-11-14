package com.smartdiagnosis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Disease - Represents a medical condition in the knowledge base.
 * Demonstrates: Encapsulation, Composition, Collections
 */
public class Disease {
    private int id;
    private String name;
    private String description;
    private String category;
    private String severityLevel; // LOW, MODERATE, HIGH
    private List<DiseaseSymptomLink> symptomLinks;
    private List<String> commonSigns;
    private List<String> selfCareAdvice;
    private List<String> warningSigns;
    private String medicalAttentionAdvice;
    private boolean requiresImmediateAttention;

    public Disease() {
        this.symptomLinks = new ArrayList<>();
        this.commonSigns = new ArrayList<>();
        this.selfCareAdvice = new ArrayList<>();
        this.warningSigns = new ArrayList<>();
    }

    public Disease(int id, String name, String description, String category, String severityLevel) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.severityLevel = severityLevel;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }

    public List<DiseaseSymptomLink> getSymptomLinks() { return symptomLinks; }
    public void setSymptomLinks(List<DiseaseSymptomLink> symptomLinks) { this.symptomLinks = symptomLinks; }
    public void addSymptomLink(DiseaseSymptomLink link) { this.symptomLinks.add(link); }

    public List<String> getCommonSigns() { return commonSigns; }
    public void setCommonSigns(List<String> commonSigns) { this.commonSigns = commonSigns; }

    public List<String> getSelfCareAdvice() { return selfCareAdvice; }
    public void setSelfCareAdvice(List<String> selfCareAdvice) { this.selfCareAdvice = selfCareAdvice; }

    public List<String> getWarningSigns() { return warningSigns; }
    public void setWarningSigns(List<String> warningSigns) { this.warningSigns = warningSigns; }

    public String getMedicalAttentionAdvice() { return medicalAttentionAdvice; }
    public void setMedicalAttentionAdvice(String advice) { this.medicalAttentionAdvice = advice; }

    public boolean isRequiresImmediateAttention() { return requiresImmediateAttention; }
    public void setRequiresImmediateAttention(boolean r) { this.requiresImmediateAttention = r; }

    @Override
    public String toString() { return name; }
}
