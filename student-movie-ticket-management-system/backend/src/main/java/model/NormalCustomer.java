package model;

public class NormalCustomer extends Customer {

    public NormalCustomer() {
        super();
        setCustomerType(CustomerType.NORMAL);
    }

    public NormalCustomer(String userId, String fullName, String phone, String email) {
        super(userId, fullName, phone, email, CustomerType.NORMAL);
    }

    @Override
    public String toString() {
        return "NormalCustomer{" + super.toString() + "}";
    }
}
