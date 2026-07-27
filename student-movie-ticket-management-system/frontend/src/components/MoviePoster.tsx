function hashString(value: string): number {
  let hash = 0;
  for (let i = 0; i < value.length; i++) {
    hash = (hash * 31 + value.charCodeAt(i)) >>> 0;
  }
  return hash;
}

const GLOW_POSITIONS = [
  "at 30% 20%",
  "at 70% 15%",
  "at 50% 35%",
  "at 25% 45%",
  "at 75% 40%",
];

export function MoviePoster({ title, className = "" }: { title: string; className?: string }) {
  const hash = hashString(title);
  const glowPosition = GLOW_POSITIONS[hash % GLOW_POSITIONS.length];
  const initials = title
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((w) => w[0])
    .join("")
    .toUpperCase();

  return (
    <div
      className={`relative flex flex-col items-center justify-center overflow-hidden bg-linear-to-b from-slate-800 via-slate-900 to-slate-950 ${className}`}
    >
      <div
        className="absolute inset-0"
        style={{
          backgroundImage: `radial-gradient(circle ${glowPosition}, rgba(245,158,11,0.16), transparent 60%)`,
        }}
      />

      {/* Sprocket holes for a film-strip feel */}
      <div className="absolute inset-y-0 left-0 flex w-3 flex-col items-center justify-evenly py-3 sm:w-4">
        {Array.from({ length: 8 }).map((_, i) => (
          <span key={i} className="h-1.5 w-1.5 rounded-full bg-black/40 sm:h-2 sm:w-2" />
        ))}
      </div>
      <div className="absolute inset-y-0 right-0 flex w-3 flex-col items-center justify-evenly py-3 sm:w-4">
        {Array.from({ length: 8 }).map((_, i) => (
          <span key={i} className="h-1.5 w-1.5 rounded-full bg-black/40 sm:h-2 sm:w-2" />
        ))}
      </div>

      <div className="relative flex flex-col items-center gap-2 px-6">
        <span className="text-4xl opacity-90 drop-shadow-lg">🎬</span>
        <span className="text-xs font-bold uppercase tracking-[0.2em] text-amber-400/90">{initials}</span>
      </div>
    </div>
  );
}
