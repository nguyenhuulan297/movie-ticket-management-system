import { api } from "./client";
import type { Customer, CustomerType } from "../types";

export interface RegisterPayload {
  fullName: string;
  phone: string;
  email: string;
  password: string;
  customerType: CustomerType;
  studentId?: string;
  schoolName?: string;
  memberSince?: string;
  pointBalance?: number;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export async function register(payload: RegisterPayload): Promise<Customer> {
  const { data } = await api.post<Customer>("/auth/register", payload);
  return data;
}

export async function login(payload: LoginPayload): Promise<Customer> {
  const { data } = await api.post<Customer>("/auth/login", payload);
  return data;
}
