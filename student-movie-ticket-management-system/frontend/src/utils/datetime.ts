/**
 * Backend trả ngày giờ ở 2 dạng: "yyyy-MM-dd HH:mm:ss" (Showtime) và
 * LocalDateTime.toString() có thể có tới 9 chữ số phần thập phân giây (Ticket.bookedAt),
 * vượt quá độ chính xác mili-giây mà Date.parse hỗ trợ ổn định trên mọi trình duyệt.
 * Hàm này chuẩn hoá về dạng ISO an toàn trước khi parse.
 */
function toParsableIso(raw: string): string {
  const withT = raw.includes(" ") ? raw.replace(" ", "T") : raw;
  return withT.replace(/(\.\d{3})\d+$/, "$1");
}

export function parseBackendDate(raw: string): Date {
  return new Date(toParsableIso(raw));
}

export function formatDateTime(raw: string): string {
  const date = parseBackendDate(raw);
  if (Number.isNaN(date.getTime())) return raw;
  return date.toLocaleString("vi-VN", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

export function formatDate(raw: string): string {
  const date = parseBackendDate(raw);
  if (Number.isNaN(date.getTime())) return raw;
  return date.toLocaleDateString("vi-VN", { weekday: "short", day: "2-digit", month: "2-digit" });
}

export function formatTime(raw: string): string {
  const date = parseBackendDate(raw);
  if (Number.isNaN(date.getTime())) return raw;
  return date.toLocaleTimeString("vi-VN", { hour: "2-digit", minute: "2-digit" });
}

export function formatDuration(minutes: number): string {
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  return h > 0 ? `${h}h${m > 0 ? ` ${m}p` : ""}` : `${m} phút`;
}
