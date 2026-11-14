-- ============================================================
-- Smart Disease Diagnosis Simulator — Supabase PostgreSQL Schema
-- ============================================================

-- 1. Profiles Table (linked to Supabase auth.users)
CREATE TABLE IF NOT EXISTS public.profiles (
  id UUID REFERENCES auth.users ON DELETE CASCADE PRIMARY KEY,
  username TEXT UNIQUE,
  name TEXT NOT NULL,
  age INTEGER CHECK (age >= 1 AND age <= 120),
  gender TEXT,
  height NUMERIC,
  weight NUMERIC,
  blood_group TEXT,
  date_of_birth DATE,
  allergies TEXT,
  existing_conditions TEXT,
  emergency_contact TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  streak_days INTEGER DEFAULT 0,
  total_check_ins INTEGER DEFAULT 0,
  badges TEXT[] DEFAULT '{}',
  last_check_in DATE
);

-- 2. Health Checks Table (Daily check-ins)
CREATE TABLE IF NOT EXISTS public.health_checks (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
  check_date DATE NOT NULL,
  energy_level INTEGER CHECK (energy_level BETWEEN 1 AND 5),
  sleep_quality INTEGER CHECK (sleep_quality BETWEEN 1 AND 5),
  stress_level INTEGER CHECK (stress_level BETWEEN 1 AND 5),
  mood_level INTEGER CHECK (mood_level BETWEEN 1 AND 5),
  notes TEXT,
  wellness_score INTEGER CHECK (wellness_score BETWEEN 0 AND 100),
  created_at TIMESTAMPTZ DEFAULT NOW(),
  UNIQUE(user_id, check_date)
);

-- 3. Symptom Logs Table (Symptom tracker)
CREATE TABLE IF NOT EXISTS public.symptom_logs (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
  symptom_id INTEGER NOT NULL,
  symptom_name TEXT NOT NULL,
  log_date DATE NOT NULL,
  severity INTEGER CHECK (severity BETWEEN 1 AND 5),
  duration TEXT,
  notes TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 4. Diagnosis History Table
CREATE TABLE IF NOT EXISTS public.diagnosis_history (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
  analysis_date TIMESTAMPTZ DEFAULT NOW(),
  selected_symptoms JSONB NOT NULL,
  top_matches JSONB NOT NULL,
  emergency_warning BOOLEAN DEFAULT FALSE,
  emergency_message TEXT,
  overall_severity TEXT,
  duration TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 5. Health Journal Table
CREATE TABLE IF NOT EXISTS public.health_journal (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
  entry_date TIMESTAMPTZ DEFAULT NOW(),
  title TEXT NOT NULL,
  content TEXT NOT NULL,
  mood TEXT NOT NULL,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================
-- Row Level Security (RLS) Policies
-- Users can only read, insert, update, and delete their own data.
-- ============================================================

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.health_checks ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.symptom_logs ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.diagnosis_history ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.health_journal ENABLE ROW LEVEL SECURITY;

-- Profiles Policies
CREATE POLICY "Users can view their own profile"
  ON public.profiles FOR SELECT
  USING (auth.uid() = id);

CREATE POLICY "Users can update their own profile"
  ON public.profiles FOR UPDATE
  USING (auth.uid() = id);

CREATE POLICY "Users can insert their own profile"
  ON public.profiles FOR INSERT
  WITH CHECK (auth.uid() = id);

-- Health Checks Policies
CREATE POLICY "Users can manage their health checks"
  ON public.health_checks FOR ALL
  USING (auth.uid() = user_id);

-- Symptom Logs Policies
CREATE POLICY "Users can manage their symptom logs"
  ON public.symptom_logs FOR ALL
  USING (auth.uid() = user_id);

-- Diagnosis History Policies
CREATE POLICY "Users can manage their diagnosis history"
  ON public.diagnosis_history FOR ALL
  USING (auth.uid() = user_id);

-- Health Journal Policies
CREATE POLICY "Users can manage their journal entries"
  ON public.health_journal FOR ALL
  USING (auth.uid() = user_id);
