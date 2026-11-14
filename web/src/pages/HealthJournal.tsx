import React, { useState } from 'react';
import { useData } from '../context/DataContext';
import { MoodType } from '../types';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Modal } from '../components/ui/Modal';
import { 
  BookOpen, 
  PlusCircle, 
  Trash2, 
  Calendar, 
  Smile, 
  Meh, 
  Frown, 
  Search
} from 'lucide-react';

export const HealthJournal: React.FC = () => {
  const { journalEntries, addJournalEntry, deleteJournalEntry } = useData();
  const [isAddOpen, setIsAddOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedMoodFilter, setSelectedMoodFilter] = useState<string>('ALL');

  // Form states
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [mood, setMood] = useState<MoodType>('GOOD');

  const moods: { label: string; value: MoodType; icon: React.ReactNode }[] = [
    { label: 'Great', value: 'GREAT', icon: <Smile className="h-4 w-4 text-emerald-400" /> },
    { label: 'Good', value: 'GOOD', icon: <Smile className="h-4 w-4 text-cyan-400" /> },
    { label: 'Okay', value: 'OKAY', icon: <Meh className="h-4 w-4 text-blue-400" /> },
    { label: 'Bad', value: 'BAD', icon: <Frown className="h-4 w-4 text-amber-400" /> },
    { label: 'Terrible', value: 'TERRIBLE', icon: <Frown className="h-4 w-4 text-rose-400" /> },
  ];

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim() || !content.trim()) return;

    await addJournalEntry({
      title: title.trim(),
      content: content.trim(),
      mood,
      entryDate: new Date().toISOString()
    });

    setTitle('');
    setContent('');
    setMood('GOOD');
    setIsAddOpen(false);
  };

  const filteredEntries = journalEntries.filter(entry => {
    const matchesSearch = entry.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          entry.content.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesMood = selectedMoodFilter === 'ALL' || entry.mood === selectedMoodFilter;
    return matchesSearch && matchesMood;
  });

  const getMoodBadge = (m: MoodType) => {
    switch (m) {
      case 'GREAT':
        return <Badge variant="success"><Smile className="h-3 w-3 mr-1" /> Great</Badge>;
      case 'GOOD':
        return <Badge variant="info"><Smile className="h-3 w-3 mr-1" /> Good</Badge>;
      case 'OKAY':
        return <Badge variant="default"><Meh className="h-3 w-3 mr-1" /> Okay</Badge>;
      case 'BAD':
        return <Badge variant="warning"><Frown className="h-3 w-3 mr-1" /> Bad</Badge>;
      case 'TERRIBLE':
        return <Badge variant="danger"><Frown className="h-3 w-3 mr-1" /> Terrible</Badge>;
      default:
        return <Badge variant="default">{m}</Badge>;
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl md:text-3xl font-bold text-white tracking-tight flex items-center gap-3">
            <BookOpen className="h-8 w-8 text-cyan-400" />
            Health & Reflection Journal
          </h1>
          <p className="text-slate-400 text-sm mt-1">
            Maintain daily notes on mood, medications, diet effects, or lifestyle patterns.
          </p>
        </div>

        <Button
          onClick={() => setIsAddOpen(true)}
          variant="primary"
          className="flex items-center gap-2 self-start md:self-auto"
        >
          <PlusCircle className="h-4 w-4" />
          New Journal Entry
        </Button>
      </div>

      {/* Filter and Search Bar */}
      <div className="flex flex-col sm:flex-row gap-3 items-center justify-between bg-slate-900/60 p-3 rounded-xl border border-slate-800">
        <div className="relative w-full sm:w-80">
          <Search className="absolute left-3 top-2.5 h-4 w-4 text-slate-400" />
          <input
            type="text"
            placeholder="Search notes or reflections..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg pl-9 pr-3 py-1.5 text-xs text-white focus:outline-none focus:border-cyan-500"
          />
        </div>

        <div className="flex items-center gap-1.5 overflow-x-auto w-full sm:w-auto pb-1 sm:pb-0">
          <span className="text-xs text-slate-400 mr-1 hidden lg:inline">Filter:</span>
          {['ALL', 'GREAT', 'GOOD', 'OKAY', 'BAD', 'TERRIBLE'].map((m) => (
            <button
              key={m}
              onClick={() => setSelectedMoodFilter(m)}
              className={`px-2.5 py-1 rounded-lg text-xs font-medium transition-all ${
                selectedMoodFilter === m
                  ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/40'
                  : 'text-slate-400 hover:text-white hover:bg-slate-800'
              }`}
            >
              {m}
            </button>
          ))}
        </div>
      </div>

      {/* Journal Cards */}
      {journalEntries.length === 0 ? (
        <Card className="p-12 text-center">
          <BookOpen className="h-12 w-12 text-slate-600 mx-auto mb-3" />
          <h3 className="text-lg font-bold text-white">Your Health Journal is Empty</h3>
          <p className="text-sm text-slate-400 max-w-md mx-auto mt-1 mb-6">
            Start keeping a log of how physical activities, meals, medications, and emotional factors impact your overall health.
          </p>
          <Button onClick={() => setIsAddOpen(true)} variant="outline">
            Write First Entry
          </Button>
        </Card>
      ) : filteredEntries.length === 0 ? (
        <Card className="p-8 text-center text-slate-400 text-sm">
          No journal entries match your filters.
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {filteredEntries.slice().reverse().map((entry, idx) => (
            <Card key={entry.id ?? idx} className="p-5 flex flex-col justify-between hover:border-slate-700 transition-all">
              <div className="space-y-3">
                <div className="flex items-start justify-between gap-2">
                  <h3 className="font-bold text-white text-base line-clamp-1">{entry.title}</h3>
                  {getMoodBadge(entry.mood)}
                </div>

                <p className="text-xs text-slate-300 leading-relaxed whitespace-pre-line line-clamp-6">
                  {entry.content}
                </p>
              </div>

              <div className="pt-4 mt-4 border-t border-slate-800/80 flex items-center justify-between text-xs text-slate-500">
                <span className="flex items-center gap-1.5">
                  <Calendar className="h-3.5 w-3.5" />
                  {new Date(entry.entryDate).toLocaleDateString(undefined, {
                    month: 'short',
                    day: 'numeric',
                    year: 'numeric'
                  })}
                </span>
                {entry.id && (
                  <button
                    onClick={() => deleteJournalEntry(entry.id!)}
                    className="text-slate-500 hover:text-rose-400 transition-colors p-1"
                    title="Delete Entry"
                  >
                    <Trash2 className="h-3.5 w-3.5" />
                  </button>
                )}
              </div>
            </Card>
          ))}
        </div>
      )}

      {/* New Entry Modal */}
      <Modal
        isOpen={isAddOpen}
        onClose={() => setIsAddOpen(false)}
        title="Create New Health Journal Entry"
      >
        <form onSubmit={handleCreate} className="space-y-4 text-sm">
          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1.5">
              Entry Title
            </label>
            <input
              type="text"
              required
              placeholder="e.g., Felt energized after morning jog, minor stiffness"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2.5 text-sm text-white focus:outline-none focus:border-cyan-500"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1.5">
              General State / Mood
            </label>
            <div className="grid grid-cols-5 gap-2">
              {moods.map((m) => (
                <button
                  key={m.value}
                  type="button"
                  onClick={() => setMood(m.value)}
                  className={`p-2.5 rounded-xl border flex flex-col items-center gap-1.5 text-xs font-medium transition-all ${
                    mood === m.value
                      ? 'bg-cyan-500/20 border-cyan-500 text-cyan-300'
                      : 'bg-slate-900 border-slate-800 text-slate-400 hover:border-slate-700'
                  }`}
                >
                  {m.icon}
                  <span>{m.label}</span>
                </button>
              ))}
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1.5">
              Journal Notes & Reflections
            </label>
            <textarea
              required
              rows={5}
              placeholder="Record details about your diet, medication changes, mental outlook, sleep quality, or physical comfort..."
              value={content}
              onChange={(e) => setContent(e.target.value)}
              className="w-full bg-slate-900 border border-slate-800 rounded-xl p-3 text-sm text-white focus:outline-none focus:border-cyan-500 placeholder-slate-500"
            />
          </div>

          <div className="flex justify-end gap-3 pt-3 border-t border-slate-800">
            <Button type="button" variant="ghost" onClick={() => setIsAddOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Save Entry
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
