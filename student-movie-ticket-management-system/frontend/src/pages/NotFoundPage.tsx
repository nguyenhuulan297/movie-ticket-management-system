import { Link } from "react-router-dom";

export function NotFoundPage() {
  return (
    <div className="flex flex-col items-center justify-center gap-3 py-20 text-center">
      <p className="text-6xl">🎬</p>
      <h1 className="text-2xl font-bold text-white">404 — Không tìm thấy trang</h1>
      <p className="text-slate-400">Trang bạn tìm không tồn tại hoặc đã bị di chuyển.</p>
      <Link to="/" className="mt-2 rounded-lg bg-amber-500 px-4 py-2 font-semibold text-slate-950 hover:bg-amber-400">
        Về trang chủ
      </Link>
    </div>
  );
}
