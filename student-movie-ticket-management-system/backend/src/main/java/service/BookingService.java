package service;

import model.*;
import repository.TicketRepository;
import exception.*;
import utils.IdGenerator;
import utils.Validator;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final CustomerService customerService;
    private final ShowtimeService showtimeService;
    private final SeatService seatService;
    private final TicketRepository ticketRepository;

    // Constructor injection (sẽ được Spring autowire)
    public BookingService(CustomerService customerService,
                          ShowtimeService showtimeService,
                          SeatService seatService,
                          TicketRepository ticketRepository) {
        this.customerService = customerService;
        this.showtimeService = showtimeService;
        this.seatService = seatService;
        this.ticketRepository = ticketRepository;
    }

    public Ticket bookTicket(String customerId, String showtimeId, String seatId, String paymentType) {
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

        // 3. Kiểm tra phim đang chiếu (dùng MovieService)
        if (!showtimeService.isMovieShowing(showtime)) {
            throw new MovieNotShowingException("Phim đã ngừng chiếu, không thể đặt vé");
        }

        // 4. Kiểm tra ghế
        Seat seat = seatService.findById(seatId);
        if (seat == null) {
            throw new SeatNotFoundException("Không tìm thấy ghế với ID: " + seatId);
        }
        if (!seatService.isAvailable(seatId)) {
            throw new SeatAlreadyBookedException("Ghế đã được đặt, không thể chọn");
        }

        // 5. Tính giá (lấy policy từ CustomerService)
        TicketPricePolicy policy = customerService.getPricePolicy(customer.getCustomerType());
        double finalPrice = policy.calculatePrice(showtime.getBasePrice(), seat.getSeatType());
        Validator.validatePrice(finalPrice);

        // 6. Tạo vé
        Ticket ticket = new Ticket();
        ticket.setTicketId(IdGenerator.generateTicketId());
        ticket.setCustomer(customer);
        ticket.setShowtime(showtime);
        ticket.setSeat(seat);
        ticket.setFinalPrice(finalPrice);
        ticket.setPaymentStatus(PaymentStatus.PENDING);
        ticket.setBookedAt(java.time.LocalDateTime.now().toString());

        // 7. Cập nhật trạng thái ghế
        seatService.bookSeat(seatId);

        // 8. Lưu vé
        ticketRepository.save(ticket);

        return ticket;
    }
}