package model;

public class NormalCustomer extends Customer {

    public NormalCustomer() {
        super();
    }

    public NormalCustomer(String userId, String fullName, String phone, String email, CustomerType customerType) {
        super(userId, fullName, phone, email, customerType);
    }

    @Override
    public String toString() {
        return "NormalCustomer{" + super.toString() + "}";
    }
}
