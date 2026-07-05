package service;

import model.*;
import repository.TicketRepository;
import exception.*;
import utils.IdGenerator;
import utils.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final CustomerService customerService;
    private final ShowtimeService showtimeService;
    private final SeatService seatService;
    private final TicketRepository ticketRepository;

    public BookingService(CustomerService customerService,
                          ShowtimeService showtimeService,
                          SeatService seatService,
                          TicketRepository ticketRepository) {
        this.customerService = customerService;
        this.showtimeService = showtimeService;
        this.seatService = seatService;
        this.ticketRepository = ticketRepository;
    }

    public Booking bookTickets(String customerId, String showtimeId, List<String> seatIds) {
        // 0. Validate input cơ bản
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Phải chọn ít nhất 1 ghế để đặt vé");
        }

        // 1. Kiểm tra customer
        Customer customer = customerService.findById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Không tìm thấy khách hàng với ID: " + customerId);
        }

        // 2. Kiểm tra showtime
        Showtime showtime = showtimeService.findById(showtimeId);
        if (showtime == null) {
            throw new ShowtimeNotFoundException("Không tìm thấy suất chiếu với ID: " + showtimeId);
        }

        // 3. Kiểm tra phim đang chiếu
        if (!showtimeService.isMovieShowing(showtime)) {
            throw new MovieNotShowingException("Phim đã ngừng chiếu, không thể đặt vé");
        }

        // 4. PASS 1: kiểm tra TẤT CẢ ghế hợp lệ và còn trống trước khi đặt bất kỳ ghế nào
        List<Seat> seats = new ArrayList<>();
        for (String seatId : seatIds) {
            Seat seat = seatService.findById(seatId);
            if (seat == null) {
                throw new SeatNotFoundException("Không tìm thấy ghế với ID: " + seatId);
            }
            if (!seatService.isAvailable(seatId)) {
                throw new SeatAlreadyBookedException("Ghế " + seatId + " đã được đặt, không thể chọn");
            }
            seats.add(seat);
        }

        // 5. PASS 2: mọi ghế đều hợp lệ -> tiến hành đặt từng ghế và tạo vé tương ứng
        TicketPricePolicy policy = customerService.getPricePolicy(customer.getCustomerType());
        List<Ticket> tickets = new ArrayList<>();
        double totalPrice = 0.0;
        String bookedAt = LocalDateTime.now().toString();

        for (Seat seat : seats) {
            double finalPrice = policy.calculatePrice(showtime.getBasePrice(), seat.getSeatType());
            Validator.validatePrice(finalPrice);

            Ticket ticket = new Ticket();
            ticket.setTicketId(IdGenerator.generateTicketId());
            ticket.setCustomer(customer);
            ticket.setShowtime(showtime);
            ticket.setSeat(seat);
            ticket.setFinalPrice(finalPrice);
            ticket.setPaymentStatus(PaymentStatus.PENDING);
            ticket.setBookedAt(bookedAt);

            seatService.bookSeat(seat.getSeatId());   // cập nhật trạng thái ghế -> BOOKED
            ticketRepository.save(ticket);            // lưu vé

            tickets.add(ticket);
            totalPrice += finalPrice;
        }

        // 6. Gộp thành 1 Booking để trả về cho client
        Booking booking = new Booking();
        booking.setBookingId(IdGenerator.generateBookingId());
        booking.setCustomer(customer);
        booking.setShowtime(showtime);
        booking.setTickets(tickets);
        booking.setTotalPrice(totalPrice);
        booking.setBookingTime(bookedAt);

        return booking;
    }
}