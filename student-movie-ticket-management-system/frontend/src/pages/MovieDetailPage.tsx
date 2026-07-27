import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { fetchMovieById, fetchSeatsByShowtime, fetchShowtimesByMovie } from "../api/catalog";
import { toApiError } from "../api/client";
import { useAuth } from "../context/AuthContext";
import { Spinner } from "../components/Spinner";
import { MoviePoster } from "../components/MoviePoster";
import { formatDate, formatDuration, formatTime } from "../utils/datetime";
import { formatCurrency } from "../utils/pricing";
import type { Movie, Showtime } from "../types";

interface ShowtimeWithAvailability extends Showtime {
  availableSeats: number;
}

export function MovieDetailPage() {
  const { movieId } = useParams<{ movieId: string }>();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  const [movie, setMovie] = useState<Movie | null>(null);
  const [showtimes, setShowtimes] = useState<ShowtimeWithAvailability[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!movieId) return;
    let cancelled = false;
    setLoading(true);
    setError(null);

    Promise.all([fetchMovieById(movieId), fetchShowtimesByMovie(movieId)])
      .then(async ([movieResult, showtimeList]) => {
        const withAvailability = await Promise.all(
          showtimeList.map(async (st) => {
            const seats = await fetchSeatsByShowtime(st.showtimeId);
            return { ...st, availableSeats: seats.filter((s) => s.status === "AVAILABLE").length };
          })
        );
        if (cancelled) return;
        setMovie(movieResult);
        setShowtimes(
          withAvailability.sort((a, b) => a.startTime.localeCompare(b.startTime))
        );
      })
      .catch((err) => {
        if (!cancelled) setError(toApiError(err).message);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    return () => {
      cancelled = true;
    };
  }, [movieId]);

  const handleSelectShowtime = (showtimeId: string) => {
    if (!isAuthenticated) {
      navigate("/login", { state: { from: { pathname: `/showtimes/${showtimeId}` } } });
      return;
    }
    navigate(`/showtimes/${showtimeId}`);
  };

  if (loading) return <Spinner label="Đang tải thông tin phim..." />;
  if (error || !movie) {
    return (
      <div className="rounded-xl border border-red-500/30 bg-red-500/10 p-6 text-center text-red-300">
        {error ?? "Không tìm thấy phim"}
      </div>
    );
  }

  const groupedByDate = showtimes.reduce<Record<string, ShowtimeWithAvailability[]>>((acc, st) => {
    const key = formatDate(st.startTime);
    acc[key] = acc[key] ?? [];
    acc[key].push(st);
    return acc;
  }, {});

  return (
    <div className="flex flex-col gap-8">
      <Link to="/" className="w-fit text-sm text-slate-400 hover:text-white">
        ← Quay lại danh sách phim
      </Link>

      <div className="grid gap-6 sm:grid-cols-[200px_1fr]">
        <MoviePoster title={movie.title} className="aspect-[2/3] w-full rounded-2xl" />
        <div className="flex flex-col gap-3">
          <h1 className="text-2xl font-bold text-white sm:text-3xl">{movie.title}</h1>
          <div className="flex flex-wrap gap-2 text-xs">
            <span className="rounded-full border border-white/10 bg-white/5 px-2.5 py-1 text-slate-300">
              {movie.genre}
            </span>
            <span className="rounded-full border border-white/10 bg-white/5 px-2.5 py-1 text-slate-300">
              {formatDuration(movie.duration)}
            </span>
            <span className="rounded-full border border-white/10 bg-white/5 px-2.5 py-1 text-slate-300">
              {movie.ageLimit === 0 ? "Mọi lứa tuổi" : `${movie.ageLimit}+`}
            </span>
            <span className="rounded-full border border-emerald-500/30 bg-emerald-500/10 px-2.5 py-1 text-emerald-400">
              Đang chiếu
            </span>
          </div>
          {!isAuthenticated && (
            <p className="mt-2 rounded-lg border border-amber-500/20 bg-amber-500/5 px-3 py-2 text-sm text-amber-300">
              Đăng nhập để đặt vé và nhận ưu đãi giá theo loại khách hàng.
            </p>
          )}
        </div>
      </div>

      <section>
        <h2 className="mb-4 text-lg font-bold text-white">Chọn suất chiếu</h2>
        {showtimes.length === 0 && (
          <div className="rounded-xl border border-white/10 bg-white/5 p-8 text-center text-slate-400">
            Hiện chưa có suất chiếu nào cho phim này.
          </div>
        )}
        <div className="flex flex-col gap-5">
          {Object.entries(groupedByDate).map(([date, group]) => (
            <div key={date}>
              <p className="mb-2 text-sm font-semibold uppercase tracking-wide text-slate-400">{date}</p>
              <div className="flex flex-wrap gap-3">
                {group.map((st) => (
                  <button
                    key={st.showtimeId}
                    onClick={() => handleSelectShowtime(st.showtimeId)}
                    disabled={st.availableSeats === 0}
                    className="flex min-w-[140px] flex-col gap-1 rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-left transition-colors hover:border-amber-500/50 hover:bg-amber-500/5 disabled:cursor-not-allowed disabled:opacity-40"
                  >
                    <span className="text-base font-bold text-white">{formatTime(st.startTime)}</span>
                    <span className="text-xs text-slate-400">{st.room.roomName}</span>
                    <span className="text-xs font-medium text-amber-400">{formatCurrency(st.basePrice)}</span>
                    <span className="text-xs text-slate-500">
                      {st.availableSeats === 0 ? "Hết ghế" : `Còn ${st.availableSeats} ghế`}
                    </span>
                  </button>
                ))}
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
