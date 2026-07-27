package model;

public class StudentCustomer extends Customer {
    private String studentId;
    private String schoolName;

    public StudentCustomer() {
        super();
        setCustomerType(CustomerType.STUDENT);
    }

    public StudentCustomer(String userId, String fullName, String phone, String email,
                            String studentId, String schoolName) {
        super(userId, fullName, phone, email, CustomerType.STUDENT);
        this.studentId = studentId;
        this.schoolName = schoolName;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

    @Override
    public String toString() {
        return "StudentCustomer{" + super.toString() +
                ", studentId='" + studentId + '\'' +
                ", schoolName='" + schoolName + '\'' + '}';
    }
}
