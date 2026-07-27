import type { CustomerType, SeatType } from "../types";

const SEAT_MULTIPLIER: Record<SeatType, number> = {
  NORMAL: 1,
  DOUBLE: 1.2,
  VIP: 1.3,
};

const CUSTOMER_MULTIPLIER: Record<CustomerType, number> = {
  NORMAL: 1,
  STUDENT: 0.8,
  VIP: 0.7,
};

/**
 * Ước tính giá vé hiển thị ở FE, phản chiếu đúng công thức phía backend
 * (NormalCustomerPricePolicy / StudentPricePolicy / VIPPricePolicy).
 * Giá cuối cùng luôn được backend tính lại khi đặt vé nên đây chỉ là giá xem trước.
 */
export function estimateTicketPrice(
  basePrice: number,
  seatType: SeatType,
  customerType: CustomerType
): number {
  return basePrice * SEAT_MULTIPLIER[seatType] * CUSTOMER_MULTIPLIER[customerType];
}

export function formatCurrency(amount: number): string {
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(amount);
}

export function seatTypeLabel(type: SeatType): string {
  switch (type) {
    case "VIP": return "VIP";
    case "DOUBLE": return "Ghế đôi";
    default: return "Ghế thường";
  }
}

export function customerTypeLabel(type: CustomerType): string {
  switch (type) {
    case "STUDENT": return "Sinh viên";
    case "VIP": return "Thành viên VIP";
    default: return "Khách thường";
  }
}
