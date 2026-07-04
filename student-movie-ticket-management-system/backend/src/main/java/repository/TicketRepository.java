package repository;

import model.Ticket;
import utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository {
    private static final String FILE_PATH = "data/tickets.json";
    private Gson gson = new Gson();

    public List<Ticket> findAll() {
        String json = FileUtils.readJsonFile(FILE_PATH);
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

    public void save(Ticket ticket) {
        List<Ticket> tickets = findAll();
        tickets.add(ticket);
        FileUtils.writeJsonFile(FILE_PATH, gson.toJson(tickets));
    }

    public void update(Ticket ticket) {
        List<Ticket> tickets = findAll();
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getTicketId().equals(ticket.getTicketId())) {
                tickets.set(i, ticket);
                break;
            }
        }
        FileUtils.writeJsonFile(FILE_PATH, gson.toJson(tickets));
    }
}