import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { BADGES } from '../types';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { 
  User, 
  Flame, 
  Award, 
  Scale, 
  Save, 
  CheckCircle2,
  Activity
} from 'lucide-react';

export const Profile: React.FC = () => {
  const { user, updateUser } = useAuth();

  const [name, setName] = useState(user?.name || '');
  const [age, setAge] = useState(user?.age?.toString() || '21');
  const [gender, setGender] = useState(user?.gender || 'Male');
  const [height, setHeight] = useState(user?.height?.toString() || '175');
  const [weight, setWeight] = useState(user?.weight?.toString() || '70');
  const [bloodGroup, setBloodGroup] = useState(user?.bloodGroup || 'O+');
  const [allergies, setAllergies] = useState(user?.allergies || 'Penicillin');
  const [existingConditions, setExistingConditions] = useState(user?.existingConditions || 'Mild Seasonal Asthma');
  const [emergencyContact, setEmergencyContact] = useState(user?.emergencyContact || '+91 98765 43210');
  const [saved, setSaved] = useState(false);

  // Calculate BMI
  const hM = Number(height) / 100;
  const wKg = Number(weight);
  const bmi = hM > 0 ? (wKg / (hM * hM)).toFixed(1) : '0';
  const bmiNum = Number(bmi);

  const getBmiCategory = (val: number) => {
    if (val < 18.5) return { label: 'Underweight', color: 'text-amber-400 bg-amber-500/10 border-amber-500/20' };
    if (val < 25) return { label: 'Normal / Healthy Weight', color: 'text-emerald-400 bg-emerald-500/10 border-emerald-500/20' };
    if (val < 30) return { label: 'Overweight', color: 'text-amber-400 bg-amber-500/10 border-amber-500/20' };
    return { label: 'Obese Range', color: 'text-rose-400 bg-rose-500/10 border-rose-500/20' };
  };

  const bmiCat = getBmiCategory(bmiNum);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await updateUser({
      name,
      age: Number(age) || 20,
      gender,
      height: Number(height) || 170,
      weight: Number(weight) || 70,
      bloodGroup,
      allergies,
      existingConditions,
      emergencyContact
    });

    setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  };

  const badgeEntries = Object.entries(BADGES);
  const userBadges = user?.badges || [];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl md:text-3xl font-bold text-white tracking-tight flex items-center gap-3">
          <User className="h-8 w-8 text-cyan-400" />
          Health Profile & Vitals
        </h1>
        <p className="text-slate-400 text-sm mt-1">
          Manage your baseline physiological parameters, allergy alerts, and emergency contact information.
        </p>
      </div>

      {saved && (
        <div className="bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 p-4 rounded-xl flex items-center gap-3 animate-in fade-in">
          <CheckCircle2 className="h-5 w-5 flex-shrink-0" />
          <p className="text-sm font-medium">Health profile successfully updated!</p>
        </div>
      )}

      {/* Top Stats Overview */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="p-5 flex items-center gap-4 bg-gradient-to-br from-cyan-950/30 to-slate-900 border-cyan-500/20">
          <div className="w-12 h-12 rounded-xl bg-cyan-500/10 border border-cyan-500/30 flex items-center justify-center flex-shrink-0">
            <Scale className="h-6 w-6 text-cyan-400" />
          </div>
          <div>
            <div className="text-xs text-slate-400 font-medium">Body Mass Index (BMI)</div>
            <div className="text-2xl font-bold text-white flex items-center gap-2 mt-0.5">
              {bmi}
              <span className={`text-[10px] px-2 py-0.5 rounded-full border font-semibold ${bmiCat.color}`}>
                {bmiCat.label}
              </span>
            </div>
          </div>
        </Card>

        <Card className="p-5 flex items-center gap-4 bg-gradient-to-br from-amber-950/20 to-slate-900 border-amber-500/20">
          <div className="w-12 h-12 rounded-xl bg-amber-500/10 border border-amber-500/30 flex items-center justify-center flex-shrink-0">
            <Flame className="h-6 w-6 text-amber-400" />
          </div>
          <div>
            <div className="text-xs text-slate-400 font-medium">Active Health Streak</div>
            <div className="text-2xl font-bold text-amber-400 mt-0.5 flex items-center gap-1.5">
              {user?.streakDays || 0} Days <span className="text-xs text-slate-400 font-normal">in a row 🔥</span>
            </div>
          </div>
        </Card>

        <Card className="p-5 flex items-center gap-4 bg-gradient-to-br from-emerald-950/20 to-slate-900 border-emerald-500/20">
          <div className="w-12 h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/30 flex items-center justify-center flex-shrink-0">
            <Award className="h-6 w-6 text-emerald-400" />
          </div>
          <div>
            <div className="text-xs text-slate-400 font-medium">Unlocked Badges</div>
            <div className="text-2xl font-bold text-emerald-400 mt-0.5">
              {userBadges.length} <span className="text-xs text-slate-400 font-normal">/ {badgeEntries.length} Badges</span>
            </div>
          </div>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Profile Form */}
        <div className="lg:col-span-2">
          <Card className="p-6 md:p-8">
            <form onSubmit={handleSubmit} className="space-y-6">
              <h3 className="text-lg font-bold text-white border-b border-slate-800 pb-3 flex items-center gap-2">
                <Activity className="h-5 w-5 text-cyan-400" />
                Personal & Medical Parameters
              </h3>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1.5">Full Name</label>
                  <input
                    type="text"
                    required
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-cyan-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1.5">Age</label>
                  <input
                    type="number"
                    required
                    min="1"
                    max="120"
                    value={age}
                    onChange={(e) => setAge(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-cyan-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1.5">Biological Gender</label>
                  <select
                    value={gender}
                    onChange={(e) => setGender(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-cyan-500"
                  >
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                    <option value="Other">Other / Non-Binary</option>
                    <option value="Prefer not to say">Prefer not to say</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1.5">Blood Group (Optional)</label>
                  <select
                    value={bloodGroup}
                    onChange={(e) => setBloodGroup(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-cyan-500"
                  >
                    <option value="A+">A+</option>
                    <option value="A-">A-</option>
                    <option value="B+">B+</option>
                    <option value="B-">B-</option>
                    <option value="AB+">AB+</option>
                    <option value="AB-">AB-</option>
                    <option value="O+">O+</option>
                    <option value="O-">O-</option>
                    <option value="Unknown">Unknown</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1.5">Height (cm)</label>
                  <input
                    type="number"
                    required
                    min="50"
                    max="250"
                    value={height}
                    onChange={(e) => setHeight(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-cyan-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1.5">Weight (kg)</label>
                  <input
                    type="number"
                    required
                    min="20"
                    max="300"
                    value={weight}
                    onChange={(e) => setWeight(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-cyan-500"
                  />
                </div>
              </div>

              <div className="space-y-4 pt-2 border-t border-slate-800">
                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1.5">
                    Known Allergies
                  </label>
                  <input
                    type="text"
                    placeholder="e.g., Penicillin, Peanuts, Sulfa drugs, Shellfish"
                    value={allergies}
                    onChange={(e) => setAllergies(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-cyan-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1.5">
                    Pre-existing Medical Conditions
                  </label>
                  <input
                    type="text"
                    placeholder="e.g., Hypertension, Type 2 Diabetes, Asthma, None"
                    value={existingConditions}
                    onChange={(e) => setExistingConditions(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-cyan-500"
                  />
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-300 mb-1.5">
                    Emergency Contact Name & Phone
                  </label>
                  <input
                    type="text"
                    placeholder="e.g., Dr. Sharma / +91 98765 43210"
                    value={emergencyContact}
                    onChange={(e) => setEmergencyContact(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2 text-sm text-white focus:outline-none focus:border-cyan-500"
                  />
                </div>
              </div>

              <div className="pt-2">
                <Button type="submit" variant="primary" className="w-full py-2.5">
                  <Save className="h-4 w-4 mr-2" />
                  Save Changes to Profile
                </Button>
              </div>
            </form>
          </Card>
        </div>

        {/* Badges and Gamification */}
        <div className="space-y-6">
          <Card className="p-6">
            <h3 className="text-base font-bold text-white mb-2 flex items-center gap-2">
              <Award className="h-5 w-5 text-amber-400" />
              Health Milestones & Badges
            </h3>
            <p className="text-xs text-slate-400 mb-4">
              Earn badges by maintaining check-in streaks, logging symptoms, and completing health evaluations.
            </p>

            <div className="space-y-3">
              {badgeEntries.map(([key, label]) => {
                const isUnlocked = userBadges.includes(key);
                return (
                  <div
                    key={key}
                    className={`p-3 rounded-xl border flex items-center gap-3 transition-all ${
                      isUnlocked
                        ? 'bg-slate-900/80 border-slate-700/80'
                        : 'bg-slate-950/40 border-slate-900 opacity-50'
                    }`}
                  >
                    <div className="flex-1">
                      <div className="flex items-center justify-between">
                        <span className={`text-xs font-bold ${isUnlocked ? 'text-white' : 'text-slate-500'}`}>
                          {label}
                        </span>
                        {isUnlocked && (
                          <span className="text-[10px] font-semibold text-emerald-400 bg-emerald-500/10 px-1.5 py-0.5 rounded border border-emerald-500/20">
                            Unlocked
                          </span>
                        )}
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </Card>
        </div>
      </div>
    </div>
  );
};
