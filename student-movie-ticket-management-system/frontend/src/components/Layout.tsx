import type { ReactNode } from "react";
import { Navbar } from "./Navbar";

export function Layout({ children }: { children: ReactNode }) {
  return (
    <div className="flex min-h-screen flex-col">
      <Navbar />
      <main className="mx-auto w-full max-w-6xl flex-1 px-4 py-8 sm:px-6">{children}</main>
      <footer className="border-t border-white/10 py-6 text-center text-xs text-slate-500">
        CineTicket — Đồ án OOP Nhóm 06 · Dữ liệu mô phỏng, chỉ phục vụ mục đích học tập
      </footer>
    </div>
  );
}
