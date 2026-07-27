import axios, { AxiosError } from "axios";
import type { ApiError } from "../types";

export const api = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: { "Content-Type": "application/json" },
});

/**
 * Backend trả lỗi nghiệp vụ dạng text/plain (message thuần), không phải JSON.
 * Chuẩn hoá về 1 dạng ApiError để UI hiển thị nhất quán.
 */
export function toApiError(error: unknown): ApiError {
  if (axios.isAxiosError(error)) {
    const err = error as AxiosError<string>;
    if (err.response) {
      const data = err.response.data;
      const message = typeof data === "string" && data.trim().length > 0
        ? data
        : "Đã có lỗi xảy ra, vui lòng thử lại";
      return { message, status: err.response.status };
    }
    if (err.request) {
      return { message: "Không thể kết nối tới máy chủ. Vui lòng kiểm tra backend đang chạy.", status: 0 };
    }
  }
  return { message: "Đã có lỗi không xác định xảy ra", status: 0 };
}
