package repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import model.Customer;
import model.CustomerType;
import model.NormalCustomer;
import model.StudentCustomer;
import model.VIPCustomer;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    }

    public Customer findById(String id) {
        return findAll().stream()
                .filter(c -> c.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Customer customer) {
        List<Customer> customers = findAll();
        customers.add(customer);
        FileUtils.writeJsonFile(FILE_PATH, gson.toJson(customers));
    }

    public void update(Customer customer) {
        List<Customer> customers = findAll();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUserId().equals(customer.getUserId())) {
                customers.set(i, customer);
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
    }
}
