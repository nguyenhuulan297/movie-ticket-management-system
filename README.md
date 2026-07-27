# Hệ thống quản lý mua vé xem phim

Bài tập lớn OOP — Nhóm 06. Web đặt vé xem phim: chọn phim, chọn suất chiếu, chọn ghế, đặt vé và thanh toán.

## Yêu cầu cài đặt

- Java 21+
- Node.js 18+

## 1. Chạy Backend

```powershell
cd student-movie-ticket-management-system\backend
.\mvnw.cmd spring-boot:run
```

Backend chạy tại `http://localhost:8080`. Chờ đến khi thấy dòng `Started StudentMovieTicketManagementSystemApplication` là xong.

## 2. Chạy Frontend

Mở một cửa sổ terminal khác:

```powershell
cd student-movie-ticket-management-system\frontend
npm install
npm run dev
```

Frontend chạy tại `http://localhost:3001`. Mở link này bằng trình duyệt để dùng web.

> Lưu ý: phải chạy Backend trước, vì Frontend gọi API tới `localhost:8080`.

## 3. Tài khoản đăng nhập thử (demo)

Mật khẩu chung cho tất cả tài khoản demo: **Password123!**

| Email | Loại khách hàng |
|---|---|
| an.nguyen@email.com | Khách thường |
| cuong.le@student.edu.vn | Sinh viên (giảm 20%) |
| em.hoang@vip.com | Thành viên VIP (giảm 30%) |

Hoặc bấm nút **Đăng ký** để tạo tài khoản mới.

## 4. Cách sử dụng cơ bản

1. Đăng nhập (hoặc đăng ký tài khoản mới).
2. Ở trang chủ, chọn 1 phim đang chiếu.
3. Chọn suất chiếu → chọn ghế trống → bấm **Đặt vé**.
4. Bấm **Thanh toán**, chọn phương thức (Tiền mặt / Chuyển khoản / Ví điện tử) → thanh toán.
5. Vào mục **Vé của tôi** để xem lại các vé đã đặt và trạng thái thanh toán.

## Dữ liệu

Toàn bộ dữ liệu (phim, suất chiếu, ghế, vé, khách hàng...) được lưu trong các file JSON ở `student-movie-ticket-management-system/backend/data/`, không dùng database thật — chỉ phục vụ mục đích học tập/demo.
