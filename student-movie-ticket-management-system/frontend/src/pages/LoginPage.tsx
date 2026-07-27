import { useState, type FormEvent } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useToast } from "../context/ToastContext";
import { toApiError } from "../api/client";
import { InlineSpinner } from "../components/Spinner";

const DEMO_ACCOUNTS = [
  { label: "Khách thường", email: "an.nguyen@email.com" },
  { label: "Sinh viên", email: "cuong.le@student.edu.vn" },
  { label: "Thành viên VIP", email: "em.hoang@vip.com" },
];
const DEMO_PASSWORD = "Password123!";

export function LoginPage() {
  const { login } = useAuth();
  const { showToast } = useToast();
  const navigate = useNavigate();
  const location = useLocation();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const from = (location.state as { from?: { pathname: string } } | null)?.from?.pathname ?? "/";

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      const customer = await login(email.trim(), password);
      showToast(`Chào mừng trở lại, ${customer.fullName}!`, "success");
      navigate(from, { replace: true });
    } catch (err) {
      setError(toApiError(err).message);
    } finally {
      setSubmitting(false);
    }
  };

  const fillDemo = (demoEmail: string) => {
    setEmail(demoEmail);
    setPassword(DEMO_PASSWORD);
    setError(null);
  };

  return (
    <div className="mx-auto flex max-w-md flex-col gap-6">
      <div className="text-center">
        <h1 className="text-2xl font-bold text-white">Đăng nhập</h1>
        <p className="mt-1 text-sm text-slate-400">Đăng nhập để chọn ghế, đặt vé và thanh toán</p>
      </div>

      <form onSubmit={handleSubmit} className="flex flex-col gap-4 rounded-2xl border border-white/10 bg-white/5 p-6 shadow-xl">
        {error && (
          <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-3 py-2 text-sm text-red-300">
            {error}
          </div>
        )}
        <label className="flex flex-col gap-1.5 text-sm">
          <span className="font-medium text-slate-300">Email</span>
          <input
            type="email"
            required
            autoComplete="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="rounded-lg border border-white/10 bg-slate-900 px-3 py-2.5 text-white outline-none ring-amber-500/50 placeholder:text-slate-500 focus:ring-2"
            placeholder="ban@email.com"
          />
        </label>
        <label className="flex flex-col gap-1.5 text-sm">
          <span className="font-medium text-slate-300">Mật khẩu</span>
          <input
            type="password"
            required
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="rounded-lg border border-white/10 bg-slate-900 px-3 py-2.5 text-white outline-none ring-amber-500/50 placeholder:text-slate-500 focus:ring-2"
            placeholder="••••••••"
          />
        </label>

        <button
          type="submit"
          disabled={submitting}
          className="mt-2 flex items-center justify-center gap-2 rounded-lg bg-amber-500 px-4 py-2.5 font-semibold text-slate-950 transition-colors hover:bg-amber-400 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {submitting && <InlineSpinner />}
          Đăng nhập
        </button>

        <p className="text-center text-sm text-slate-400">
          Chưa có tài khoản?{" "}
          <Link to="/register" className="font-medium text-amber-400 hover:underline">
            Đăng ký ngay
          </Link>
        </p>
      </form>

      <div className="rounded-2xl border border-white/10 bg-white/[0.03] p-4 text-xs text-slate-400">
        <p className="mb-2 font-medium text-slate-300">Tài khoản demo (mật khẩu: {DEMO_PASSWORD})</p>
        <div className="flex flex-col gap-1.5">
          {DEMO_ACCOUNTS.map((acc) => (
            <button
              key={acc.email}
              onClick={() => fillDemo(acc.email)}
              className="flex items-center justify-between rounded-lg px-2 py-1.5 text-left hover:bg-white/5"
            >
              <span>{acc.label}</span>
              <span className="text-slate-500">{acc.email}</span>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
