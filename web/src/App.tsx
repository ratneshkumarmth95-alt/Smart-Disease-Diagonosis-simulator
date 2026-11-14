import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { DataProvider } from './context/DataContext';
import { AppLayout } from './components/layout/AppLayout';

import { Login } from './pages/Login';
import { Dashboard } from './pages/Dashboard';
import { SymptomChecker } from './pages/SymptomChecker';
import { HealthTracker } from './pages/HealthTracker';
import { HealthHistory } from './pages/HealthHistory';
import { HealthJournal } from './pages/HealthJournal';
import { ConditionsLibrary } from './pages/ConditionsLibrary';
import { HealthQuiz } from './pages/HealthQuiz';
import { HealthTips } from './pages/HealthTips';
import { Profile } from './pages/Profile';

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen bg-slate-950 flex items-center justify-center">
        <div className="flex flex-col items-center gap-3">
          <div className="w-10 h-10 border-4 border-cyan-500/20 border-t-cyan-500 rounded-full animate-spin" />
          <p className="text-xs text-slate-400 font-medium">Initializing Health Simulator...</p>
        </div>
      </div>
    );
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  return <AppLayout>{children}</AppLayout>;
};

export const App: React.FC = () => {
  return (
    <BrowserRouter>
      <AuthProvider>
        <DataProvider>
          <Routes>
            <Route path="/login" element={<Login />} />
            
            <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
            <Route path="/checker" element={<ProtectedRoute><SymptomChecker /></ProtectedRoute>} />
            <Route path="/tracker" element={<ProtectedRoute><HealthTracker /></ProtectedRoute>} />
            <Route path="/history" element={<ProtectedRoute><HealthHistory /></ProtectedRoute>} />
            <Route path="/journal" element={<ProtectedRoute><HealthJournal /></ProtectedRoute>} />
            <Route path="/conditions" element={<ProtectedRoute><ConditionsLibrary /></ProtectedRoute>} />
            <Route path="/quiz" element={<ProtectedRoute><HealthQuiz /></ProtectedRoute>} />
            <Route path="/tips" element={<ProtectedRoute><HealthTips /></ProtectedRoute>} />
            <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />

            {/* Fallback route */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </DataProvider>
      </AuthProvider>
    </BrowserRouter>
  );
};

export default App;
