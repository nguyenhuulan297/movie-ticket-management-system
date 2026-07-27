package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exception.CustomerNotFoundException;
import model.Customer;
import org.springframework.stereotype.Repository;
import utils.FileUtils;
import utils.IdGenerator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomerRepository {
    private static final String DEFAULT_FILE_PATH = "data/customers.json";

    private final String filePath;
    private final Gson gson;

    public CustomerRepository() {
        this(DEFAULT_FILE_PATH);
    }

    CustomerRepository(String filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Customer.class, new CustomerTypeAdapter())
                .create();
        IdGenerator.initCustomerCounter(
                findAll().stream().map(Customer::getUserId).collect(Collectors.toList()));
    }

    public List<Customer> findAll() {
        String json = FileUtils.readJsonFile(filePath);
        Type listType = new TypeToken<List<Customer>>(){}.getType();
        List<Customer> customers = gson.fromJson(json, listType);
        return customers != null ? customers : new ArrayList<>();
    }

    public Customer findById(String id) {
        return findAll().stream()
                .filter(c -> c.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Customer save(Customer customer) {
        List<Customer> customers = findAll();
        if (customer.getUserId() == null || customer.getUserId().isBlank()) {
            customer.setUserId(IdGenerator.generateCustomerId());
        }
        customers.add(customer);
        FileUtils.writeJsonFile(filePath, gson.toJson(customers));
        return customer;
    }

    public Customer update(Customer customer) {
        List<Customer> customers = findAll();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUserId().equals(customer.getUserId())) {
                customers.set(i, customer);
                FileUtils.writeJsonFile(filePath, gson.toJson(customers));
                return customer;
            }
        }
        throw new CustomerNotFoundException("Không tìm thấy khách hàng với ID: " + customer.getUserId());
    }

    public boolean deleteById(String id) {
        List<Customer> customers = findAll();
        boolean removed = customers.removeIf(c -> c.getUserId().equals(id));
        if (removed) {
            FileUtils.writeJsonFile(filePath, gson.toJson(customers));
        }
        return removed;
    }
}
