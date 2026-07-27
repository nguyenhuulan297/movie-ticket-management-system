import type { PaymentStatus } from "../types";

const STYLES: Record<PaymentStatus, string> = {
  PAID: "bg-emerald-500/15 text-emerald-400 border-emerald-500/30",
  PENDING: "bg-amber-500/15 text-amber-400 border-amber-500/30",
  FAILED: "bg-red-500/15 text-red-400 border-red-500/30",
};

const LABELS: Record<PaymentStatus, string> = {
  PAID: "Đã thanh toán",
  PENDING: "Chưa thanh toán",
  FAILED: "Thất bại",
};

export function PaymentStatusBadge({ status }: { status: PaymentStatus }) {
  return (
    <span className={`inline-flex items-center gap-1.5 rounded-full border px-2.5 py-1 text-xs font-medium ${STYLES[status]}`}>
      <span className="h-1.5 w-1.5 rounded-full bg-current" />
      {LABELS[status]}
    </span>
  );
}
