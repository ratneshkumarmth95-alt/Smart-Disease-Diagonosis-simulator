package com.smartdiagnosis.model;

/**
 * DiseaseSymptomLink - Maps a disease to a symptom with a relevance weight.
 * Weight: 1=common symptom, 2=important symptom, 3=highly characteristic symptom
 * Demonstrates: Composition pattern
 */
public class DiseaseSymptomLink {
    private int symptomId;
    private int weight; // 1, 2, or 3

    public DiseaseSymptomLink(int symptomId, int weight) {
        this.symptomId = symptomId;
        this.weight = weight;
    }

    public int getSymptomId() { return symptomId; }
    public void setSymptomId(int symptomId) { this.symptomId = symptomId; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    @Override
    public String toString() {
        return "DiseaseSymptomLink{symptomId=" + symptomId + ", weight=" + weight + "}";
    }
}
