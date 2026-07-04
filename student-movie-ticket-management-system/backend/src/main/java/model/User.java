package model;

public abstract class User {
    private String userId;
    private String fullName;
    private String phone;
    private String email;

    public User() {}

    public User(String userId, String fullName, String phone, String email) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }

    // Getters và Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("User{userId='%s', fullName='%s', phone='%s', email='%s'}",
                userId, fullName, phone, email);
    }
}
