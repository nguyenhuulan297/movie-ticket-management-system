import { Link, useLocation, useNavigate } from "react-router-dom";
import { formatDate, formatTime } from "../utils/datetime";
import { formatCurrency } from "../utils/pricing";
import type { Booking } from "../types";

export function BookingSuccessPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const booking = (location.state as { booking?: Booking } | null)?.booking;

  if (!booking) {
    return (
      <div className="mx-auto max-w-lg rounded-2xl border border-white/10 bg-white/5 p-8 text-center">
        <p className="text-slate-300">Không tìm thấy thông tin đặt vé (có thể trang đã được tải lại).</p>
        <Link to="/my-tickets" className="mt-4 inline-block font-medium text-amber-400 hover:underline">
          Xem vé của tôi →
        </Link>
      </div>
    );
  }

  return (
    <div className="mx-auto flex max-w-xl flex-col gap-6">
      <div className="text-center">
        <div className="mx-auto mb-3 flex h-14 w-14 items-center justify-center rounded-full bg-emerald-500/15 text-3xl">
          🎉
        </div>
        <h1 className="text-2xl font-bold text-white">Đặt vé thành công!</h1>
        <p className="mt-1 text-sm text-slate-400">
          Mã đặt vé <span className="font-mono text-slate-300">{booking.bookingId}</span> · Vui lòng thanh toán để hoàn tất
        </p>
      </div>

      <div className="rounded-2xl border border-white/10 bg-white/5 p-5">
        <div className="mb-4 border-b border-white/10 pb-3">
          <p className="text-sm text-slate-400">
            {formatDate(booking.showtime.startTime)} · {formatTime(booking.showtime.startTime)} · {booking.showtime.room.roomName}
          </p>
        </div>

        <ul className="flex flex-col gap-3">
          {booking.tickets.map((ticket) => (
            <li
              key={ticket.ticketId}
              className="flex items-center justify-between gap-3 rounded-xl border border-white/10 bg-slate-900/50 px-4 py-3"
            >
              <div>
                <p className="font-medium text-white">Ghế {ticket.seat.seatNumber}</p>
                <p className="text-xs font-mono text-slate-500">{ticket.ticketId}</p>
              </div>
              <div className="flex items-center gap-3">
                <span className="text-sm font-semibold text-amber-400">{formatCurrency(ticket.finalPrice)}</span>
                <button
                  onClick={() => navigate(`/payment/${ticket.ticketId}`)}
                  className="rounded-lg bg-amber-500 px-3 py-1.5 text-xs font-semibold text-slate-950 hover:bg-amber-400"
                >
                  Thanh toán
                </button>
              </div>
            </li>
          ))}
        </ul>

        <div className="mt-4 flex items-center justify-between border-t border-white/10 pt-4 font-bold text-white">
          <span>Tổng cộng</span>
          <span className="text-amber-400">{formatCurrency(booking.totalPrice)}</span>
        </div>
      </div>

      <Link to="/my-tickets" className="text-center text-sm font-medium text-slate-400 hover:text-white">
        Xem tất cả vé của tôi →
      </Link>
    </div>
  );
}
