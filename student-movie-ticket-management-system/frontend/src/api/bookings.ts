import { api } from "./client";
import type { Booking, Payment, PaymentMethodCode, Ticket } from "../types";

export async function createBooking(
  customerId: string,
  showtimeId: string,
  seatIds: string[]
): Promise<Booking> {
  const { data } = await api.post<Booking>("/bookings", { customerId, showtimeId, seatIds });
  return data;
}

export async function fetchTicketById(ticketId: string): Promise<Ticket> {
  const { data } = await api.get<Ticket>(`/tickets/${ticketId}`);
  return data;
}

export async function fetchTicketsByCustomer(customerId: string): Promise<Ticket[]> {
  const { data } = await api.get<Ticket[]>("/tickets", { params: { customerId } });
  return data;
}

export async function payForTicket(ticketId: string, method: PaymentMethodCode): Promise<Payment> {
  const { data } = await api.post<Payment>("/payments", { ticketId, method });
  return data;
}
