package model;

public class StudentCustomer extends Customer {
    private String studentId;
    private String schoolName;

    public StudentCustomer() {
        super();
<<<<<<< Updated upstream
    }

    public StudentCustomer(String userId, String fullName, String phone, String email,
                           CustomerType customerType, String studentId, String schoolName) {
        super(userId, fullName, phone, email, customerType);
=======
        setCustomerType(CustomerType.STUDENT);
    }

    public StudentCustomer(String userId, String fullName, String phone, String email,
                            String studentId, String schoolName) {
        super(userId, fullName, phone, email, CustomerType.STUDENT);
>>>>>>> Stashed changes
        this.studentId = studentId;
        this.schoolName = schoolName;
    }

<<<<<<< Updated upstream
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Override
    public String toString() {
        return "StudentCustomer{" + super.toString() + ", studentId='" + studentId + '\'' +
=======
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

    @Override
    public String toString() {
        return "StudentCustomer{" + super.toString() +
                ", studentId='" + studentId + '\'' +
>>>>>>> Stashed changes
                ", schoolName='" + schoolName + '\'' + '}';
    }
}
