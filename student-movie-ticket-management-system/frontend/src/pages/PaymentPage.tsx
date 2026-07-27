import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { fetchTicketById, payForTicket } from "../api/bookings";
import { toApiError } from "../api/client";
import { useToast } from "../context/ToastContext";
import { Spinner, InlineSpinner } from "../components/Spinner";
import { PaymentStatusBadge } from "../components/StatusBadge";
import { formatDate, formatTime } from "../utils/datetime";
import { formatCurrency, seatTypeLabel } from "../utils/pricing";
import type { Payment, PaymentMethodCode, Ticket } from "../types";

const METHODS: { value: PaymentMethodCode; label: string; icon: string; hint: string }[] = [
  { value: "CASH", label: "Tiền mặt", icon: "💵", hint: "Thanh toán tại quầy, luôn thành công" },
  { value: "BANK_TRANSFER", label: "Chuyển khoản", icon: "🏦", hint: "Qua ngân hàng, tỉ lệ thành công 80%" },
  { value: "E_WALLET", label: "Ví điện tử", icon: "📱", hint: "MoMo/ZaloPay..., tỉ lệ thành công 80%" },
];

export function PaymentPage() {
  const { ticketId } = useParams<{ ticketId: string }>();
  const { showToast } = useToast();

  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [method, setMethod] = useState<PaymentMethodCode>("CASH");
  const [paying, setPaying] = useState(false);
  const [lastResult, setLastResult] = useState<Payment | null>(null);

  const loadTicket = () => {
    if (!ticketId) return;
    setLoading(true);
    setError(null);
    fetchTicketById(ticketId)
      .then(setTicket)
      .catch((err) => setError(toApiError(err).message))
      .finally(() => setLoading(false));
  };

  useEffect(loadTicket, [ticketId]);

  const handlePay = async () => {
    if (!ticketId) return;
    setPaying(true);
    setLastResult(null);
    try {
      const result = await payForTicket(ticketId, method);
      setLastResult(result);
      if (result.success) {
        showToast("Thanh toán thành công!", "success");
        loadTicket();
      } else {
        showToast(result.failureReason ?? "Giao dịch thất bại, vui lòng thử lại", "error");
      }
    } catch (err) {
      showToast(toApiError(err).message, "error");
    } finally {
      setPaying(false);
    }
  };

  if (loading) return <Spinner label="Đang tải thông tin vé..." />;
  if (error || !ticket) {
    return (
      <div className="rounded-xl border border-red-500/30 bg-red-500/10 p-6 text-center text-red-300">
        {error ?? "Không tìm thấy vé"}
      </div>
    );
  }

  const isPaid = ticket.paymentStatus === "PAID";

  return (
    <div className="mx-auto flex max-w-lg flex-col gap-6">
      <div className="text-center">
        <h1 className="text-2xl font-bold text-white">Thanh toán vé</h1>
        <p className="mt-1 text-sm text-slate-400">Mã vé {ticket.ticketId}</p>
      </div>

      <div className="rounded-2xl border border-white/10 bg-white/5 p-5">
        <div className="flex items-start justify-between">
          <div>
            <p className="font-semibold text-white">Ghế {ticket.seat.seatNumber}</p>
            <p className="text-xs text-slate-400">{seatTypeLabel(ticket.seat.seatType)}</p>
            <p className="mt-2 text-sm text-slate-400">
              {formatDate(ticket.showtime.startTime)} · {formatTime(ticket.showtime.startTime)}
            </p>
            <p className="text-sm text-slate-400">{ticket.showtime.room.roomName}</p>
          </div>
          <PaymentStatusBadge status={ticket.paymentStatus} />
        </div>
        <div className="mt-4 flex items-center justify-between border-t border-white/10 pt-4 font-bold text-white">
          <span>Số tiền</span>
          <span className="text-amber-400">{formatCurrency(ticket.finalPrice)}</span>
        </div>
      </div>

      {isPaid ? (
        <div className="rounded-2xl border border-emerald-500/30 bg-emerald-500/10 p-5 text-center text-emerald-300">
          <p className="font-semibold">Vé này đã được thanh toán.</p>
          <Link to="/my-tickets" className="mt-2 inline-block text-sm font-medium underline">
            Xem vé của tôi
          </Link>
        </div>
      ) : (
        <div className="rounded-2xl border border-white/10 bg-white/5 p-5">
          <h2 className="mb-3 text-sm font-bold uppercase tracking-wide text-slate-300">Chọn phương thức</h2>
          <div className="flex flex-col gap-2">
            {METHODS.map((m) => (
              <button
                key={m.value}
                onClick={() => setMethod(m.value)}
                className={`flex items-center gap-3 rounded-xl border px-4 py-3 text-left transition-colors ${
                  method === m.value
                    ? "border-amber-500/60 bg-amber-500/10"
                    : "border-white/10 bg-slate-900/50 hover:border-white/20"
                }`}
              >
                <span className="text-xl">{m.icon}</span>
                <span className="flex-1">
                  <span className="block text-sm font-medium text-white">{m.label}</span>
                  <span className="block text-xs text-slate-400">{m.hint}</span>
                </span>
                <span
                  className={`h-4 w-4 shrink-0 rounded-full border-2 ${
                    method === m.value ? "border-amber-400 bg-amber-400" : "border-slate-600"
                  }`}
                />
              </button>
            ))}
          </div>

          {lastResult && !lastResult.success && (
            <div className="mt-4 rounded-lg border border-red-500/30 bg-red-500/10 px-3 py-2 text-sm text-red-300">
              Giao dịch thất bại: {lastResult.failureReason}. Vui lòng thử lại hoặc chọn phương thức khác.
            </div>
          )}

          <button
            onClick={handlePay}
            disabled={paying}
            className="mt-5 flex w-full items-center justify-center gap-2 rounded-lg bg-amber-500 px-4 py-3 font-semibold text-slate-950 transition-colors hover:bg-amber-400 disabled:cursor-not-allowed disabled:opacity-60"
          >
            {paying && <InlineSpinner />}
            Thanh toán {formatCurrency(ticket.finalPrice)}
          </button>
        </div>
      )}
    </div>
  );
}
