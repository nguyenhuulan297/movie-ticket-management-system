package model;

public class NormalCustomer extends Customer {

    public NormalCustomer() {
        super();
<<<<<<< Updated upstream
    }

    public NormalCustomer(String userId, String fullName, String phone, String email, CustomerType customerType) {
        super(userId, fullName, phone, email, customerType);
=======
        setCustomerType(CustomerType.NORMAL);
    }

    public NormalCustomer(String userId, String fullName, String phone, String email) {
        super(userId, fullName, phone, email, CustomerType.NORMAL);
>>>>>>> Stashed changes
    }

    @Override
    public String toString() {
        return "NormalCustomer{" + super.toString() + "}";
    }
}
