# Smart Disease Diagnosis Simulator — Modern Web Platform

> **Clinical Simulation & Preventive Wellness Platform**  
> Complete web modernization of the Smart Disease Diagnosis Simulator desktop system, built with React 19, TypeScript, Vite, Tailwind CSS, Recharts, and Supabase.

---

## ⚕️ Important Medical & Educational Disclaimer

**This software is strictly an educational simulation and health literacy tool.**  
It does **NOT** provide medical diagnoses, treatment plans, or definitive clinical assessments.  
All condition match calculations are displayed as **"Possible Conditions"** or **"Symptom Compatibility"** representing mathematical pattern overlap. Users experiencing severe, worsening, or emergency symptoms must seek care from licensed medical professionals immediately.

---

## 🚀 Key Features

1. **Intelligent Symptom Checker**:
   - 80 clinical symptoms across 9 anatomical categories.
   - 3-step interactive clinical evaluation wizard.
   - Severity rating (1–5) and duration weighting.
   - Red-flag emergency detection with immediate safety override.
   - Mathematical condition matching with "Why This Result?" explanations.
   - Plain-text diagnostic report export (`.txt`).

2. **Longitudinal Health & Symptom Tracker**:
   - Log individual symptom episodes with intensity, duration, and contextual notes.
   - Chronological ledger with visual severity bars.
   - 7-day wellness telemetry graphs and most frequent symptom recurrence charts.

3. **Conditions Knowledge Base**:
   - 25 comprehensive condition monographs with clinical risk levels.
   - Primary vs. secondary diagnostic marker weights.
   - Evidence-based self-care recommendations and physician escalation triggers.

4. **Health & Reflection Journal**:
   - Daily health journaling with emotional state tags (`GREAT`, `GOOD`, `OKAY`, `BAD`, `TERRIBLE`).
   - Keyword search and category filtering.

5. **Gamification & Daily Check-in**:
   - Interactive daily telemetry tracking (Energy, Sleep, Stress, Mood).
   - Streak counters with fire indicators 🔥.
   - 10 milestone badges (First Check-in, Hydration Tracker, 7-Day Tracker, etc.) with celebration confetti.

6. **Medical Trivia Quiz & Health Tips**:
   - Interactive 5-question trivia drawer with instant explanations.
   - 20 evidence-backed lifestyle tips with daily featured insights.
   - 12 fascinating human physiology facts.

7. **Patient Profile & Vitals**:
   - Body Mass Index (BMI) calculator with dynamic classification.
   - Allergies, pre-existing conditions, and emergency contacts.

8. **Dual-Mode Persistence (Zero Configuration Required)**:
   - **Local Demo Mode**: Works instantly out of the box using browser `localStorage` with rich pre-seeded mock records.
   - **Cloud Mode**: Seamlessly connects to Supabase PostgreSQL with Row Level Security when environment variables are supplied.

---

## 🛠️ Tech Stack

- **Framework**: React 19 + TypeScript + Vite
- **Styling**: Tailwind CSS + Glassmorphism + Custom Dark Slate Theme
- **Icons**: Lucide React
- **Data Visualizations**: Recharts
- **Animations & Effects**: Motion + Canvas Confetti
- **Testing**: Vitest
- **Database / Auth**: Supabase (PostgreSQL + RLS) with LocalStorage fallback

---

## 📦 Getting Started

### 1. Install Dependencies
```bash
cd web
npm install
```

### 2. Run Locally in Development Mode
```bash
npm run dev
```
The application will launch at `http://localhost:5173`. Click **"Try Demo Mode"** to test with preloaded mock records instantly!

### 3. Run Unit Tests
```bash
npm run test
```

### 4. Build for Production
```bash
npm run build
```

---

## ☁️ Deploying to Vercel

1. Push your repository to **GitHub**.
2. Log into your [Vercel Dashboard](https://vercel.com) and click **"Add New Project"**.
3. Import your repository.
4. Configure the project settings:
   - **Framework Preset**: `Vite`
   - **Root Directory**: `web`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
5. *(Optional)* Add Supabase environment variables:
   - `VITE_SUPABASE_URL` = your Supabase project URL
   - `VITE_SUPABASE_ANON_KEY` = your Supabase anon key
   *(If omitted, the app automatically runs in Local Demo Mode!)*
6. Click **Deploy**.

---

## 🗄️ Supabase Database Schema

To enable Supabase cloud persistence:
1. Create a project at [supabase.com](https://supabase.com).
2. Open the **SQL Editor** in your Supabase dashboard.
3. Paste and run the contents of [`web/supabase/schema.sql`](./supabase/schema.sql).
4. Copy your project URL and anon public key into `web/.env`.
