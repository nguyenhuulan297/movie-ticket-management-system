package repository;

import com.google.gson.Gson;
<<<<<<< Updated upstream
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import model.Customer;
import model.CustomerType;
import model.NormalCustomer;
import model.StudentCustomer;
import model.VIPCustomer;
import utils.FileUtils;

=======
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exception.CustomerNotFoundException;
import model.Customer;
import org.springframework.stereotype.Repository;
import utils.FileUtils;
import utils.IdGenerator;

import java.lang.reflect.Type;
>>>>>>> Stashed changes
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

<<<<<<< Updated upstream
public class CustomerRepository {
    private static final String FILE_PATH = "data/customers.json";
    private final Gson gson = new Gson();

    public List<Customer> findAll() {
        String json = FileUtils.readJsonFile(FILE_PATH);
        JsonArray array = JsonParser.parseString(json).getAsJsonArray();
        List<Customer> customers = new ArrayList<>();
        for (JsonElement element : array) {
            JsonElement typeElement = element.getAsJsonObject().get("customerType");
            CustomerType type = typeElement != null ? CustomerType.valueOf(typeElement.getAsString()) : CustomerType.NORMAL;
            Customer customer;
            switch (type) {
                case STUDENT:
                    customer = gson.fromJson(element, StudentCustomer.class);
                    break;
                case VIP:
                    customer = gson.fromJson(element, VIPCustomer.class);
                    break;
                default:
                    customer = gson.fromJson(element, NormalCustomer.class);
            }
            customers.add(customer);
        }
        return customers;
=======
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
>>>>>>> Stashed changes
    }

    public Customer findById(String id) {
        return findAll().stream()
                .filter(c -> c.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }

<<<<<<< Updated upstream
    public void save(Customer customer) {
        List<Customer> customers = findAll();
        customers.add(customer);
        FileUtils.writeJsonFile(FILE_PATH, gson.toJson(customers));
    }

    public void update(Customer customer) {
=======
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
>>>>>>> Stashed changes
        List<Customer> customers = findAll();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUserId().equals(customer.getUserId())) {
                customers.set(i, customer);
<<<<<<< Updated upstream
                break;
            }
        }
        FileUtils.writeJsonFile(FILE_PATH, gson.toJson(customers));
    }

    public void deleteById(String id) {
        List<Customer> customers = findAll().stream()
                .filter(c -> !c.getUserId().equals(id))
                .collect(Collectors.toList());
        FileUtils.writeJsonFile(FILE_PATH, gson.toJson(customers));
=======
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
>>>>>>> Stashed changes
    }
}
