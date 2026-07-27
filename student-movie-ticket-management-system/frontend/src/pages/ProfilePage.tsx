import { useAuth } from "../context/AuthContext";
import { customerTypeLabel } from "../utils/pricing";

export function ProfilePage() {
  const { customer } = useAuth();
  if (!customer) return null;

  const rows: { label: string; value: string }[] = [
    { label: "Mã khách hàng", value: customer.userId },
    { label: "Họ tên", value: customer.fullName },
    { label: "Số điện thoại", value: customer.phone },
    { label: "Email", value: customer.email },
    { label: "Loại khách hàng", value: customerTypeLabel(customer.customerType) },
  ];

  if (customer.customerType === "STUDENT") {
    rows.push(
      { label: "Mã sinh viên", value: customer.studentId ?? "—" },
      { label: "Trường", value: customer.schoolName ?? "—" }
    );
  }
  if (customer.customerType === "VIP") {
    rows.push(
      { label: "Thành viên từ", value: customer.memberSince ?? "—" },
      { label: "Điểm tích lũy", value: String(customer.pointBalance ?? 0) }
    );
  }

  return (
    <div className="mx-auto flex max-w-lg flex-col gap-6">
      <div className="text-center">
        <div className="mx-auto mb-3 flex h-16 w-16 items-center justify-center rounded-full bg-amber-500/15 text-2xl font-bold text-amber-400">
          {customer.fullName.trim().charAt(0).toUpperCase()}
        </div>
        <h1 className="text-2xl font-bold text-white">{customer.fullName}</h1>
        <p className="text-sm text-slate-400">{customerTypeLabel(customer.customerType)}</p>
      </div>

      <div className="divide-y divide-white/10 rounded-2xl border border-white/10 bg-white/5">
        {rows.map((row) => (
          <div key={row.label} className="flex items-center justify-between px-5 py-3.5 text-sm">
            <span className="text-slate-400">{row.label}</span>
            <span className="font-medium text-white">{row.value}</span>
          </div>
        ))}
      </div>
    </div>
  );
}
