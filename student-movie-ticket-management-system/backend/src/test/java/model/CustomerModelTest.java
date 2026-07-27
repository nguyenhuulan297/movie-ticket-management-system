package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerModelTest {

    @Test
    void normalCustomer_setsCustomerTypeNormal() {
        NormalCustomer customer = new NormalCustomer("CUS001", "Nguyen Van A", "0901234567", "a@email.com");

        assertEquals(CustomerType.NORMAL, customer.getCustomerType());
        assertEquals("CUS001", customer.getUserId());
    }

    @Test
    void studentCustomer_keepsStudentFields() {
        StudentCustomer customer = new StudentCustomer("CUS003", "Le Minh C", "0923456789",
                "c@student.edu.vn", "SV123456", "Dai hoc Bach Khoa");

        assertEquals(CustomerType.STUDENT, customer.getCustomerType());
        assertEquals("SV123456", customer.getStudentId());
        assertEquals("Dai hoc Bach Khoa", customer.getSchoolName());
    }

    @Test
    void vipCustomer_keepsVipFields() {
        VIPCustomer customer = new VIPCustomer("CUS005", "Hoang Van E", "0945678901",
                "e@vip.com", "2024-01-15", 350);

        assertEquals(CustomerType.VIP, customer.getCustomerType());
        assertEquals("2024-01-15", customer.getMemberSince());
        assertEquals(350, customer.getPointBalance());
    }
}
