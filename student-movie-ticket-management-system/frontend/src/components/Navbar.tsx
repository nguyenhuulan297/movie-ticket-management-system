import { useState } from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const navLinkClass = ({ isActive }: { isActive: boolean }) =>
  `rounded-lg px-3 py-2 text-sm font-medium transition-colors ${
    isActive ? "bg-amber-500/15 text-amber-400" : "text-slate-300 hover:bg-white/5 hover:text-white"
  }`;

export function Navbar() {
  const { customer, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    setMenuOpen(false);
    navigate("/");
  };

  return (
    <header className="sticky top-0 z-40 border-b border-white/10 bg-slate-950/80 backdrop-blur-md">
      <div className="mx-auto flex h-16 max-w-6xl items-center justify-between px-4 sm:px-6">
        <Link to="/" className="flex items-center gap-2 text-lg font-bold tracking-tight text-white">
          <span className="text-2xl">🎬</span>
          <span>
            Cine<span className="text-amber-400">Ticket</span>
          </span>
        </Link>

        <nav className="hidden items-center gap-1 md:flex">
          <NavLink to="/" end className={navLinkClass}>
            Phim đang chiếu
          </NavLink>
          {isAuthenticated && (
            <NavLink to="/my-tickets" className={navLinkClass}>
              Vé của tôi
            </NavLink>
          )}
        </nav>

        <div className="hidden items-center gap-3 md:flex">
          {isAuthenticated ? (
            <>
              <Link
                to="/profile"
                className="flex items-center gap-2 rounded-lg px-3 py-2 text-sm text-slate-300 hover:bg-white/5 hover:text-white"
              >
                <span className="flex h-7 w-7 items-center justify-center rounded-full bg-amber-500/20 text-xs font-semibold text-amber-400">
                  {customer?.fullName?.trim().charAt(0).toUpperCase() ?? "?"}
                </span>
                {customer?.fullName}
              </Link>
              <button
                onClick={handleLogout}
                className="rounded-lg border border-white/10 px-3 py-2 text-sm font-medium text-slate-300 transition-colors hover:border-red-500/40 hover:text-red-400"
              >
                Đăng xuất
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="rounded-lg px-3 py-2 text-sm font-medium text-slate-300 hover:text-white">
                Đăng nhập
              </Link>
              <Link
                to="/register"
                className="rounded-lg bg-amber-500 px-4 py-2 text-sm font-semibold text-slate-950 transition-colors hover:bg-amber-400"
              >
                Đăng ký
              </Link>
            </>
          )}
        </div>

        <button
          className="rounded-lg p-2 text-slate-300 hover:bg-white/5 md:hidden"
          onClick={() => setMenuOpen((v) => !v)}
          aria-label="Mở menu"
        >
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            {menuOpen ? <path d="M6 6l12 12M18 6L6 18" /> : <path d="M4 7h16M4 12h16M4 17h16" />}
          </svg>
        </button>
      </div>

      {menuOpen && (
        <div className="border-t border-white/10 bg-slate-950 px-4 py-3 md:hidden">
          <div className="flex flex-col gap-1">
            <NavLink to="/" end className={navLinkClass} onClick={() => setMenuOpen(false)}>
              Phim đang chiếu
            </NavLink>
            {isAuthenticated && (
              <NavLink to="/my-tickets" className={navLinkClass} onClick={() => setMenuOpen(false)}>
                Vé của tôi
              </NavLink>
            )}
            {isAuthenticated ? (
              <>
                <NavLink to="/profile" className={navLinkClass} onClick={() => setMenuOpen(false)}>
                  Tài khoản ({customer?.fullName})
                </NavLink>
                <button
                  onClick={handleLogout}
                  className="rounded-lg px-3 py-2 text-left text-sm font-medium text-red-400 hover:bg-white/5"
                >
                  Đăng xuất
                </button>
              </>
            ) : (
              <>
                <NavLink to="/login" className={navLinkClass} onClick={() => setMenuOpen(false)}>
                  Đăng nhập
                </NavLink>
                <NavLink to="/register" className={navLinkClass} onClick={() => setMenuOpen(false)}>
                  Đăng ký
                </NavLink>
              </>
            )}
          </div>
        </div>
      )}
    </header>
  );
}
