package repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Payment;
import org.springframework.stereotype.Repository;
import utils.FileUtils;
import utils.IdGenerator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PaymentRepository {
    private static final String DEFAULT_FILE_PATH = "data/payments.json";

    private final String filePath;
    private final Gson gson = new Gson();

    public PaymentRepository() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Cho phép truyền filePath tùy chỉnh (dùng khi test, tránh ghi đè dữ liệu thật trong data/payments.json).
     */
    public PaymentRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Payment> findAll() {
        String json = FileUtils.readJsonFile(filePath);
        Type listType = new TypeToken<List<Payment>>(){}.getType();
        List<Payment> payments = gson.fromJson(json, listType);
        return payments != null ? payments : new ArrayList<>();
    }

    public Payment findById(String id) {
        return findAll().stream()
                .filter(p -> p.getPaymentId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Payment> findByTicketId(String ticketId) {
        return findAll().stream()
                .filter(p -> p.getTicketId().equals(ticketId))
                .collect(Collectors.toList());
    }

    public Payment save(Payment payment) {
        List<Payment> payments = findAll();
        if (payment.getPaymentId() == null || payment.getPaymentId().isBlank()) {
            payment.setPaymentId(IdGenerator.generatePaymentId());
        }
        payments.add(payment);
        FileUtils.writeJsonFile(filePath, gson.toJson(payments));
        return payment;
    }
}
