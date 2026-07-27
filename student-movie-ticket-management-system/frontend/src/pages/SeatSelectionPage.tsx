import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { fetchMovieById, fetchSeatsByShowtime, fetchShowtimeById } from "../api/catalog";
import { createBooking } from "../api/bookings";
import { toApiError } from "../api/client";
import { useAuth } from "../context/AuthContext";
import { useToast } from "../context/ToastContext";
import { Spinner, InlineSpinner } from "../components/Spinner";
import { formatDate, formatTime } from "../utils/datetime";
import { customerTypeLabel, estimateTicketPrice, formatCurrency, seatTypeLabel } from "../utils/pricing";
import type { Movie, Seat, SeatType, Showtime } from "../types";

const SEAT_TYPE_DOT: Record<SeatType, string> = {
  NORMAL: "bg-slate-500",
  DOUBLE: "bg-sky-500",
  VIP: "bg-amber-500",
};

function parseSeat(seatNumber: string): { row: string; col: number } {
  const match = seatNumber.match(/^([A-Za-z]+)(\d+)$/);
  if (!match) return { row: seatNumber, col: 0 };
  return { row: match[1], col: Number(match[2]) };
}

export function SeatSelectionPage() {
  const { showtimeId } = useParams<{ showtimeId: string }>();
  const navigate = useNavigate();
  const { customer } = useAuth();
  const { showToast } = useToast();

  const [movie, setMovie] = useState<Movie | null>(null);
  const [showtime, setShowtime] = useState<Showtime | null>(null);
  const [seats, setSeats] = useState<Seat[]>([]);
  const [selectedSeatIds, setSelectedSeatIds] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [booking, setBooking] = useState(false);

  useEffect(() => {
    if (!showtimeId) return;
    setLoading(true);
    setError(null);
    fetchShowtimeById(showtimeId)
      .then(async (st) => {
        const [movieResult, seatList] = await Promise.all([
          fetchMovieById(st.movieId),
          fetchSeatsByShowtime(showtimeId),
        ]);
        setShowtime(st);
        setMovie(movieResult);
        setSeats(seatList);
      })
      .catch((err) => setError(toApiError(err).message))
      .finally(() => setLoading(false));
  }, [showtimeId]);

  const rows = useMemo(() => {
    const grouped = new Map<string, (Seat & { col: number })[]>();
    for (const seat of seats) {
      const { row, col } = parseSeat(seat.seatNumber);
      const list = grouped.get(row) ?? [];
      list.push({ ...seat, col });
      grouped.set(row, list);
    }
    return Array.from(grouped.entries())
      .sort(([a], [b]) => a.localeCompare(b))
      .map(([row, items]) => [row, items.sort((a, b) => a.col - b.col)] as const);
  }, [seats]);

  const selectedSeats = seats.filter((s) => selectedSeatIds.includes(s.seatId));

  const total = useMemo(() => {
    if (!showtime || !customer) return 0;
    return selectedSeats.reduce(
      (sum, seat) => sum + estimateTicketPrice(showtime.basePrice, seat.seatType, customer.customerType),
      0
    );
  }, [selectedSeats, showtime, customer]);

  const toggleSeat = (seat: Seat) => {
    if (seat.status !== "AVAILABLE") return;
    setSelectedSeatIds((prev) =>
      prev.includes(seat.seatId) ? prev.filter((id) => id !== seat.seatId) : [...prev, seat.seatId]
    );
  };

  const handleBook = async () => {
    if (!customer || !showtimeId || selectedSeatIds.length === 0) return;
    setBooking(true);
    try {
      const result = await createBooking(customer.userId, showtimeId, selectedSeatIds);
      showToast(`Đặt ${result.tickets.length} vé thành công! Vui lòng thanh toán.`, "success");
      navigate("/booking-success", { state: { booking: result } });
    } catch (err) {
      showToast(toApiError(err).message, "error");
    } finally {
      setBooking(false);
    }
  };

  if (loading) return <Spinner label="Đang tải sơ đồ ghế..." />;
  if (error || !showtime || !movie) {
    return (
      <div className="rounded-xl border border-red-500/30 bg-red-500/10 p-6 text-center text-red-300">
        {error ?? "Không tìm thấy suất chiếu"}
      </div>
    );
  }

  return (
    <div className="grid gap-8 lg:grid-cols-[1fr_320px]">
      <div className="flex flex-col gap-6">
        <div>
          <h1 className="text-xl font-bold text-white">{movie.title}</h1>
          <p className="text-sm text-slate-400">
            {formatDate(showtime.startTime)} · {formatTime(showtime.startTime)} · {showtime.room.roomName}
          </p>
        </div>

        <div className="rounded-2xl border border-white/10 bg-white/5 p-6">
          <div className="mb-6 flex flex-col items-center gap-2">
            <div className="h-2 w-3/4 rounded-full bg-linear-to-r from-transparent via-slate-500 to-transparent" />
            <p className="text-xs uppercase tracking-widest text-slate-500">Màn hình</p>
          </div>

          <div className="flex flex-col items-center gap-2 overflow-x-auto pb-2">
            {rows.map(([row, rowSeats]) => (
              <div key={row} className="flex items-center gap-2">
                <span className="w-4 text-xs font-semibold text-slate-500">{row}</span>
                <div className="flex gap-2">
                  {rowSeats.map((seat) => {
                    const isSelected = selectedSeatIds.includes(seat.seatId);
                    const isBooked = seat.status === "BOOKED";
                    return (
                      <button
                        key={seat.seatId}
                        onClick={() => toggleSeat(seat)}
                        disabled={isBooked}
                        title={`${seat.seatNumber} · ${seatTypeLabel(seat.seatType)}`}
                        className={`flex h-9 w-9 items-center justify-center rounded-lg text-[11px] font-semibold transition-all ${
                          isBooked
                            ? "cursor-not-allowed bg-slate-800 text-slate-600"
                            : isSelected
                              ? "scale-105 bg-amber-500 text-slate-950 shadow-lg shadow-amber-500/30"
                              : seat.seatType === "VIP"
                                ? "bg-amber-500/15 text-amber-400 hover:bg-amber-500/25"
                                : seat.seatType === "DOUBLE"
                                  ? "bg-sky-500/15 text-sky-400 hover:bg-sky-500/25"
                                  : "bg-white/10 text-slate-300 hover:bg-white/20"
                        }`}
                      >
                        {seat.seatNumber}
                      </button>
                    );
                  })}
                </div>
              </div>
            ))}
          </div>

          <div className="mt-6 flex flex-wrap justify-center gap-4 text-xs text-slate-400">
            <span className="flex items-center gap-1.5">
              <span className={`h-2.5 w-2.5 rounded-full ${SEAT_TYPE_DOT.NORMAL}`} /> Ghế thường
            </span>
            <span className="flex items-center gap-1.5">
              <span className={`h-2.5 w-2.5 rounded-full ${SEAT_TYPE_DOT.DOUBLE}`} /> Ghế đôi
            </span>
            <span className="flex items-center gap-1.5">
              <span className={`h-2.5 w-2.5 rounded-full ${SEAT_TYPE_DOT.VIP}`} /> Ghế VIP
            </span>
            <span className="flex items-center gap-1.5">
              <span className="h-2.5 w-2.5 rounded-full bg-amber-500" /> Đang chọn
            </span>
            <span className="flex items-center gap-1.5">
              <span className="h-2.5 w-2.5 rounded-full bg-slate-800" /> Đã đặt
            </span>
          </div>
        </div>
      </div>

      <aside className="h-fit rounded-2xl border border-white/10 bg-white/5 p-5">
        <h2 className="mb-4 text-sm font-bold uppercase tracking-wide text-slate-300">Tóm tắt đặt vé</h2>

        {customer && (
          <p className="mb-3 text-xs text-slate-400">
            Hạng khách hàng: <span className="text-amber-400">{customerTypeLabel(customer.customerType)}</span>
          </p>
        )}

        {selectedSeats.length === 0 ? (
          <p className="text-sm text-slate-500">Chưa chọn ghế nào</p>
        ) : (
          <ul className="flex flex-col gap-2 text-sm">
            {selectedSeats.map((seat) => (
              <li key={seat.seatId} className="flex items-center justify-between text-slate-300">
                <span>
                  {seat.seatNumber} <span className="text-slate-500">({seatTypeLabel(seat.seatType)})</span>
                </span>
                <span>
                  {customer && formatCurrency(estimateTicketPrice(showtime.basePrice, seat.seatType, customer.customerType))}
                </span>
              </li>
            ))}
          </ul>
        )}

        <div className="mt-4 flex items-center justify-between border-t border-white/10 pt-4 text-base font-bold text-white">
          <span>Tổng cộng</span>
          <span className="text-amber-400">{formatCurrency(total)}</span>
        </div>

        <button
          onClick={handleBook}
          disabled={selectedSeatIds.length === 0 || booking}
          className="mt-5 flex w-full items-center justify-center gap-2 rounded-lg bg-amber-500 px-4 py-3 font-semibold text-slate-950 transition-colors hover:bg-amber-400 disabled:cursor-not-allowed disabled:opacity-40"
        >
          {booking && <InlineSpinner />}
          Đặt {selectedSeatIds.length > 0 ? `${selectedSeatIds.length} vé` : "vé"}
        </button>
      </aside>
    </div>
  );
}
