export type CustomerType = "NORMAL" | "STUDENT" | "VIP";
export type MovieStatus = "SHOWING" | "STOPPED";
export type SeatType = "NORMAL" | "DOUBLE" | "VIP";
export type SeatStatus = "AVAILABLE" | "BOOKED";
export type PaymentStatus = "PENDING" | "PAID" | "FAILED";
export type PaymentMethodCode = "CASH" | "BANK_TRANSFER" | "E_WALLET";

export interface Customer {
  userId: string;
  fullName: string;
  phone: string;
  email: string;
  customerType: CustomerType;
  // STUDENT only
  studentId?: string;
  schoolName?: string;
  // VIP only
  memberSince?: string;
  pointBalance?: number;
}

export interface Movie {
  movieId: string;
  title: string;
  genre: string;
  duration: number;
  ageLimit: number;
  status: MovieStatus;
}

export interface CinemaRoom {
  roomId: string;
  roomName: string;
  totalSeats: number;
}

export interface Showtime {
  showtimeId: string;
  movieId: string;
  room: CinemaRoom;
  startTime: string;
  endTime: string;
  basePrice: number;
}

export interface Seat {
  seatId: string;
  seatNumber: string;
  seatType: SeatType;
  status: SeatStatus;
  showtimeId: string;
}

export interface Ticket {
  ticketId: string;
  customer: Customer;
  showtime: Showtime;
  seat: Seat;
  finalPrice: number;
  paymentStatus: PaymentStatus;
  bookedAt: string;
}

export interface Booking {
  bookingId: string;
  customer: Customer;
  showtime: Showtime;
  tickets: Ticket[];
  totalPrice: number;
  bookingTime: string;
}

export interface Payment {
  paymentId: string;
  ticketId: string;
  method: PaymentMethodCode;
  amount: number;
  paidAt: string;
  success: boolean;
  failureReason?: string | null;
}

export interface ApiError {
  message: string;
  status: number;
}
