package repository;

import model.Customer;
import model.Ticket;
import utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TicketRepository {
    private static final String DEFAULT_FILE_PATH = "data/tickets.json";
    private final String filePath;
    // Ticket chứa Customer (abstract) -> cần adapter đa hình giống CustomerRepository,
    // nếu không Gson sẽ không biết dựng NormalCustomer/StudentCustomer/VIPCustomer khi đọc lại.
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Customer.class, new CustomerTypeAdapter())
            .create();

    public TicketRepository() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Cho phép truyền filePath tùy chỉnh (dùng khi test, tránh ghi đè dữ liệu thật trong data/tickets.json).
     */
    public TicketRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Ticket> findAll() {
        String json = FileUtils.readJsonFile(filePath);
        Type listType = new TypeToken<List<Ticket>>(){}.getType();
        List<Ticket> tickets = gson.fromJson(json, listType);
        return tickets != null ? tickets : new ArrayList<>();
    }

    public Ticket findById(String id) {
        return findAll().stream()
                .filter(t -> t.getTicketId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Ticket> findByCustomerId(String customerId) {
        return findAll().stream()
                .filter(t -> t.getCustomer() != null && t.getCustomer().getUserId().equals(customerId))
                .collect(Collectors.toList());
    }

    public void save(Ticket ticket) {
        List<Ticket> tickets = findAll();
        tickets.add(ticket);
        FileUtils.writeJsonFile(filePath, gson.toJson(tickets));
    }

    public void update(Ticket ticket) {
        List<Ticket> tickets = findAll();
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getTicketId().equals(ticket.getTicketId())) {
                tickets.set(i, ticket);
                break;
            }
        }
        FileUtils.writeJsonFile(filePath, gson.toJson(tickets));
    }
}