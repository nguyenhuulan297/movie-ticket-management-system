package model;

public class VIPCustomer extends Customer {
    private String memberSince;
    private int pointBalance;

    public VIPCustomer() {
        super();
    }

    public VIPCustomer(String userId, String fullName, String phone, String email,
                       CustomerType customerType, String memberSince, int pointBalance) {
        super(userId, fullName, phone, email, customerType);
        this.memberSince = memberSince;
        this.pointBalance = pointBalance;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    public int getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(int pointBalance) {
        this.pointBalance = pointBalance;
    }

    @Override
    public String toString() {
        return "VIPCustomer{" + super.toString() + ", memberSince='" + memberSince + '\'' +
                ", pointBalance=" + pointBalance + '}';
    }
}
