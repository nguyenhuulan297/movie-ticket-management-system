import { createContext, useCallback, useContext, useMemo, useState, type ReactNode } from "react";
import * as authApi from "../api/auth";
import type { Customer } from "../types";

const STORAGE_KEY = "cineticket.customer";

interface AuthContextValue {
  customer: Customer | null;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<Customer>;
  register: (payload: authApi.RegisterPayload) => Promise<Customer>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

function readStoredCustomer(): Customer | null {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? (JSON.parse(raw) as Customer) : null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [customer, setCustomer] = useState<Customer | null>(() => readStoredCustomer());

  const persist = useCallback((value: Customer | null) => {
    setCustomer(value);
    if (value) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(value));
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  }, []);

  const login = useCallback(
    async (email: string, password: string) => {
      const result = await authApi.login({ email, password });
      persist(result);
      return result;
    },
    [persist]
  );

  const register = useCallback(
    async (payload: authApi.RegisterPayload) => {
      const result = await authApi.register(payload);
      persist(result);
      return result;
    },
    [persist]
  );

  const logout = useCallback(() => persist(null), [persist]);

  const value = useMemo<AuthContextValue>(
    () => ({ customer, isAuthenticated: customer !== null, login, register, logout }),
    [customer, login, register, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return ctx;
}
