package com.example.cinema.repository;

import com.example.cinema.model.Payment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PaymentRepository {
    private static final String FILE_PATH = "data/payments.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Payment> payments = new ArrayList<>();

    public PaymentRepository() {
        loadPayments();
    }

    private void loadPayments() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try {
                payments = objectMapper.readValue(file, new TypeReference<List<Payment>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePayments() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), payments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }

    public Optional<Payment> findById(String paymentId) {
        return payments.stream()
                .filter(p -> p.getPaymentId().equals(paymentId))
                .findFirst();
    }

    public Payment save(Payment payment) {
        if (payment.getPaymentId() == null || payment.getPaymentId().isEmpty()) {
            payment.setPaymentId("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        payments.add(payment);
        savePayments();
        return payment;
    }
}