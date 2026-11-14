package com.smartdiagnosis.service;

import com.smartdiagnosis.model.*;
import java.util.*;

/**
 * KnowledgeBase - In-memory database of all symptoms, diseases, and health tips.
 * Uses Singleton pattern to ensure only one instance exists.
 * This is the HEART of the application - all 80 symptoms and 25 diseases are defined here.
 *
 * Demonstrates: Singleton Pattern, Collections Framework, HashMap, ArrayList
 */
public class KnowledgeBase {

    private static KnowledgeBase instance;

    private final Map<Integer, Symptom> symptomsById = new HashMap<>();
    private final Map<String, Symptom> symptomsByName = new HashMap<>();
    private final Map<Integer, Disease> diseasesById = new HashMap<>();
    private final List<Disease> allDiseases = new ArrayList<>();
    private final List<Symptom> allSymptoms = new ArrayList<>();
    private final List<HealthTip> allHealthTips = new ArrayList<>();

    // Private constructor — Singleton pattern
    private KnowledgeBase() {
        initializeSymptoms();
        initializeDiseases();
        initializeHealthTips();
    }

    /** Returns the single shared instance */
    public static synchronized KnowledgeBase getInstance() {
        if (instance == null) {
            instance = new KnowledgeBase();
        }
        return instance;
    }

    // ==================== SYMPTOM INITIALIZATION ====================

    private void addSymptom(int id, String name, String category, boolean emergency) {
        Symptom s = new Symptom(id, name, category, emergency);
        symptomsById.put(id, s);
        symptomsByName.put(name.toLowerCase(), s);
        allSymptoms.add(s);
    }

    private void initializeSymptoms() {

        // === GENERAL SYMPTOMS ===
        addSymptom(1,  "Fever",             "General", false);
        addSymptom(2,  "Chills",            "General", false);
        addSymptom(3,  "Fatigue",           "General", false);
        addSymptom(4,  "Weakness",          "General", false);
        addSymptom(5,  "Malaise",           "General", false);
        addSymptom(6,  "Dizziness",         "General", false);
        addSymptom(7,  "Fainting",          "General", true);  // EMERGENCY
        addSymptom(8,  "Sweating",          "General", false);
        addSymptom(9,  "Night sweats",      "General", false);
        addSymptom(10, "Loss of appetite",  "General", false);

        // === HEAD / NEUROLOGICAL ===
        addSymptom(11, "Headache",                    "Neurological", false);
        addSymptom(12, "Migraine-like headache",      "Neurological", false);
        addSymptom(13, "Confusion",                   "Neurological", true); // EMERGENCY
        addSymptom(14, "Memory difficulty",           "Neurological", false);
        addSymptom(15, "Numbness",                    "Neurological", false);
        addSymptom(16, "Tingling",                    "Neurological", false);
        addSymptom(17, "Tremors",                     "Neurological", false);
        addSymptom(18, "Sudden severe headache",      "Neurological", true); // EMERGENCY
        addSymptom(19, "Sensitivity to light",        "Neurological", false);
        addSymptom(20, "Sensitivity to sound",        "Neurological", false);
        addSymptom(21, "Neck stiffness",              "Neurological", true); // EMERGENCY
        addSymptom(22, "Sudden one-sided weakness",   "Neurological", true); // EMERGENCY

        // === RESPIRATORY ===
        addSymptom(23, "Cough",                      "Respiratory", false);
        addSymptom(24, "Dry cough",                  "Respiratory", false);
        addSymptom(25, "Wet cough",                  "Respiratory", false);
        addSymptom(26, "Shortness of breath",        "Respiratory", true);  // EMERGENCY
        addSymptom(27, "Wheezing",                   "Respiratory", false);
        addSymptom(28, "Runny nose",                 "Respiratory", false);
        addSymptom(29, "Nasal congestion",           "Respiratory", false);
        addSymptom(30, "Sneezing",                   "Respiratory", false);
        addSymptom(31, "Sore throat",                "Respiratory", false);
        addSymptom(32, "Chest discomfort",           "Respiratory", false);
        addSymptom(33, "Severe chest pain",          "Respiratory", true);  // EMERGENCY
        addSymptom(34, "Severe difficulty breathing","Respiratory", true);  // EMERGENCY
        addSymptom(35, "Loss of smell",              "Respiratory", false);
        addSymptom(36, "Loss of taste",              "Respiratory", false);

        // === DIGESTIVE ===
        addSymptom(37, "Nausea",                "Digestive", false);
        addSymptom(38, "Vomiting",              "Digestive", false);
        addSymptom(39, "Diarrhea",              "Digestive", false);
        addSymptom(40, "Constipation",          "Digestive", false);
        addSymptom(41, "Abdominal pain",        "Digestive", false);
        addSymptom(42, "Bloating",              "Digestive", false);
        addSymptom(43, "Heartburn",             "Digestive", false);
        addSymptom(44, "Acid reflux",           "Digestive", false);
        addSymptom(45, "Blood in stool",        "Digestive", true); // EMERGENCY

        // === MUSCULOSKELETAL ===
        addSymptom(46, "Muscle pain",    "Musculoskeletal", false);
        addSymptom(47, "Joint pain",     "Musculoskeletal", false);
        addSymptom(48, "Back pain",      "Musculoskeletal", false);
        addSymptom(49, "Neck pain",      "Musculoskeletal", false);
        addSymptom(50, "Body ache",      "Musculoskeletal", false);
        addSymptom(51, "Muscle weakness","Musculoskeletal", false);
        addSymptom(52, "Muscle cramps",  "Musculoskeletal", false);

        // === SKIN ===
        addSymptom(53, "Rash",                                     "Skin", false);
        addSymptom(54, "Itching",                                  "Skin", false);
        addSymptom(55, "Redness",                                  "Skin", false);
        addSymptom(56, "Dry skin",                                 "Skin", false);
        addSymptom(57, "Swelling",                                 "Skin", false);
        addSymptom(58, "Hives",                                    "Skin", false);
        addSymptom(59, "Skin lesions",                             "Skin", false);
        addSymptom(60, "Severe allergic reaction (throat swelling)","Skin", true); // EMERGENCY

        // === URINARY ===
        addSymptom(61, "Frequent urination",        "Urinary", false);
        addSymptom(62, "Painful urination",         "Urinary", false);
        addSymptom(63, "Blood in urine",            "Urinary", true); // EMERGENCY
        addSymptom(64, "Lower abdominal discomfort","Urinary", false);
        addSymptom(65, "Foul-smelling urine",       "Urinary", false);

        // === EYE ===
        addSymptom(66, "Eye redness",    "Eye", false);
        addSymptom(67, "Eye pain",       "Eye", false);
        addSymptom(68, "Blurred vision", "Eye", false);
        addSymptom(69, "Watery eyes",    "Eye", false);
        addSymptom(70, "Light sensitivity","Eye", false);

        // === EAR / NOSE / THROAT ===
        addSymptom(71, "Ear pain",             "ENT", false);
        addSymptom(72, "Hearing difficulty",   "ENT", false);
        addSymptom(73, "Sinus pressure",       "ENT", false);
        addSymptom(74, "Difficulty swallowing","ENT", false);
        addSymptom(75, "Swollen lymph nodes",  "ENT", false);

        // === SEVERE / EMERGENCY ===
        addSymptom(76, "Severe dehydration",          "General",    true); // EMERGENCY
        addSymptom(77, "High fever above 103F",        "General",    true); // EMERGENCY
        addSymptom(78, "Persistent vomiting",          "Digestive",  true); // EMERGENCY
        addSymptom(79, "Productive cough with blood",  "Respiratory",true); // EMERGENCY
        addSymptom(80, "Sudden vision loss",           "Eye",        true); // EMERGENCY
    }

    // ==================== DISEASE INITIALIZATION ====================

    private void addDisease(Disease d) {
        diseasesById.put(d.getId(), d);
        allDiseases.add(d);
    }

    private void initializeDiseases() {

        // ---- 1. Common Cold ----
        Disease cold = new Disease(1, "Common Cold",
            "A mild viral infection of the upper respiratory tract caused by rhinoviruses. Very common and usually resolves within 7-10 days.",
            "Respiratory", "LOW");
        cold.addSymptomLink(new DiseaseSymptomLink(28, 3)); // runny nose - highly characteristic
        cold.addSymptomLink(new DiseaseSymptomLink(29, 3)); // nasal congestion
        cold.addSymptomLink(new DiseaseSymptomLink(30, 2)); // sneezing
        cold.addSymptomLink(new DiseaseSymptomLink(23, 2)); // cough
        cold.addSymptomLink(new DiseaseSymptomLink(31, 2)); // sore throat
        cold.addSymptomLink(new DiseaseSymptomLink(3,  1)); // fatigue
        cold.addSymptomLink(new DiseaseSymptomLink(1,  1)); // fever (mild/rare)
        cold.addSymptomLink(new DiseaseSymptomLink(11, 1)); // headache
        cold.setCommonSigns(Arrays.asList("Runny nose","Nasal congestion","Sneezing","Mild cough","Sore throat"));
        cold.setSelfCareAdvice(Arrays.asList(
            "Rest adequately and stay hydrated.",
            "Drink warm fluids like herbal tea or warm water with honey.",
            "Use saline nasal rinse to relieve congestion.",
            "Gargle with warm salt water for sore throat relief.",
            "Eat nutritious food even if appetite is reduced."
        ));
        cold.setWarningSigns(Arrays.asList(
            "Symptoms lasting more than 10 days without improvement.",
            "High fever above 103\u00b0F (39.4\u00b0C).",
            "Severe headache or sinus pain.",
            "Difficulty breathing or chest pain."
        ));
        cold.setMedicalAttentionAdvice("Consider consulting a healthcare professional if symptoms persist beyond 10 days, worsen significantly, or if you develop a high fever.");
        addDisease(cold);

        // ---- 2. Influenza-like Illness ----
        Disease flu = new Disease(2, "Influenza-like Illness",
            "A contagious respiratory illness caused by influenza viruses. Symptoms are typically more severe than a common cold and come on suddenly.",
            "Respiratory", "MODERATE");
        flu.addSymptomLink(new DiseaseSymptomLink(1,  3)); // fever - highly characteristic
        flu.addSymptomLink(new DiseaseSymptomLink(50, 3)); // body ache - highly characteristic
        flu.addSymptomLink(new DiseaseSymptomLink(3,  3)); // fatigue
        flu.addSymptomLink(new DiseaseSymptomLink(11, 2)); // headache
        flu.addSymptomLink(new DiseaseSymptomLink(23, 2)); // cough
        flu.addSymptomLink(new DiseaseSymptomLink(2,  2)); // chills
        flu.addSymptomLink(new DiseaseSymptomLink(46, 2)); // muscle pain
        flu.addSymptomLink(new DiseaseSymptomLink(37, 1)); // nausea
        flu.addSymptomLink(new DiseaseSymptomLink(10, 1)); // loss of appetite
        flu.setCommonSigns(Arrays.asList("Sudden-onset fever","Body aches","Severe fatigue","Dry cough","Chills","Headache"));
        flu.setSelfCareAdvice(Arrays.asList(
            "Rest completely - your body needs energy to fight the illness.",
            "Drink plenty of fluids: water, clear broths, electrolyte drinks.",
            "Monitor your temperature regularly.",
            "Eat light, easy-to-digest foods.",
            "Avoid contact with others to prevent spreading."
        ));
        flu.setWarningSigns(Arrays.asList(
            "Difficulty breathing or shortness of breath.",
            "Persistent chest pain or pressure.",
            "Confusion or altered mental status.",
            "Severe vomiting or inability to keep fluids down.",
            "Symptoms that improve then suddenly return with fever and worsening cough."
        ));
        flu.setMedicalAttentionAdvice("Seek medical evaluation if symptoms are severe, you belong to a high-risk group (elderly, pregnant, immunocompromised), or symptoms worsen after initial improvement.");
        addDisease(flu);

        // ---- 3. COVID-19-like Respiratory Illness ----
        Disease covid = new Disease(3, "COVID-19-like Respiratory Illness",
            "A respiratory illness with symptom patterns similar to COVID-19. This is an educational simulation - please get proper medical testing for a confirmed diagnosis.",
            "Respiratory", "MODERATE");
        covid.addSymptomLink(new DiseaseSymptomLink(1,  3)); // fever
        covid.addSymptomLink(new DiseaseSymptomLink(24, 3)); // dry cough
        covid.addSymptomLink(new DiseaseSymptomLink(3,  3)); // fatigue
        covid.addSymptomLink(new DiseaseSymptomLink(35, 3)); // loss of smell
        covid.addSymptomLink(new DiseaseSymptomLink(36, 3)); // loss of taste
        covid.addSymptomLink(new DiseaseSymptomLink(26, 2)); // shortness of breath
        covid.addSymptomLink(new DiseaseSymptomLink(50, 2)); // body ache
        covid.addSymptomLink(new DiseaseSymptomLink(11, 2)); // headache
        covid.addSymptomLink(new DiseaseSymptomLink(31, 1)); // sore throat
        covid.addSymptomLink(new DiseaseSymptomLink(37, 1)); // nausea
        covid.setCommonSigns(Arrays.asList("Fever","Dry cough","Fatigue","Loss of smell/taste","Shortness of breath"));
        covid.setSelfCareAdvice(Arrays.asList(
            "Self-isolate to prevent spreading to others.",
            "Rest and stay well hydrated.",
            "Monitor oxygen levels if a pulse oximeter is available.",
            "Track your temperature daily.",
            "Seek proper medical testing for confirmation."
        ));
        covid.setWarningSigns(Arrays.asList(
            "Difficulty breathing or shortness of breath.",
            "Persistent chest pain or pressure.",
            "Confusion or inability to wake or stay awake.",
            "Bluish lips or face.",
            "Oxygen saturation below 94%."
        ));
        covid.setMedicalAttentionAdvice("IMPORTANT: This simulation cannot diagnose COVID-19. Please get proper medical testing. Seek emergency care immediately if you have difficulty breathing or chest pain.");
        addDisease(covid);

        // ---- 4. Acute Bronchitis ----
        Disease bronchitis = new Disease(4, "Acute Bronchitis",
            "Inflammation of the bronchial tubes, usually caused by a viral infection. The main symptom is a persistent cough.",
            "Respiratory", "LOW");
        bronchitis.addSymptomLink(new DiseaseSymptomLink(23, 3)); // cough - hallmark
        bronchitis.addSymptomLink(new DiseaseSymptomLink(25, 3)); // wet cough
        bronchitis.addSymptomLink(new DiseaseSymptomLink(32, 2)); // chest discomfort
        bronchitis.addSymptomLink(new DiseaseSymptomLink(3,  2)); // fatigue
        bronchitis.addSymptomLink(new DiseaseSymptomLink(27, 2)); // wheezing
        bronchitis.addSymptomLink(new DiseaseSymptomLink(1,  1)); // fever (low-grade)
        bronchitis.addSymptomLink(new DiseaseSymptomLink(29, 1)); // nasal congestion
        bronchitis.setCommonSigns(Arrays.asList("Persistent cough (may produce mucus)","Chest discomfort","Slight fever","Fatigue","Mild wheezing"));
        bronchitis.setSelfCareAdvice(Arrays.asList(
            "Rest and drink plenty of fluids to thin mucus.",
            "Use a humidifier to ease breathing.",
            "Avoid smoking or secondhand smoke.",
            "Steam inhalation may provide relief."
        ));
        bronchitis.setWarningSigns(Arrays.asList(
            "Cough lasting more than 3 weeks.",
            "Blood in coughed-up mucus.",
            "High fever above 100.4\u00b0F (38\u00b0C).",
            "Shortness of breath at rest."
        ));
        bronchitis.setMedicalAttentionAdvice("Consult a healthcare professional if cough persists beyond 3 weeks, you develop high fever, or breathing becomes difficult.");
        addDisease(bronchitis);

        // ---- 5. Sinusitis ----
        Disease sinusitis = new Disease(5, "Sinusitis",
            "Inflammation of the sinuses, often following a cold. Can be viral or bacterial. Causes facial pressure and congestion.",
            "Respiratory", "LOW");
        sinusitis.addSymptomLink(new DiseaseSymptomLink(73, 3)); // sinus pressure
        sinusitis.addSymptomLink(new DiseaseSymptomLink(29, 3)); // nasal congestion
        sinusitis.addSymptomLink(new DiseaseSymptomLink(28, 2)); // runny nose
        sinusitis.addSymptomLink(new DiseaseSymptomLink(11, 2)); // headache
        sinusitis.addSymptomLink(new DiseaseSymptomLink(1,  1)); // fever
        sinusitis.addSymptomLink(new DiseaseSymptomLink(3,  1)); // fatigue
        sinusitis.setCommonSigns(Arrays.asList("Sinus pressure and pain","Nasal congestion","Thick nasal discharge","Headache","Facial tenderness"));
        sinusitis.setSelfCareAdvice(Arrays.asList(
            "Use saline nasal sprays or rinses.",
            "Apply warm compresses to the face.",
            "Stay hydrated to thin mucus.",
            "Use a humidifier.",
            "Sleep with head elevated."
        ));
        sinusitis.setWarningSigns(Arrays.asList(
            "Symptoms lasting more than 10 days.",
            "Severe headache or facial pain.",
            "Vision changes or eye swelling.",
            "Stiff neck or high fever."
        ));
        sinusitis.setMedicalAttentionAdvice("See a doctor if sinusitis symptoms persist more than 10 days, are severe, or if you develop vision problems.");
        addDisease(sinusitis);

        // ---- 6. Pharyngitis / Sore Throat ----
        Disease pharyngitis = new Disease(6, "Pharyngitis (Sore Throat)",
            "Inflammation of the pharynx (throat), usually caused by a viral infection. Can also be bacterial (strep throat).",
            "Respiratory", "LOW");
        pharyngitis.addSymptomLink(new DiseaseSymptomLink(31, 3)); // sore throat
        pharyngitis.addSymptomLink(new DiseaseSymptomLink(74, 2)); // difficulty swallowing
        pharyngitis.addSymptomLink(new DiseaseSymptomLink(75, 2)); // swollen lymph nodes
        pharyngitis.addSymptomLink(new DiseaseSymptomLink(1,  2)); // fever
        pharyngitis.addSymptomLink(new DiseaseSymptomLink(11, 1)); // headache
        pharyngitis.addSymptomLink(new DiseaseSymptomLink(3,  1)); // fatigue
        pharyngitis.setCommonSigns(Arrays.asList("Sore throat","Difficulty swallowing","Swollen lymph nodes","Fever","Redness in throat"));
        pharyngitis.setSelfCareAdvice(Arrays.asList(
            "Gargle with warm salt water several times a day.",
            "Drink warm fluids like herbal tea with honey.",
            "Rest your voice.",
            "Use throat lozenges for comfort.",
            "Stay hydrated."
        ));
        pharyngitis.setWarningSigns(Arrays.asList(
            "Severe difficulty breathing or swallowing.",
            "Inability to open mouth widely.",
            "Rash appearing with sore throat.",
            "High fever persisting beyond 3 days."
        ));
        pharyngitis.setMedicalAttentionAdvice("See a doctor if throat pain is severe, you cannot swallow, have a high fever, or symptoms persist beyond 1 week.");
        addDisease(pharyngitis);

        // ---- 7. Pneumonia Pattern ----
        Disease pneumonia = new Disease(7, "Pneumonia-like Symptom Pattern",
            "A pattern of symptoms that may overlap with pneumonia. Pneumonia is a serious lung infection requiring medical attention.",
            "Respiratory", "HIGH");
        pneumonia.addSymptomLink(new DiseaseSymptomLink(1,  3)); // fever
        pneumonia.addSymptomLink(new DiseaseSymptomLink(25, 3)); // wet cough
        pneumonia.addSymptomLink(new DiseaseSymptomLink(26, 3)); // shortness of breath
        pneumonia.addSymptomLink(new DiseaseSymptomLink(32, 3)); // chest discomfort
        pneumonia.addSymptomLink(new DiseaseSymptomLink(3,  2)); // fatigue
        pneumonia.addSymptomLink(new DiseaseSymptomLink(2,  2)); // chills
        pneumonia.addSymptomLink(new DiseaseSymptomLink(37, 1)); // nausea
        pneumonia.setRequiresImmediateAttention(true);
        pneumonia.setCommonSigns(Arrays.asList("Productive cough","Fever with chills","Shortness of breath","Chest pain when breathing","Fatigue"));
        pneumonia.setSelfCareAdvice(Arrays.asList(
            "IMPORTANT: Seek medical evaluation promptly.",
            "Rest and maintain hydration.",
            "Take all prescribed medications as directed.",
            "Monitor breathing rate."
        ));
        pneumonia.setWarningSigns(Arrays.asList(
            "Difficulty breathing at rest.",
            "Bluish lips or fingertips.",
            "Confusion or altered consciousness.",
            "Inability to keep fluids down."
        ));
        pneumonia.setMedicalAttentionAdvice("These symptoms require professional medical evaluation. Please consult a doctor or visit an urgent care facility promptly.");
        addDisease(pneumonia);

        // ---- 8. Gastroenteritis ----
        Disease gastro = new Disease(8, "Gastroenteritis (Stomach Flu)",
            "Inflammation of the stomach and intestines, usually caused by a viral or bacterial infection.",
            "Digestive", "LOW");
        gastro.addSymptomLink(new DiseaseSymptomLink(37, 3)); // nausea
        gastro.addSymptomLink(new DiseaseSymptomLink(38, 3)); // vomiting
        gastro.addSymptomLink(new DiseaseSymptomLink(39, 3)); // diarrhea
        gastro.addSymptomLink(new DiseaseSymptomLink(41, 2)); // abdominal pain
        gastro.addSymptomLink(new DiseaseSymptomLink(1,  2)); // fever
        gastro.addSymptomLink(new DiseaseSymptomLink(2,  1)); // chills
        gastro.addSymptomLink(new DiseaseSymptomLink(50, 1)); // body ache
        gastro.addSymptomLink(new DiseaseSymptomLink(10, 2)); // loss of appetite
        gastro.setCommonSigns(Arrays.asList("Nausea","Vomiting","Diarrhea","Abdominal cramps","Low-grade fever"));
        gastro.setSelfCareAdvice(Arrays.asList(
            "Replace lost fluids with water, clear broth, or oral rehydration solution.",
            "Rest your stomach - avoid solid foods for a few hours if vomiting.",
            "Gradually reintroduce bland foods (rice, toast, banana).",
            "Wash hands thoroughly to prevent spreading.",
            "Avoid dairy, fatty, or spicy foods until recovered."
        ));
        gastro.setWarningSigns(Arrays.asList(
            "Signs of severe dehydration (extreme thirst, no urination, dizziness).",
            "Blood in vomit or stool.",
            "Severe abdominal pain.",
            "High fever above 102\u00b0F.",
            "Symptoms lasting more than 3 days."
        ));
        gastro.setMedicalAttentionAdvice("Seek medical attention if you show signs of severe dehydration, have blood in stool or vomit, or symptoms persist beyond 3 days.");
        addDisease(gastro);

        // ---- 9. Food Poisoning ----
        Disease foodPoisoning = new Disease(9, "Food Poisoning",
            "Illness caused by eating contaminated food. Symptoms usually appear within hours of eating the contaminated food.",
            "Digestive", "LOW");
        foodPoisoning.addSymptomLink(new DiseaseSymptomLink(37, 3)); // nausea
        foodPoisoning.addSymptomLink(new DiseaseSymptomLink(38, 3)); // vomiting
        foodPoisoning.addSymptomLink(new DiseaseSymptomLink(39, 2)); // diarrhea
        foodPoisoning.addSymptomLink(new DiseaseSymptomLink(41, 3)); // abdominal pain/cramps
        foodPoisoning.addSymptomLink(new DiseaseSymptomLink(1,  1)); // fever
        foodPoisoning.addSymptomLink(new DiseaseSymptomLink(50, 1)); // body ache
        foodPoisoning.setCommonSigns(Arrays.asList("Sudden nausea and vomiting","Diarrhea","Stomach cramps","Symptoms within hours of eating"));
        foodPoisoning.setSelfCareAdvice(Arrays.asList(
            "Replace fluids lost through vomiting and diarrhea.",
            "Avoid solid foods until vomiting stops.",
            "Avoid caffeine, alcohol, and dairy.",
            "Rest and monitor symptoms.",
            "Identify the food source if possible."
        ));
        foodPoisoning.setWarningSigns(Arrays.asList(
            "Symptoms of dehydration.",
            "Blood in stool.",
            "Neurological symptoms (blurred vision, weakness).",
            "Symptoms in pregnant women, elderly, or immunocompromised individuals."
        ));
        foodPoisoning.setMedicalAttentionAdvice("Seek medical attention if symptoms are severe, last more than 3 days, or if you have neurological symptoms.");
        addDisease(foodPoisoning);

        // ---- 10. Gastritis ----
        Disease gastritis = new Disease(10, "Gastritis",
            "Inflammation of the stomach lining. Can be acute or chronic, often triggered by diet, stress, or infection.",
            "Digestive", "LOW");
        gastritis.addSymptomLink(new DiseaseSymptomLink(41, 3)); // abdominal pain
        gastritis.addSymptomLink(new DiseaseSymptomLink(37, 2)); // nausea
        gastritis.addSymptomLink(new DiseaseSymptomLink(43, 2)); // heartburn
        gastritis.addSymptomLink(new DiseaseSymptomLink(42, 2)); // bloating
        gastritis.addSymptomLink(new DiseaseSymptomLink(10, 1)); // loss of appetite
        gastritis.addSymptomLink(new DiseaseSymptomLink(38, 1)); // vomiting
        gastritis.setCommonSigns(Arrays.asList("Upper abdominal pain or burning","Nausea","Bloating","Loss of appetite","Heartburn"));
        gastritis.setSelfCareAdvice(Arrays.asList(
            "Eat smaller, more frequent meals.",
            "Avoid spicy, acidic, and fatty foods.",
            "Avoid alcohol and caffeine.",
            "Do not take NSAIDs (ibuprofen) on an empty stomach.",
            "Manage stress through relaxation techniques."
        ));
        gastritis.setWarningSigns(Arrays.asList(
            "Black or tarry stools (possible bleeding).",
            "Vomiting blood.",
            "Severe abdominal pain.",
            "Unintentional weight loss."
        ));
        gastritis.setMedicalAttentionAdvice("See a doctor if symptoms persist despite dietary changes, or if you notice blood in vomit or stool.");
        addDisease(gastritis);

        // ---- 11. Acid Reflux / GERD ----
        Disease gerd = new Disease(11, "Acid Reflux / GERD",
            "A condition where stomach acid flows back into the esophagus. Very common and usually manageable with lifestyle changes.",
            "Digestive", "LOW");
        gerd.addSymptomLink(new DiseaseSymptomLink(43, 3)); // heartburn
        gerd.addSymptomLink(new DiseaseSymptomLink(44, 3)); // acid reflux
        gerd.addSymptomLink(new DiseaseSymptomLink(31, 2)); // sore throat (from acid)
        gerd.addSymptomLink(new DiseaseSymptomLink(23, 1)); // chronic cough
        gerd.addSymptomLink(new DiseaseSymptomLink(37, 1)); // nausea
        gerd.addSymptomLink(new DiseaseSymptomLink(42, 1)); // bloating
        gerd.setCommonSigns(Arrays.asList("Heartburn (burning sensation)","Acid taste in mouth","Belching","Difficulty swallowing","Chronic cough"));
        gerd.setSelfCareAdvice(Arrays.asList(
            "Avoid trigger foods: spicy, fatty, citrus, chocolate, coffee.",
            "Eat smaller meals and avoid lying down right after eating.",
            "Elevate the head of your bed.",
            "Maintain a healthy weight.",
            "Avoid tight-fitting clothing around the abdomen."
        ));
        gerd.setWarningSigns(Arrays.asList(
            "Difficulty or pain swallowing.",
            "Vomiting blood.",
            "Unexplained weight loss.",
            "Chest pain (rule out heart issues first)."
        ));
        gerd.setMedicalAttentionAdvice("See a doctor if heartburn occurs more than twice a week or doesn't improve with lifestyle changes.");
        addDisease(gerd);

        // ---- 12. Irritable Bowel Syndrome ----
        Disease ibs = new Disease(12, "Irritable Bowel Syndrome (IBS)",
            "A functional disorder affecting the large intestine. Causes chronic symptoms that fluctuate over time.",
            "Digestive", "LOW");
        ibs.addSymptomLink(new DiseaseSymptomLink(41, 3)); // abdominal pain
        ibs.addSymptomLink(new DiseaseSymptomLink(42, 3)); // bloating
        ibs.addSymptomLink(new DiseaseSymptomLink(39, 2)); // diarrhea
        ibs.addSymptomLink(new DiseaseSymptomLink(40, 2)); // constipation
        ibs.addSymptomLink(new DiseaseSymptomLink(37, 1)); // nausea
        ibs.setCommonSigns(Arrays.asList("Abdominal cramping","Bloating","Alternating diarrhea/constipation","Mucus in stool","Relief after bowel movement"));
        ibs.setSelfCareAdvice(Arrays.asList(
            "Identify and avoid trigger foods.",
            "Eat regular meals and avoid large portions.",
            "Increase fiber intake gradually.",
            "Stay well hydrated.",
            "Manage stress through exercise and relaxation."
        ));
        ibs.setWarningSigns(Arrays.asList(
            "Blood in stool.",
            "Unintentional weight loss.",
            "Symptoms waking you from sleep.",
            "New symptoms after age 50."
        ));
        ibs.setMedicalAttentionAdvice("See a doctor for diagnosis and to rule out other conditions. Keep a food and symptom diary.");
        addDisease(ibs);

        // ---- 13. Migraine ----
        Disease migraine = new Disease(13, "Migraine",
            "A neurological condition causing intense, recurring headaches, often with nausea and light sensitivity.",
            "Neurological", "MODERATE");
        migraine.addSymptomLink(new DiseaseSymptomLink(12, 3)); // migraine-like headache
        migraine.addSymptomLink(new DiseaseSymptomLink(19, 3)); // light sensitivity
        migraine.addSymptomLink(new DiseaseSymptomLink(20, 3)); // sound sensitivity
        migraine.addSymptomLink(new DiseaseSymptomLink(37, 2)); // nausea
        migraine.addSymptomLink(new DiseaseSymptomLink(38, 1)); // vomiting
        migraine.addSymptomLink(new DiseaseSymptomLink(6,  1)); // dizziness
        migraine.addSymptomLink(new DiseaseSymptomLink(70, 1)); // light sensitivity (eye)
        migraine.setCommonSigns(Arrays.asList("Throbbing one-sided headache","Nausea","Light sensitivity","Sound sensitivity","Visual disturbances (aura)"));
        migraine.setSelfCareAdvice(Arrays.asList(
            "Rest in a quiet, dark room.",
            "Apply cold or warm compress to head.",
            "Stay hydrated.",
            "Avoid known triggers (stress, certain foods, disrupted sleep).",
            "Keep a migraine diary to identify patterns."
        ));
        migraine.setWarningSigns(Arrays.asList(
            "Sudden, severe 'thunderclap' headache.",
            "Headache with fever and stiff neck.",
            "Weakness or numbness on one side of body.",
            "Vision loss or speech difficulties.",
            "Headache after head injury."
        ));
        migraine.setMedicalAttentionAdvice("See a doctor for a migraine management plan. Seek emergency care for sudden severe headache unlike any before.");
        addDisease(migraine);

        // ---- 14. Tension Headache ----
        Disease tensionHA = new Disease(14, "Tension Headache",
            "The most common type of headache, often described as a tight band around the head. Usually related to stress, posture, or eye strain.",
            "Neurological", "LOW");
        tensionHA.addSymptomLink(new DiseaseSymptomLink(11, 3)); // headache
        tensionHA.addSymptomLink(new DiseaseSymptomLink(49, 2)); // neck pain
        tensionHA.addSymptomLink(new DiseaseSymptomLink(3,  1)); // fatigue
        tensionHA.addSymptomLink(new DiseaseSymptomLink(14, 1)); // memory difficulty
        tensionHA.setCommonSigns(Arrays.asList("Dull pressing head pain","Neck and shoulder tension","Bilateral headache","Worse with stress or screen use"));
        tensionHA.setSelfCareAdvice(Arrays.asList(
            "Take regular breaks from screens.",
            "Practice relaxation techniques and stress management.",
            "Ensure correct posture when sitting.",
            "Stay hydrated.",
            "Gentle neck stretches may help.",
            "Ensure adequate sleep."
        ));
        tensionHA.setWarningSigns(Arrays.asList(
            "Headache with sudden onset (worst headache ever).",
            "Headache with fever and stiff neck.",
            "Headache after head trauma.",
            "Progressive worsening over days."
        ));
        tensionHA.setMedicalAttentionAdvice("See a doctor if headaches are frequent, severe, or interfering with daily life.");
        addDisease(tensionHA);

        // ---- 15. Allergic Rhinitis ----
        Disease allergyRhinitis = new Disease(15, "Allergic Rhinitis",
            "An allergic response causing sneezing, itching, and a runny nose. Often called hay fever when triggered by pollen.",
            "Allergic", "LOW");
        allergyRhinitis.addSymptomLink(new DiseaseSymptomLink(30, 3)); // sneezing
        allergyRhinitis.addSymptomLink(new DiseaseSymptomLink(28, 3)); // runny nose
        allergyRhinitis.addSymptomLink(new DiseaseSymptomLink(54, 2)); // itching
        allergyRhinitis.addSymptomLink(new DiseaseSymptomLink(29, 2)); // nasal congestion
        allergyRhinitis.addSymptomLink(new DiseaseSymptomLink(69, 2)); // watery eyes
        allergyRhinitis.addSymptomLink(new DiseaseSymptomLink(66, 1)); // eye redness
        allergyRhinitis.addSymptomLink(new DiseaseSymptomLink(23, 1)); // cough
        allergyRhinitis.setCommonSigns(Arrays.asList("Frequent sneezing","Itchy nose/eyes/throat","Runny nose","Watery eyes","Nasal congestion"));
        allergyRhinitis.setSelfCareAdvice(Arrays.asList(
            "Identify and avoid allergen triggers.",
            "Keep windows closed during high pollen season.",
            "Use air purifiers.",
            "Shower after outdoor exposure.",
            "Rinse nasal passages with saline."
        ));
        allergyRhinitis.setWarningSigns(Arrays.asList(
            "Symptoms severely affecting daily functioning.",
            "Development of asthma-like symptoms.",
            "Severe allergic reaction (difficulty breathing, throat swelling)."
        ));
        allergyRhinitis.setMedicalAttentionAdvice("See a doctor for allergy testing and treatment options if symptoms significantly impact your quality of life.");
        addDisease(allergyRhinitis);

        // ---- 16. Urinary Tract Infection ----
        Disease uti = new Disease(16, "Urinary Tract Infection (UTI)",
            "A bacterial infection affecting any part of the urinary system. More common in women but can affect anyone.",
            "Urinary", "MODERATE");
        uti.addSymptomLink(new DiseaseSymptomLink(62, 3)); // painful urination
        uti.addSymptomLink(new DiseaseSymptomLink(61, 3)); // frequent urination
        uti.addSymptomLink(new DiseaseSymptomLink(64, 2)); // lower abdominal discomfort
        uti.addSymptomLink(new DiseaseSymptomLink(65, 2)); // foul-smelling urine
        uti.addSymptomLink(new DiseaseSymptomLink(1,  1)); // fever
        uti.addSymptomLink(new DiseaseSymptomLink(63, 1)); // blood in urine
        uti.setCommonSigns(Arrays.asList("Burning sensation when urinating","Frequent urge to urinate","Cloudy or strong-smelling urine","Pelvic discomfort","Low-grade fever"));
        uti.setSelfCareAdvice(Arrays.asList(
            "Drink plenty of water to help flush bacteria.",
            "Urinate frequently - do not hold urine.",
            "Avoid irritants: caffeine, alcohol, spicy foods.",
            "Seek medical evaluation for antibiotic treatment."
        ));
        uti.setWarningSigns(Arrays.asList(
            "High fever above 101\u00b0F with chills (may indicate kidney infection).",
            "Back or side pain below the ribs.",
            "Blood in urine.",
            "Vomiting.",
            "Symptoms not improving within 1-2 days of treatment."
        ));
        uti.setMedicalAttentionAdvice("UTIs typically require antibiotic treatment. Please consult a healthcare professional for proper diagnosis and treatment.");
        addDisease(uti);

        // ---- 17. Dehydration ----
        Disease dehydration = new Disease(17, "Dehydration",
            "Occurs when you lose more fluids than you take in. Can range from mild to severe.",
            "General", "MODERATE");
        dehydration.addSymptomLink(new DiseaseSymptomLink(6,  3)); // dizziness
        dehydration.addSymptomLink(new DiseaseSymptomLink(3,  2)); // fatigue
        dehydration.addSymptomLink(new DiseaseSymptomLink(11, 2)); // headache
        dehydration.addSymptomLink(new DiseaseSymptomLink(4,  2)); // weakness
        dehydration.addSymptomLink(new DiseaseSymptomLink(52, 1)); // muscle cramps
        dehydration.addSymptomLink(new DiseaseSymptomLink(61, 1)); // decreased urination
        dehydration.setCommonSigns(Arrays.asList("Thirst","Dark-colored urine","Fatigue","Dizziness","Headache","Decreased urination","Dry mouth"));
        dehydration.setSelfCareAdvice(Arrays.asList(
            "Drink water and electrolyte-containing fluids immediately.",
            "Oral rehydration solution (ORS) is very effective.",
            "Avoid caffeine and alcohol.",
            "Rest in a cool environment.",
            "Monitor urine color (pale yellow = well hydrated)."
        ));
        dehydration.setWarningSigns(Arrays.asList(
            "No urination for 8+ hours.",
            "Extreme thirst, confusion, or rapid heartbeat.",
            "Fainting or severe dizziness.",
            "Inability to keep fluids down."
        ));
        dehydration.setMedicalAttentionAdvice("Seek medical attention for severe dehydration, especially in children, elderly, or if unable to keep fluids down.");
        addDisease(dehydration);

        // ---- 18. Muscle Strain ----
        Disease muscleStrain = new Disease(18, "Muscle Strain",
            "Overstretching or tearing of muscle fibers, usually from physical activity or poor posture.",
            "Musculoskeletal", "LOW");
        muscleStrain.addSymptomLink(new DiseaseSymptomLink(46, 3)); // muscle pain
        muscleStrain.addSymptomLink(new DiseaseSymptomLink(50, 2)); // body ache
        muscleStrain.addSymptomLink(new DiseaseSymptomLink(57, 2)); // swelling
        muscleStrain.addSymptomLink(new DiseaseSymptomLink(51, 2)); // muscle weakness
        muscleStrain.setCommonSigns(Arrays.asList("Localized muscle pain","Stiffness","Swelling in affected area","Limited range of motion","Weakness"));
        muscleStrain.setSelfCareAdvice(Arrays.asList(
            "Rest the affected muscle.",
            "Apply ice for the first 48-72 hours (20 min on, 20 min off).",
            "Use compression bandage if available.",
            "Elevate the affected area if possible.",
            "After 72 hours, gentle heat may help."
        ));
        muscleStrain.setWarningSigns(Arrays.asList(
            "Severe pain that does not improve with rest.",
            "Significant swelling, bruising, or deformity.",
            "Inability to bear weight or move the affected area.",
            "Numbness or tingling."
        ));
        muscleStrain.setMedicalAttentionAdvice("See a doctor if pain is severe, swelling significant, or if the injury doesn't improve after a few days of rest.");
        addDisease(muscleStrain);

        // ---- 19. Iron Deficiency Pattern ----
        Disease ironDef = new Disease(19, "Iron Deficiency-like Symptom Pattern",
            "A pattern of symptoms commonly associated with low iron levels. Must be confirmed with blood tests.",
            "General", "MODERATE");
        ironDef.addSymptomLink(new DiseaseSymptomLink(3,  3)); // fatigue
        ironDef.addSymptomLink(new DiseaseSymptomLink(4,  3)); // weakness
        ironDef.addSymptomLink(new DiseaseSymptomLink(6,  2)); // dizziness
        ironDef.addSymptomLink(new DiseaseSymptomLink(26, 2)); // shortness of breath on exertion
        ironDef.addSymptomLink(new DiseaseSymptomLink(11, 1)); // headache
        ironDef.addSymptomLink(new DiseaseSymptomLink(10, 1)); // loss of appetite
        ironDef.setCommonSigns(Arrays.asList("Persistent fatigue and weakness","Pale skin","Shortness of breath on mild exertion","Dizziness","Cold hands and feet"));
        ironDef.setSelfCareAdvice(Arrays.asList(
            "Include iron-rich foods: red meat, beans, lentils, dark leafy vegetables, fortified cereals.",
            "Pair iron-rich foods with vitamin C to improve absorption.",
            "Avoid tea/coffee with meals (reduces iron absorption).",
            "Get a blood test for confirmation."
        ));
        ironDef.setWarningSigns(Arrays.asList(
            "Extreme fatigue interfering with daily activities.",
            "Chest pain or shortness of breath.",
            "Irregular heartbeat.",
            "Unusual cravings (ice, dirt - called pica)."
        ));
        ironDef.setMedicalAttentionAdvice("Get a blood test to confirm iron levels. Iron supplementation should be recommended by a healthcare professional.");
        addDisease(ironDef);

        // ---- 20. Vitamin Deficiency Pattern ----
        Disease vitD = new Disease(20, "Vitamin Deficiency-like Symptom Pattern",
            "A pattern of symptoms associated with vitamin deficiencies. Common in students and those with limited sun exposure or poor diet.",
            "General", "LOW");
        vitD.addSymptomLink(new DiseaseSymptomLink(3,  2)); // fatigue
        vitD.addSymptomLink(new DiseaseSymptomLink(50, 2)); // body ache
        vitD.addSymptomLink(new DiseaseSymptomLink(46, 2)); // muscle pain
        vitD.addSymptomLink(new DiseaseSymptomLink(14, 1)); // memory difficulty
        vitD.addSymptomLink(new DiseaseSymptomLink(5,  1)); // malaise
        vitD.addSymptomLink(new DiseaseSymptomLink(47, 1)); // joint pain
        vitD.setCommonSigns(Arrays.asList("Fatigue and tiredness","Bone/muscle aches","Brain fog","Mood changes","Frequent illness"));
        vitD.setSelfCareAdvice(Arrays.asList(
            "Spend time outdoors for natural vitamin D (sun exposure).",
            "Eat vitamin D-rich foods: fatty fish, eggs, fortified milk.",
            "Eat B12-rich foods: meat, dairy, eggs.",
            "Get blood tests to identify specific deficiencies."
        ));
        vitD.setWarningSigns(Arrays.asList(
            "Symptoms severely affecting daily function.",
            "Severe bone pain.",
            "Numbness or tingling in extremities."
        ));
        vitD.setMedicalAttentionAdvice("Get blood tests to identify specific vitamin deficiencies. Supplementation should be guided by a healthcare professional.");
        addDisease(vitD);

        // ---- 21. Dermatitis / Eczema ----
        Disease dermatitis = new Disease(21, "Dermatitis / Eczema",
            "Inflammation of the skin causing itchy, red, dry patches. Can be triggered by allergens, irritants, or stress.",
            "Skin", "LOW");
        dermatitis.addSymptomLink(new DiseaseSymptomLink(54, 3)); // itching
        dermatitis.addSymptomLink(new DiseaseSymptomLink(56, 3)); // dry skin
        dermatitis.addSymptomLink(new DiseaseSymptomLink(55, 2)); // redness
        dermatitis.addSymptomLink(new DiseaseSymptomLink(53, 2)); // rash
        dermatitis.addSymptomLink(new DiseaseSymptomLink(57, 1)); // swelling
        dermatitis.setCommonSigns(Arrays.asList("Dry itchy skin","Red or brownish patches","Small raised bumps","Thickened or cracked skin","Worse with scratching"));
        dermatitis.setSelfCareAdvice(Arrays.asList(
            "Moisturize affected areas frequently with gentle lotions.",
            "Avoid known triggers (soaps, detergents, certain fabrics).",
            "Use gentle, fragrance-free skincare products.",
            "Avoid scratching to prevent infection.",
            "Cool compresses may relieve itching."
        ));
        dermatitis.setWarningSigns(Arrays.asList(
            "Signs of skin infection (warmth, pus, increasing redness).",
            "Rash spreading rapidly.",
            "Fever accompanying skin symptoms.",
            "Severe itching disrupting sleep."
        ));
        dermatitis.setMedicalAttentionAdvice("See a dermatologist if skin symptoms are severe, persistent, or infected.");
        addDisease(dermatitis);

        // ---- 22. Urticaria / Hives ----
        Disease hives = new Disease(22, "Urticaria (Hives)",
            "An itchy skin reaction causing welts (hives). Can be triggered by allergens, stress, infections, or medications.",
            "Skin", "MODERATE");
        hives.addSymptomLink(new DiseaseSymptomLink(58, 3)); // hives
        hives.addSymptomLink(new DiseaseSymptomLink(54, 3)); // itching
        hives.addSymptomLink(new DiseaseSymptomLink(53, 2)); // rash
        hives.addSymptomLink(new DiseaseSymptomLink(55, 2)); // redness
        hives.addSymptomLink(new DiseaseSymptomLink(57, 1)); // swelling
        hives.setCommonSigns(Arrays.asList("Raised itchy welts (hives)","Red or skin-colored bumps","Swelling","Welts that change shape/location","Intense itching"));
        hives.setSelfCareAdvice(Arrays.asList(
            "Identify and avoid the trigger.",
            "Apply cool compresses to affected areas.",
            "Avoid tight clothing over affected areas.",
            "Avoid hot showers.",
            "Keep the area cool."
        ));
        hives.setWarningSigns(Arrays.asList(
            "Difficulty breathing or swallowing.",
            "Swelling of face, lips, or throat.",
            "Dizziness or fainting.",
            "Rapid heartbeat."
        ));
        hives.setMedicalAttentionAdvice("Seek emergency care immediately if you have difficulty breathing or throat swelling (signs of anaphylaxis). See a doctor for persistent or recurring hives.");
        addDisease(hives);

        // ---- 23. Conjunctivitis (Pink Eye) ----
        Disease conjunctivitis = new Disease(23, "Conjunctivitis (Pink Eye)",
            "Inflammation of the conjunctiva. Can be viral, bacterial, or allergic. Very common and usually mild.",
            "Eye", "LOW");
        conjunctivitis.addSymptomLink(new DiseaseSymptomLink(66, 3)); // eye redness
        conjunctivitis.addSymptomLink(new DiseaseSymptomLink(69, 3)); // watery eyes
        conjunctivitis.addSymptomLink(new DiseaseSymptomLink(67, 2)); // eye pain
        conjunctivitis.addSymptomLink(new DiseaseSymptomLink(54, 2)); // itching
        conjunctivitis.addSymptomLink(new DiseaseSymptomLink(70, 1)); // light sensitivity
        conjunctivitis.setCommonSigns(Arrays.asList("Eye redness","Watery or sticky discharge","Itching","Gritty feeling in eye","Swollen eyelids"));
        conjunctivitis.setSelfCareAdvice(Arrays.asList(
            "Clean eyes gently with clean, damp cloth.",
            "Do not share towels or pillowcases.",
            "Avoid touching eyes.",
            "Remove contact lenses until cleared.",
            "Wash hands frequently."
        ));
        conjunctivitis.setWarningSigns(Arrays.asList(
            "Severe eye pain.",
            "Vision changes.",
            "Severe sensitivity to light.",
            "Symptoms not improving after 1 week."
        ));
        conjunctivitis.setMedicalAttentionAdvice("See a doctor if symptoms are severe, vision is affected, or symptoms don't improve within a week.");
        addDisease(conjunctivitis);

        // ---- 24. Ear Infection Pattern ----
        Disease earInfection = new Disease(24, "Ear Infection Pattern",
            "Ear infections involve inflammation of the ear, often following a cold or respiratory infection.",
            "ENT", "LOW");
        earInfection.addSymptomLink(new DiseaseSymptomLink(71, 3)); // ear pain
        earInfection.addSymptomLink(new DiseaseSymptomLink(72, 2)); // hearing difficulty
        earInfection.addSymptomLink(new DiseaseSymptomLink(1,  2)); // fever
        earInfection.addSymptomLink(new DiseaseSymptomLink(29, 1)); // nasal congestion
        earInfection.addSymptomLink(new DiseaseSymptomLink(6,  1)); // dizziness
        earInfection.setCommonSigns(Arrays.asList("Ear pain","Muffled hearing","Feeling of fullness in ear","Fever","Possibly discharge from ear"));
        earInfection.setSelfCareAdvice(Arrays.asList(
            "Warm compress on the ear may help relieve pain.",
            "Stay hydrated.",
            "Keep ear dry.",
            "Seek medical evaluation for antibiotic treatment if bacterial."
        ));
        earInfection.setWarningSigns(Arrays.asList(
            "Discharge or fluid from the ear.",
            "Severe hearing loss.",
            "High fever.",
            "Dizziness or balance problems.",
            "Severe pain."
        ));
        earInfection.setMedicalAttentionAdvice("See a doctor for ear infection diagnosis and treatment. Do not insert anything into the ear canal.");
        addDisease(earInfection);

        // ---- 25. Minor Joint Strain ----
        Disease jointStrain = new Disease(25, "Minor Joint Strain / Sprain",
            "An injury to a ligament or joint, often from physical activity. Usually resolves with rest and care.",
            "Musculoskeletal", "LOW");
        jointStrain.addSymptomLink(new DiseaseSymptomLink(47, 3)); // joint pain
        jointStrain.addSymptomLink(new DiseaseSymptomLink(57, 2)); // swelling
        jointStrain.addSymptomLink(new DiseaseSymptomLink(55, 1)); // redness
        jointStrain.addSymptomLink(new DiseaseSymptomLink(51, 1)); // weakness
        jointStrain.setCommonSigns(Arrays.asList("Joint pain","Swelling","Limited movement","Tenderness to touch","Bruising"));
        jointStrain.setSelfCareAdvice(Arrays.asList(
            "RICE: Rest, Ice, Compression, Elevation.",
            "Apply ice for 20 minutes several times a day.",
            "Avoid activities that cause pain.",
            "After 48-72 hours, gentle movement may help."
        ));
        jointStrain.setWarningSigns(Arrays.asList(
            "Joint appears deformed.",
            "Inability to bear weight.",
            "Numbness around the joint.",
            "Severe pain.",
            "Significant swelling."
        ));
        jointStrain.setMedicalAttentionAdvice("See a doctor if unable to bear weight, the joint appears deformed, or pain is severe. X-ray may be needed.");
        addDisease(jointStrain);
    }

    // ==================== HEALTH TIPS ====================

    private void initializeHealthTips() {
        allHealthTips.add(new HealthTip(1,  "HYDRATION", "Stay Hydrated",        "Remember to drink enough fluids throughout the day, especially during hot weather or physical activity. Aim for 8 glasses (2 liters) of water daily.", "\uD83D\uDCA7"));
        allHealthTips.add(new HealthTip(2,  "SLEEP",     "Quality Sleep",        "Adults need 7-9 hours of sleep per night. Good sleep improves memory, mood, immune function, and overall health. Maintain a consistent sleep schedule.", "\uD83C\uDF19"));
        allHealthTips.add(new HealthTip(3,  "NUTRITION", "Balanced Diet",        "Include a variety of fruits, vegetables, whole grains, and lean proteins in your diet. Colorful plates are usually nutritious plates!", "\uD83E\uDD57"));
        allHealthTips.add(new HealthTip(4,  "EXERCISE",  "Stay Active",          "Aim for at least 30 minutes of moderate physical activity most days. Even short walks count! Physical activity boosts mood and health.", "\uD83C\uDFC3"));
        allHealthTips.add(new HealthTip(5,  "STRESS",    "Manage Stress",        "Chronic stress can affect physical health. Try deep breathing, meditation, or mindfulness for 10 minutes daily to reduce stress.", "\uD83E\uDDD8"));
        allHealthTips.add(new HealthTip(6,  "HYGIENE",   "Hand Hygiene",         "Wash hands for at least 20 seconds with soap and water, especially before eating and after using the bathroom. It is the best defense against many infections.", "\uD83E\uDDFC"));
        allHealthTips.add(new HealthTip(7,  "SEASONAL",  "Seasonal Health",      "During cold and flu season, boost your immunity with adequate sleep, nutrition, and hand hygiene. Stay warm and avoid touching your face in public.", "\uD83C\uDF21\uFE0F"));
        allHealthTips.add(new HealthTip(8,  "STUDY",     "Study Health",         "Take a 5-minute break every 25-30 minutes while studying. The Pomodoro technique helps maintain focus and reduces mental fatigue.", "\uD83D\uDCDA"));
        allHealthTips.add(new HealthTip(9,  "EYECARE",   "Eye Care",             "Follow the 20-20-20 rule: Every 20 minutes, look at something 20 feet away for 20 seconds to reduce eye strain from screen use.", "\uD83D\uDC41\uFE0F"));
        allHealthTips.add(new HealthTip(10, "HYDRATION", "Mindful Caffeine",     "Moderate caffeine (1-2 cups of tea/coffee) can be fine, but excessive amounts may cause anxiety, sleep disruption, or dehydration.", "\u2615"));
        allHealthTips.add(new HealthTip(11, "NUTRITION", "Eat More Greens",      "Leafy green vegetables are rich in iron, calcium, vitamins, and fiber. Try to include spinach, kale, or any local greens in your daily meals.", "\uD83C\uDF3F"));
        allHealthTips.add(new HealthTip(12, "SLEEP",     "Limit Screen Time",    "Avoid screens for at least 1 hour before bedtime. Blue light from phones disrupts melatonin production and delays sleep.", "\uD83D\uDCF1"));
        allHealthTips.add(new HealthTip(13, "EXERCISE",  "Move More Daily",      "Simple habit changes like taking the stairs, walking during phone calls, or doing stretches while studying add up to meaningful physical activity.", "\uD83E\uDDD7"));
        allHealthTips.add(new HealthTip(14, "STRESS",    "Deep Breathing",       "Try box breathing: inhale for 4 counts, hold for 4, exhale for 4, hold for 4. Repeat 5 times when feeling stressed or anxious.", "\uD83E\uDEB1"));
        allHealthTips.add(new HealthTip(15, "EYECARE",   "Protect Your Eyes",    "Wear sunglasses outdoors to protect eyes from UV radiation. Good quality sunglasses reduce the risk of cataracts and other eye conditions.", "\u2600\uFE0F"));
        allHealthTips.add(new HealthTip(16, "NUTRITION", "Breakfast Matters",    "Eating a nutritious breakfast kickstarts your metabolism, improves concentration, and gives you energy to start the day well.", "\uD83C\uDF73"));
        allHealthTips.add(new HealthTip(17, "HYGIENE",   "Oral Hygiene",         "Brush your teeth twice daily and floss once a day. Good oral hygiene is linked to better overall health, including heart health.", "\uD83E\uDDB7"));
        allHealthTips.add(new HealthTip(18, "SLEEP",     "Consistent Schedule",  "Going to bed and waking up at the same time every day helps regulate your internal clock, making it easier to fall asleep and wake up refreshed.", "\u23F0"));
        allHealthTips.add(new HealthTip(19, "STRESS",    "Connect Socially",     "Maintaining social connections is important for mental health. Even a short conversation with a friend or family member can boost your mood.", "\uD83D\uDC6B"));
        allHealthTips.add(new HealthTip(20, "EXERCISE",  "Stretch Daily",        "Spending just 5-10 minutes stretching daily improves flexibility, reduces muscle tension, and can help prevent injuries.", "\uD83E\uDD38"));
    }

    // ==================== PUBLIC ACCESSORS ====================

    public List<Symptom> getAllSymptoms() { return Collections.unmodifiableList(allSymptoms); }
    public List<Disease> getAllDiseases() { return Collections.unmodifiableList(allDiseases); }
    public List<HealthTip> getAllHealthTips() { return Collections.unmodifiableList(allHealthTips); }

    public Symptom getSymptomById(int id) { return symptomsById.get(id); }
    public Symptom getSymptomByName(String name) {
        if (name == null) return null;
        return symptomsByName.get(name.toLowerCase());
    }
    public Disease getDiseaseById(int id) { return diseasesById.get(id); }

    public List<Symptom> searchSymptoms(String query) {
        if (query == null || query.isBlank()) return getAllSymptoms();
        String q = query.toLowerCase().trim();
        List<Symptom> results = new ArrayList<>();
        for (Symptom s : allSymptoms) {
            if (s.getName().toLowerCase().contains(q) || s.getCategory().toLowerCase().contains(q)) {
                results.add(s);
            }
        }
        return results;
    }

    public List<Symptom> getSymptomsByCategory(String category) {
        List<Symptom> results = new ArrayList<>();
        for (Symptom s : allSymptoms) {
            if (s.getCategory().equalsIgnoreCase(category)) results.add(s);
        }
        return results;
    }

    public List<String> getAllCategories() {
        List<String> cats = new ArrayList<>(Arrays.asList(
            "General","Neurological","Respiratory","Digestive",
            "Musculoskeletal","Skin","Urinary","Eye","ENT"
        ));
        return cats;
    }

    public HealthTip getDailyTip() {
        int dayOfYear = java.time.LocalDate.now().getDayOfYear();
        return allHealthTips.get(dayOfYear % allHealthTips.size());
    }

    public HealthTip getRandomTip() {
        Random rand = new Random();
        return allHealthTips.get(rand.nextInt(allHealthTips.size()));
    }
}
