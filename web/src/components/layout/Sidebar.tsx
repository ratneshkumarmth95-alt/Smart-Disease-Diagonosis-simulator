import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import {
  LayoutDashboard,
  Stethoscope,
  Activity,
  History,
  BookOpen,
  Lightbulb,
  FileText,
  HelpCircle,
  User,
  LogOut,
  Sparkles,
  ChevronLeft,
  ChevronRight,
  ShieldCheck,
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { clsx } from 'clsx';

interface SidebarProps {
  isCollapsed: boolean;
  onToggleCollapse: () => void;
  onOpenCheckIn: () => void;
  mobileClose?: () => void;
}

export const Sidebar: React.FC<SidebarProps> = ({
  isCollapsed,
  onToggleCollapse,
  onOpenCheckIn,
  mobileClose,
}) => {
  const { user, isDemo, logout } = useAuth();
  const navigate = useNavigate();

  const navItems = [
    { to: '/', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/checker', label: 'Symptom Checker', icon: Stethoscope },
    { to: '/tracker', label: 'Health Tracker', icon: Activity },
    { to: '/history', label: 'Health History', icon: History },
    { to: '/journal', label: 'Health Journal', icon: BookOpen },
    { to: '/tips', label: 'Health Tips', icon: Lightbulb },
    { to: '/conditions', label: 'Conditions Library', icon: FileText },
    { to: '/quiz', label: 'Health Quiz', icon: HelpCircle },
    { to: '/profile', label: 'My Profile', icon: User },
  ];

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <aside
      className={clsx(
        'h-screen bg-navy-950 text-slate-300 flex flex-col border-r border-slate-800/80 transition-all duration-300 z-30 select-none relative',
        isCollapsed ? 'w-[76px]' : 'w-64'
      )}
    >
      {/* App Branding */}
      <div className="h-16 flex items-center justify-between px-4 border-b border-slate-800/80">
        <div className="flex items-center gap-3 overflow-hidden">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-brand-600 to-teal-400 flex items-center justify-center text-white font-bold shadow-md shadow-brand-500/20 shrink-0">
            <ShieldCheck className="w-6 h-6 text-white" />
          </div>
          {!isCollapsed && (
            <div className="flex flex-col min-w-0">
              <span className="text-sm font-bold tracking-tight text-white truncate">
                SmartDiagnosis
              </span>
              <span className="text-[10px] font-medium text-brand-400 tracking-wider uppercase">
                Simulator v2.0
              </span>
            </div>
          )}
        </div>

        {/* Desktop Collapse Toggle */}
        <button
          onClick={onToggleCollapse}
          className="hidden md:flex items-center justify-center w-7 h-7 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
          title={isCollapsed ? 'Expand sidebar' : 'Collapse sidebar'}
        >
          {isCollapsed ? <ChevronRight className="w-4 h-4" /> : <ChevronLeft className="w-4 h-4" />}
        </button>
      </div>

      {/* Demo Tag Notice */}
      {isDemo && !isCollapsed && (
        <div className="px-4 py-2 bg-amber-500/10 border-b border-amber-500/20 flex items-center justify-between">
          <span className="text-[11px] font-semibold text-amber-300 flex items-center gap-1.5">
            <span className="w-1.5 h-1.5 rounded-full bg-amber-400 animate-pulse" />
            Local Demo Mode
          </span>
          <span className="text-[10px] text-amber-200/60">Offline</span>
        </div>
      )}

      {/* Navigation links */}
      <nav className="flex-1 py-4 px-3 space-y-1 overflow-y-auto scrollbar-thin">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            onClick={mobileClose}
            className={({ isActive }) =>
              clsx(
                'flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-semibold transition-all duration-150 group relative',
                isActive
                  ? 'bg-brand-600 text-white shadow-sm shadow-brand-600/30'
                  : 'text-slate-400 hover:text-white hover:bg-slate-800/60'
              )
            }
            title={isCollapsed ? item.label : undefined}
          >
            <item.icon className="w-4 h-4 shrink-0" />
            {!isCollapsed && <span className="truncate">{item.label}</span>}
          </NavLink>
        ))}

        {/* Quick Check-in CTA Button inside Nav */}
        <div className="pt-4">
          <button
            onClick={() => {
              onOpenCheckIn();
              if (mobileClose) mobileClose();
            }}
            className={clsx(
              'w-full flex items-center justify-center gap-2 rounded-xl text-xs font-bold transition-all',
              'bg-gradient-to-r from-teal-500 to-emerald-500 text-white shadow-sm hover:from-teal-600 hover:to-emerald-600 p-2.5',
              isCollapsed && 'px-0'
            )}
            title="Daily Health Check-in"
          >
            <Sparkles className="w-4 h-4 shrink-0" />
            {!isCollapsed && <span>Daily Check-in</span>}
          </button>
        </div>
      </nav>

      {/* User profile & footer */}
      <div className="p-3 border-t border-slate-800/80 bg-navy-950/50">
        <div
          className={clsx(
            'flex items-center gap-3 p-2 rounded-xl bg-slate-900/60 border border-slate-800/60',
            isCollapsed ? 'justify-center' : 'justify-between'
          )}
        >
          <div className="flex items-center gap-2.5 overflow-hidden">
            <div className="w-8 h-8 rounded-lg bg-cyan-500/20 text-cyan-400 border border-cyan-500/30 flex items-center justify-center font-bold text-xs shrink-0">
              {user?.name ? user.name.charAt(0).toUpperCase() : 'R'}
            </div>
            {!isCollapsed && (
              <div className="min-w-0">
                <div className="text-xs font-semibold text-white truncate leading-tight">
                  {user?.name || 'Ratnesh'}
                </div>
                <div className="text-[10px] text-slate-400 flex items-center gap-1 mt-0.5">
                  <span className="text-amber-400 font-bold">🔥 {user?.streakDays || 0}d</span> streak
                </div>
              </div>
            )}
          </div>

          {!isCollapsed && (
            <button
              onClick={handleLogout}
              className="text-slate-400 hover:text-rose-400 p-1.5 rounded-lg hover:bg-slate-800 transition-colors"
              title="Logout"
            >
              <LogOut className="w-4 h-4" />
            </button>
          )}
        </div>
      </div>
    </aside>
  );
};
