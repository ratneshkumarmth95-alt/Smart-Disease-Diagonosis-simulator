# Smart Disease Diagnosis Simulator

> **College / second-Year Academic Java Project**  
> **Topic:** Health Symptom Analysis & Wellness Tracking Simulator  
> **Technology:** Java (Core + Swing GUI + OOP + Collections + JDBC/MySQL)  
> **Important Safety Note:** This is an educational simulation, **not a real medical diagnostic system**. All results are labeled as "Symptom Compatibility Patterns" and include recommendations to consult qualified healthcare professionals.

---

## 📌 1. Project Overview

The **Smart Disease Diagnosis Simulator** is a full-featured, desktop Java application that simulates modern symptom assessment, health tracking, and wellness habit formation. 

### Core Highlights:
- **Intelligent Symptom Checker**: Multi-step wizard covering 80+ symptoms across 9 body categories with dynamic severity rating (1–5) and illness duration.
- **Weighted Diagnosis Engine**: Evaluates symptoms using weight-based compatibility scoring (1 = common, 2 = important, 3 = highly characteristic), with duration and severity multipliers.
- **Emergency Warning System**: Automatically detects critical/life-threatening symptoms (e.g., severe breathing difficulty, sudden weakness, severe chest pain) and prioritizes urgent medical help over standard analysis.
- **Safe Advice System**: Provides non-prescriptive, lifestyle-oriented self-care guidance, red-flag monitoring warnings, and formal medical disclaimers.
- **"Why This Result?" Transparency**: Explains which reported symptoms matched and which key indicators were absent.
- **Daily Health Check-in**: Daily tracking of Energy, Sleep, Stress, and Mood to generate an educational Wellness Score (0–100).
- **Symptom Tracker & History**: Visual severity bars (`███░░`) and historical tracking across multiple days.
- **Health Journal**: Personal wellness reflections with mood indicators.
- **Educational Trivia Quiz**: "Test Your Health Knowledge" with instant answer explanations.
- **Gamification**: Consecutive check-in streaks and achievement badges (e.g., *7-Day Tracker*, *Hydration Tracker*, *Wellness Champion*).
- **Dual-Mode Persistence**: Works seamlessly in **Offline/In-Memory Mode** out of the box, with full **MySQL JDBC support** when a database is configured.

---

## 🗂️ 2. Package Architecture

```text
com.smartdiagnosis
│
├── model/                  # Data classes and Domain Models
│   ├── User.java               # User profile, streak, badges & BMI calculation
│   ├── Symptom.java            # Symptom definition (category, emergency flag)
│   ├── Disease.java            # Medical condition knowledge model
│   ├── DiseaseSymptomLink.java # Weighted link between disease and symptom
│   ├── ConditionMatch.java     # Match result with compatibility score
│   ├── DiagnosisResult.java    # Full session output with top matches
│   ├── HealthCheck.java        # Daily check-in & Wellness Score calculator
│   ├── SymptomLog.java         # Daily symptom severity tracking log
│   ├── JournalEntry.java       # Personal wellness notes
│   ├── HealthTip.java          # Educational rotating health tips
│   └── QuizQuestion.java       # Trivia question model with explanation
│
├── service/                # Business Logic & Algorithms
│   ├── KnowledgeBase.java      # In-memory repository (80 symptoms, 25 conditions, tips)
│   ├── DiagnosisEngine.java    # Weighted pattern matching & emergency detector
│   ├── AdviceEngine.java       # Safe self-care & report generator
│   ├── GamificationService.java# Badges, check-in streaks, progress bars
│   └── QuizService.java        # Health trivia questions provider
│
├── repository/             # Data Access Objects (DAO Pattern)
│   ├── UserDAO.java            # User CRUD with prepared statements
│   ├── HistoryDAO.java         # Diagnosis history persistence
│   ├── HealthCheckDAO.java     # Daily check-ins persistence
│   ├── SymptomLogDAO.java      # Symptom tracking log persistence
│   └── JournalDAO.java         # Journal entries persistence
│
├── database/               # Database Connection & Initialization
│   ├── DatabaseConnection.java # Thread-safe Singleton JDBC connection manager
│   ├── DatabaseInitializer.java# Auto-creates tables if DB is connected
│   └── schema.sql              # MySQL DDL schema script
│
├── ui/                     # Presentation Layer (Swing GUI)
│   ├── LoginUI.java            # Authentication, registration, and demo mode
│   ├── DashboardUI.java        # Main dashboard, quick actions, daily tip/fact
│   ├── SymptomCheckerUI.java   # 3-step diagnostic wizard
│   ├── TrackerUI.java          # Symptom logging & wellness trend charts
│   ├── HistoryUI.java          # Past analysis records
│   ├── JournalUI.java          # Health journal reader and creator
│   ├── ConditionsUI.java       # Searchable conditions reference cards
│   ├── QuizUI.java             # Interactive health quiz game
│   └── ProfileUI.java          # Health profile, BMI calculator, badges
│
├── utils/                  # Cross-cutting Utilities
│   ├── PasswordUtils.java      # SHA-256 salted password hashing
│   └── ValidationUtils.java    # Input validation, sanitization & formatting
│
└── main/                   # Application Bootstrap
    ├── AppContext.java         # Global application context (Singleton)
    └── Main.java               # Main entry point
```

---

## 🚀 3. How to Run

### Method 1: Double-Click (Windows)
Double-click `run.bat` in the root directory.

### Method 2: Command Line / Terminal

#### To Compile:
```cmd
build.bat
```
*(or manually: `javac -d bin -encoding UTF-8 @sources.txt`)*

#### To Run:
```cmd
run.bat
```
*(or manually: `java -cp "bin;lib/*" com.smartdiagnosis.main.Main`)*

---

## 🔑 4. Demo Login

When you launch the app, you can:
1. Click **"Try Demo (no account needed)"** on the login screen to enter immediately.
2. Or register a new local profile on the **Register** tab.

---

## 🗄️ 5. Optional MySQL Database Setup

The app works completely in offline mode by default. If you want persistent MySQL storage:
1. Open MySQL and run `database/schema.sql` (or create database `smart_diagnosis_db`).
2. Add `mysql-connector-j-8.x.x.jar` to the `lib/` folder.
3. Update your credentials in `src/main/java/com/smartdiagnosis/database/DatabaseConnection.java`:
   ```java
   private static final String DB_USER     = "root";
   private static final String DB_PASSWORD = "your_password";
   ```
4. Re-compile and run.

---

## 🎓 6. Academic Viva Highlights

When presenting this project:
1. **Separation of Concerns**: Model-View-Controller (MVC) separation where UI code (`ui`) is decoupled from business logic (`service`) and data access (`repository`).
2. **Design Patterns**:
   - **Singleton Pattern**: Used in `AppContext`, `DatabaseConnection`, and `KnowledgeBase`.
   - **DAO Pattern**: Used in all repository classes for clean JDBC abstraction.
   - **Factory/Composition Pattern**: Dynamic UI components, weighted condition matching links.
3. **Algorithms**:
   - Weighted scoring matching formula with non-linear multipliers.
   - Emergency short-circuiting override logic.
   - Streak calculation with calendar day boundaries.
4. **Security**: SHA-256 password hashing with cryptographically secure salt (`SecureRandom`). Input sanitization against injection attacks.
