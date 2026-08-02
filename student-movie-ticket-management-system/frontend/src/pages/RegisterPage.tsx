import { useState, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useToast } from "../context/ToastContext";
import { toApiError } from "../api/client";
import { InlineSpinner } from "../components/Spinner";
import type { CustomerType } from "../types";

const TYPE_OPTIONS: { value: CustomerType; label: string; hint: string }[] = [
  { value: "NORMAL", label: "Khách thường", hint: "Không giảm giá vé" },
  { value: "STUDENT", label: "Sinh viên", hint: "Giảm 20% giá vé" },
];

export function RegisterPage() {
  const { register } = useAuth();
  const { showToast } = useToast();
  const navigate = useNavigate();

  const [fullName, setFullName] = useState("");
  const [phone, setPhone] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [customerType, setCustomerType] = useState<CustomerType>("NORMAL");
  const [studentId, setStudentId] = useState("");
  const [schoolName, setSchoolName] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const validate = (): string | null => {
    if (!fullName.trim()) return "Vui lòng nhập họ tên";
    if (!/^\d{10}$/.test(phone)) return "Số điện thoại phải gồm đúng 10 chữ số";
    if (!email.includes("@")) return "Email không hợp lệ";
    if (password.length < 6) return "Mật khẩu phải có ít nhất 6 ký tự";
    if (password !== confirmPassword) return "Mật khẩu nhập lại không khớp";
    if (customerType === "STUDENT" && !studentId.trim()) return "Vui lòng nhập mã sinh viên";
    if (customerType === "STUDENT" && !schoolName.trim()) return "Vui lòng nhập tên trường";
    return null;
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    const validationError = validate();
    if (validationError) {
      setError(validationError);
      return;
    }
    setError(null);
    setSubmitting(true);
    try {
      const customer = await register({
        fullName: fullName.trim(),
        phone,
        email: email.trim(),
        password,
        customerType,
        studentId: customerType === "STUDENT" ? studentId.trim() : undefined,
        schoolName: customerType === "STUDENT" ? schoolName.trim() : undefined,
        memberSince: customerType === "VIP" ? new Date().toISOString().slice(0, 10) : undefined,
        pointBalance: customerType === "VIP" ? 0 : undefined,
      });
      showToast(`Tạo tài khoản thành công! Chào mừng ${customer.fullName} 🎉`, "success");
      navigate("/", { replace: true });
    } catch (err) {
      setError(toApiError(err).message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="mx-auto flex max-w-lg flex-col gap-6">
      <div className="text-center">
        <h1 className="text-2xl font-bold text-white">Tạo tài khoản</h1>
        <p className="mt-1 text-sm text-slate-400">Đăng ký để nhận ưu đãi giá vé theo loại khách hàng</p>
      </div>

      <form onSubmit={handleSubmit} className="flex flex-col gap-4 rounded-2xl border border-white/10 bg-white/5 p-6 shadow-xl">
        {error && (
          <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-3 py-2 text-sm text-red-300">
            {error}
          </div>
        )}

        <div className="grid gap-4 sm:grid-cols-2">
          <label className="flex flex-col gap-1.5 text-sm">
            <span className="font-medium text-slate-300">Họ tên</span>
            <input
              required
              value={fullName}
              onChange={(e) => setFullName(e.target.value)}
              className="rounded-lg border border-white/10 bg-slate-900 px-3 py-2.5 text-white outline-none ring-amber-500/50 focus:ring-2"
              placeholder="Nguyễn Văn A"
            />
          </label>
          <label className="flex flex-col gap-1.5 text-sm">
            <span className="font-medium text-slate-300">Số điện thoại</span>
            <input
              required
              value={phone}
              onChange={(e) => setPhone(e.target.value.replace(/[^\d]/g, ""))}
              maxLength={10}
              className="rounded-lg border border-white/10 bg-slate-900 px-3 py-2.5 text-white outline-none ring-amber-500/50 focus:ring-2"
              placeholder="09xxxxxxxx"
            />
          </label>
        </div>

        <label className="flex flex-col gap-1.5 text-sm">
          <span className="font-medium text-slate-300">Email</span>
          <input
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="rounded-lg border border-white/10 bg-slate-900 px-3 py-2.5 text-white outline-none ring-amber-500/50 focus:ring-2"
            placeholder="ban@email.com"
          />
        </label>

        <div className="grid gap-4 sm:grid-cols-2">
          <label className="flex flex-col gap-1.5 text-sm">
            <span className="font-medium text-slate-300">Mật khẩu</span>
            <input
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="rounded-lg border border-white/10 bg-slate-900 px-3 py-2.5 text-white outline-none ring-amber-500/50 focus:ring-2"
              placeholder="Tối thiểu 6 ký tự"
            />
          </label>
          <label className="flex flex-col gap-1.5 text-sm">
            <span className="font-medium text-slate-300">Nhập lại mật khẩu</span>
            <input
              type="password"
              required
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="rounded-lg border border-white/10 bg-slate-900 px-3 py-2.5 text-white outline-none ring-amber-500/50 focus:ring-2"
              placeholder="••••••••"
            />
          </label>
        </div>

        <div>
          <span className="mb-2 block text-sm font-medium text-slate-300">Loại khách hàng</span>
          <div className="grid grid-cols-2 gap-2">
            {TYPE_OPTIONS.map((opt) => (
              <button
                type="button"
                key={opt.value}
                onClick={() => setCustomerType(opt.value)}
                className={`flex flex-col items-start gap-0.5 rounded-lg border px-3 py-2.5 text-left text-xs transition-colors ${
                  customerType === opt.value
                    ? "border-amber-500/60 bg-amber-500/10 text-amber-300"
                    : "border-white/10 bg-slate-900 text-slate-400 hover:border-white/20"
                }`}
              >
                <span className="font-semibold">{opt.label}</span>
                <span className="opacity-80">{opt.hint}</span>
              </button>
            ))}
          </div>
        </div>

        {customerType === "STUDENT" && (
          <div className="grid gap-4 sm:grid-cols-2">
            <label className="flex flex-col gap-1.5 text-sm">
              <span className="font-medium text-slate-300">Mã sinh viên</span>
              <input
                required
                value={studentId}
                onChange={(e) => setStudentId(e.target.value)}
                className="rounded-lg border border-white/10 bg-slate-900 px-3 py-2.5 text-white outline-none ring-amber-500/50 focus:ring-2"
                placeholder="SV123456"
              />
            </label>
            <label className="flex flex-col gap-1.5 text-sm">
              <span className="font-medium text-slate-300">Tên trường</span>
              <input
                required
                value={schoolName}
                onChange={(e) => setSchoolName(e.target.value)}
                className="rounded-lg border border-white/10 bg-slate-900 px-3 py-2.5 text-white outline-none ring-amber-500/50 focus:ring-2"
                placeholder="Đại học Bách Khoa"
              />
            </label>
          </div>
        )}

        <button
          type="submit"
          disabled={submitting}
          className="mt-2 flex items-center justify-center gap-2 rounded-lg bg-amber-500 px-4 py-2.5 font-semibold text-slate-950 transition-colors hover:bg-amber-400 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {submitting && <InlineSpinner />}
          Tạo tài khoản
        </button>

        <p className="text-center text-sm text-slate-400">
          Đã có tài khoản?{" "}
          <Link to="/login" className="font-medium text-amber-400 hover:underline">
            Đăng nhập
          </Link>
        </p>
      </form>
    </div>
  );
}
