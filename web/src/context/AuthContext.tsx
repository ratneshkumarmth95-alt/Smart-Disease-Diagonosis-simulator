import React, { createContext, useContext, useState, useEffect } from 'react';
import { UserProfile, BADGES } from '../types';
import { LocalStore } from '../lib/storage';
import { supabase, isSupabaseConfigured } from '../lib/supabase';

interface AuthContextType {
  user: UserProfile | null;
  isDemo: boolean;
  isLoading: boolean;
  login: (username: string, pass: string) => Promise<{ success: boolean; error?: string }>;
  register: (
    username: string,
    pass: string,
    name: string,
    age: number,
    gender: string
  ) => Promise<{ success: boolean; error?: string }>;
  enterDemoMode: () => void;
  logout: () => void;
  updateUser: (fields: Partial<UserProfile>) => Promise<boolean>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<UserProfile | null>(null);
  const [isDemo, setIsDemo] = useState<boolean>(true);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    async function loadSession() {
      setIsLoading(true);
      if (isSupabaseConfigured && supabase) {
        try {
          const { data: { session } } = await supabase.auth.getSession();
          if (session?.user) {
            const { data: profile } = await supabase
              .from('profiles')
              .select('*')
              .eq('id', session.user.id)
              .single();

            if (profile) {
              setUser({
                id: profile.id,
                username: profile.username || session.user.email?.split('@')[0] || 'user',
                name: profile.name,
                age: profile.age,
                gender: profile.gender,
                height: profile.height,
                weight: profile.weight,
                bloodGroup: profile.blood_group,
                allergies: profile.allergies,
                existingConditions: profile.existing_conditions,
                emergencyContact: profile.emergency_contact,
                streakDays: profile.streak_days || 0,
                totalCheckIns: profile.total_check_ins || 0,
                badges: profile.badges || [],
                lastCheckIn: profile.last_check_in,
              });
              setIsDemo(false);
              setIsLoading(false);
              return;
            }
          }
        } catch (err) {
          console.warn('Supabase session load error, falling back to local mode', err);
        }
      }

      // Local storage fallback
      const local = LocalStore.getUser();
      if (local) {
        setUser(local);
        setIsDemo(Boolean(localStorage.getItem('sdds_is_demo_mode')));
      } else {
        // Automatically provide demo mode user on initial visit
        const demo = LocalStore.initDemoData();
        setUser(demo);
        setIsDemo(true);
      }
      setIsLoading(false);
    }

    loadSession();
  }, []);

  const login = async (username: string, pass: string): Promise<{ success: boolean; error?: string }> => {
    if (!username.trim() || !pass.trim()) {
      return { success: false, error: 'Please provide both username and password.' };
    }

    if (isSupabaseConfigured && supabase) {
      try {
        const email = username.includes('@') ? username : `${username}@smartdiagnosis.local`;
        const { data, error } = await supabase.auth.signInWithPassword({ email, password: pass });
        if (error) return { success: false, error: error.message };

        if (data.user) {
          const { data: profile } = await supabase
            .from('profiles')
            .select('*')
            .eq('id', data.user.id)
            .single();

          if (profile) {
            const loadedUser: UserProfile = {
              id: profile.id,
              username: profile.username || username,
              name: profile.name,
              age: profile.age,
              gender: profile.gender,
              height: profile.height,
              weight: profile.weight,
              bloodGroup: profile.blood_group,
              allergies: profile.allergies,
              existingConditions: profile.existing_conditions,
              emergencyContact: profile.emergency_contact,
              streakDays: profile.streak_days || 0,
              totalCheckIns: profile.total_check_ins || 0,
              badges: profile.badges || [],
              lastCheckIn: profile.last_check_in,
            };
            setUser(loadedUser);
            setIsDemo(false);
            LocalStore.setUser(loadedUser);
            return { success: true };
          }
        }
      } catch (err: unknown) {
        return { success: false, error: err instanceof Error ? err.message : 'Login failed' };
      }
    }

    // Local Mode login
    const newUser: UserProfile = {
      id: `local-${Date.now()}`,
      username,
      name: username.charAt(0).toUpperCase() + username.slice(1),
      age: 24,
      gender: 'Prefer not to say',
      streakDays: 1,
      totalCheckIns: 1,
      badges: [BADGES.FIRST_CHECKIN],
    };
    setUser(newUser);
    LocalStore.setUser(newUser);
    setIsDemo(false);
    return { success: true };
  };

  const register = async (
    username: string,
    pass: string,
    name: string,
    age: number,
    gender: string
  ): Promise<{ success: boolean; error?: string }> => {
    if (!username.trim() || !name.trim() || pass.length < 6) {
      return { success: false, error: 'Password must be at least 6 characters and all fields filled.' };
    }

    if (isSupabaseConfigured && supabase) {
      try {
        const email = username.includes('@') ? username : `${username}@smartdiagnosis.local`;
        const { data, error } = await supabase.auth.signUp({ email, password: pass });
        if (error) return { success: false, error: error.message };

        if (data.user) {
          const newProfile = {
            id: data.user.id,
            username,
            name,
            age,
            gender,
            streak_days: 0,
            total_check_ins: 0,
            badges: [],
          };
          await supabase.from('profiles').insert(newProfile);

          const loadedUser: UserProfile = {
            id: data.user.id,
            username,
            name,
            age,
            gender,
            streakDays: 0,
            totalCheckIns: 0,
            badges: [],
          };
          setUser(loadedUser);
          setIsDemo(false);
          LocalStore.setUser(loadedUser);
          return { success: true };
        }
      } catch (err: unknown) {
        return { success: false, error: err instanceof Error ? err.message : 'Registration failed' };
      }
    }

    // Local Mode Register
    const localUser: UserProfile = {
      id: `user-${Date.now()}`,
      username,
      name,
      age,
      gender,
      streakDays: 0,
      totalCheckIns: 0,
      badges: [],
    };
    setUser(localUser);
    LocalStore.setUser(localUser);
    setIsDemo(false);
    return { success: true };
  };

  const enterDemoMode = () => {
    const demo = LocalStore.initDemoData();
    setUser(demo);
    setIsDemo(true);
  };

  const logout = async () => {
    if (isSupabaseConfigured && supabase) {
      try {
        await supabase.auth.signOut();
      } catch {
        // ignore
      }
    }
    LocalStore.setUser(null);
    setUser(null);
  };

  const updateUser = async (fields: Partial<UserProfile>): Promise<boolean> => {
    if (!user) return false;
    const updated = { ...user, ...fields };
    setUser(updated);
    LocalStore.setUser(updated);

    if (isSupabaseConfigured && supabase && !isDemo) {
      try {
        await supabase
          .from('profiles')
          .update({
            name: updated.name,
            age: updated.age,
            gender: updated.gender,
            height: updated.height,
            weight: updated.weight,
            blood_group: updated.bloodGroup,
            allergies: updated.allergies,
            existing_conditions: updated.existingConditions,
            emergency_contact: updated.emergencyContact,
            streak_days: updated.streakDays,
            total_check_ins: updated.totalCheckIns,
            badges: updated.badges,
            last_check_in: updated.lastCheckIn,
          })
          .eq('id', user.id);
      } catch (err) {
        console.error('Supabase updateProfile error', err);
      }
    }
    return true;
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isDemo,
        isLoading,
        login,
        register,
        enterDemoMode,
        logout,
        updateUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within an AuthProvider');
  return context;
};
