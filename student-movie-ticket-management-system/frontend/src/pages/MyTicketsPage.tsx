import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { fetchTicketsByCustomer } from "../api/bookings";
import { toApiError } from "../api/client";
import { useAuth } from "../context/AuthContext";
import { Spinner } from "../components/Spinner";
import { PaymentStatusBadge } from "../components/StatusBadge";
import { formatDateTime } from "../utils/datetime";
import { formatCurrency, seatTypeLabel } from "../utils/pricing";
import type { Ticket } from "../types";

export function MyTicketsPage() {
  const { customer } = useAuth();
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!customer) return;
    setLoading(true);
    setError(null);
    fetchTicketsByCustomer(customer.userId)
      .then((result) => setTickets(result.sort((a, b) => b.bookedAt.localeCompare(a.bookedAt))))
      .catch((err) => setError(toApiError(err).message))
      .finally(() => setLoading(false));
  }, [customer]);

  if (loading) return <Spinner label="Đang tải vé của bạn..." />;

  return (
    <div className="flex flex-col gap-6">
      <div>
        <h1 className="text-2xl font-bold text-white">Vé của tôi</h1>
        <p className="mt-1 text-sm text-slate-400">Lịch sử đặt vé và trạng thái thanh toán</p>
      </div>

      {error && (
        <div className="rounded-xl border border-red-500/30 bg-red-500/10 p-6 text-center text-red-300">{error}</div>
      )}

      {!error && tickets.length === 0 && (
        <div className="rounded-xl border border-white/10 bg-white/5 p-10 text-center text-slate-400">
          <p>Bạn chưa đặt vé nào.</p>
          <Link to="/" className="mt-3 inline-block font-medium text-amber-400 hover:underline">
            Khám phá phim đang chiếu →
          </Link>
        </div>
      )}

      <div className="flex flex-col gap-3">
        {tickets.map((ticket) => (
          <div
            key={ticket.ticketId}
            className="flex flex-col gap-3 rounded-2xl border border-white/10 bg-white/5 p-4 sm:flex-row sm:items-center sm:justify-between"
          >
            <div>
              <div className="flex items-center gap-2">
                <p className="font-semibold text-white">Ghế {ticket.seat.seatNumber}</p>
                <span className="text-xs text-slate-500">({seatTypeLabel(ticket.seat.seatType)})</span>
              </div>
              <p className="text-sm text-slate-400">{ticket.showtime.room.roomName}</p>
              <p className="text-xs text-slate-500">Đặt lúc {formatDateTime(ticket.bookedAt)}</p>
              <p className="mt-1 font-mono text-xs text-slate-600">{ticket.ticketId}</p>
            </div>

            <div className="flex items-center justify-between gap-4 sm:flex-col sm:items-end sm:justify-center">
              <span className="font-bold text-amber-400">{formatCurrency(ticket.finalPrice)}</span>
              <PaymentStatusBadge status={ticket.paymentStatus} />
              {ticket.paymentStatus !== "PAID" && (
                <Link
                  to={`/payment/${ticket.ticketId}`}
                  className="rounded-lg bg-amber-500 px-3 py-1.5 text-xs font-semibold text-slate-950 hover:bg-amber-400"
                >
                  Thanh toán ngay
                </Link>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
