import { useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { fetchShowingMovies, searchMovies } from "../api/catalog";
import { toApiError } from "../api/client";
import { Spinner } from "../components/Spinner";
import { MoviePoster } from "../components/MoviePoster";
import { formatDuration } from "../utils/datetime";
import type { Movie } from "../types";

export function MoviesPage() {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [query, setQuery] = useState("");
  const debounceRef = useRef<number | null>(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    fetchShowingMovies()
      .then(setMovies)
      .catch((err) => setError(toApiError(err).message))
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    if (debounceRef.current) window.clearTimeout(debounceRef.current);
    debounceRef.current = window.setTimeout(() => {
      setLoading(true);
      setError(null);
      const request = query.trim() ? searchMovies(query.trim()) : fetchShowingMovies();
      request
        .then((result) => setMovies(query.trim() ? result.filter((m) => m.status === "SHOWING") : result))
        .catch((err) => setError(toApiError(err).message))
        .finally(() => setLoading(false));
    }, 300);
    return () => {
      if (debounceRef.current) window.clearTimeout(debounceRef.current);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [query]);

  return (
    <div className="flex flex-col gap-8">
      <section className="overflow-hidden rounded-3xl border border-white/10 bg-linear-to-br from-amber-500/10 via-slate-900 to-slate-950 px-6 py-10 text-center sm:py-14">
        <h1 className="text-3xl font-black tracking-tight text-white sm:text-4xl">
          Đặt vé xem phim <span className="text-amber-400">nhanh chóng</span>
        </h1>
        <p className="mx-auto mt-3 max-w-xl text-sm text-slate-400 sm:text-base">
          Chọn phim, chọn suất chiếu, chọn ghế yêu thích và thanh toán chỉ trong vài bước.
        </p>
        <div className="mx-auto mt-6 max-w-md">
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Tìm phim theo tên..."
            className="w-full rounded-xl border border-white/10 bg-slate-900/80 px-4 py-3 text-white placeholder:text-slate-500 outline-none ring-amber-500/50 focus:ring-2"
          />
        </div>
      </section>

      <section>
        <h2 className="mb-4 text-lg font-bold text-white">
          {query.trim() ? `Kết quả cho "${query.trim()}"` : "Phim đang chiếu"}
        </h2>

        {loading && <Spinner label="Đang tải danh sách phim..." />}

        {!loading && error && (
          <div className="rounded-xl border border-red-500/30 bg-red-500/10 p-6 text-center text-red-300">
            {error}
          </div>
        )}

        {!loading && !error && movies.length === 0 && (
          <div className="rounded-xl border border-white/10 bg-white/5 p-10 text-center text-slate-400">
            Không tìm thấy phim nào phù hợp.
          </div>
        )}

        {!loading && !error && movies.length > 0 && (
          <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 lg:grid-cols-4">
            {movies.map((movie) => (
              <Link
                key={movie.movieId}
                to={`/movies/${movie.movieId}`}
                className="group flex flex-col overflow-hidden rounded-2xl border border-white/10 bg-white/5 transition-all hover:-translate-y-1 hover:border-amber-500/40 hover:shadow-lg hover:shadow-amber-500/5"
              >
                <MoviePoster title={movie.title} className="aspect-[2/3] w-full" />
                <div className="flex flex-1 flex-col gap-1 p-3">
                  <h3 className="line-clamp-2 text-sm font-semibold text-white group-hover:text-amber-400">
                    {movie.title}
                  </h3>
                  <p className="text-xs text-slate-400">{movie.genre}</p>
                  <div className="mt-auto flex items-center justify-between pt-2 text-xs text-slate-500">
                    <span>{formatDuration(movie.duration)}</span>
                    <span className="rounded-full border border-white/10 px-2 py-0.5">{movie.ageLimit === 0 ? "P" : `${movie.ageLimit}+`}</span>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
