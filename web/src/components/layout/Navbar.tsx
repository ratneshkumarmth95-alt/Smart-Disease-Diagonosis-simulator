import React from 'react';
import { Menu, Flame, Sparkles } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useData } from '../../context/DataContext';
import { Button } from '../ui/Button';
import { Badge } from '../ui/Badge';
import { Link } from 'react-router-dom';

interface NavbarProps {
  onMobileMenuOpen: () => void;
  onOpenCheckIn: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({ onMobileMenuOpen, onOpenCheckIn }) => {
  const { user, isDemo } = useAuth();
  const { todayCheckIn } = useData();

  return (
    <header className="h-16 bg-slate-950/80 backdrop-blur-md border-b border-slate-800 sticky top-0 z-20 px-4 sm:px-6 flex items-center justify-between">
      {/* Left: Mobile menu button + Title */}
      <div className="flex items-center gap-3">
        <button
          onClick={onMobileMenuOpen}
          className="md:hidden p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-xl transition-colors"
          aria-label="Open mobile navigation"
        >
          <Menu className="w-5 h-5" />
        </button>

        <div className="hidden sm:flex items-center gap-2">
          <Badge variant="info" size="sm" className="font-medium">
            <span className="w-1.5 h-1.5 rounded-full bg-cyan-400 animate-pulse" />
            Educational Simulation
          </Badge>
          <span className="text-xs text-slate-400 hidden lg:inline">
            Results are for educational demonstration only. Always consult a healthcare professional.
          </span>
        </div>
      </div>

      {/* Right: Quick actions & user status */}
      <div className="flex items-center gap-3">
        {/* Streak Pill */}
        <Link
          to="/profile"
          className="flex items-center gap-1.5 bg-amber-950/40 hover:bg-amber-900/40 border border-amber-500/40 text-amber-300 px-3 py-1.5 rounded-xl text-xs font-bold transition-all shadow-sm"
          title="Daily Check-in Streak"
        >
          <Flame className="w-4 h-4 text-amber-400 fill-amber-400" />
          <span>{user?.streakDays || 0} Day Streak</span>
        </Link>

        {/* Check-In Pill */}
        {todayCheckIn ? (
          <Badge variant="success" size="md" className="hidden sm:inline-flex py-1.5 px-3">
            ✓ Checked in Today ({todayCheckIn.wellnessScore}/100)
          </Badge>
        ) : (
          <Button
            size="sm"
            variant="outline"
            leftIcon={<Sparkles className="w-3.5 h-3.5 text-cyan-400" />}
            onClick={onOpenCheckIn}
            className="hidden sm:inline-flex border-cyan-500/40 text-cyan-300 bg-cyan-950/40 hover:bg-cyan-900/40 font-semibold"
          >
            Check In Today
          </Button>
        )}

        {/* Demo indicator */}
        {isDemo && (
          <span className="hidden md:inline-block text-[11px] font-semibold text-slate-400 bg-slate-900 px-2.5 py-1 rounded-lg border border-slate-800">
            Demo Mode
          </span>
        )}

        {/* User Avatar */}
        <Link
          to="/profile"
          className="flex items-center gap-2 pl-2 border-l border-slate-800 text-slate-300 hover:text-white group"
        >
          <div className="w-8 h-8 rounded-full bg-cyan-500/20 text-cyan-300 font-bold text-xs flex items-center justify-center border border-cyan-500/40 group-hover:scale-105 transition-transform">
            {user?.name ? user.name.charAt(0).toUpperCase() : 'R'}
          </div>
          <span className="hidden xl:inline text-xs font-semibold text-slate-200">
            {user?.name || 'Ratnesh'}
          </span>
        </Link>
      </div>
    </header>
  );
};
