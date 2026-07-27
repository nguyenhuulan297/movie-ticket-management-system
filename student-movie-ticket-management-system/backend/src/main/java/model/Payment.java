package model;

public class Payment {
    private String paymentId;
    private String ticketId;
    private String method;
    private double amount;
    private String paidAt;
    private boolean success;
    private String failureReason;

    public Payment() {}

    public Payment(String paymentId, String ticketId, String method, double amount,
                   String paidAt, boolean success, String failureReason) {
        this.paymentId = paymentId;
        this.ticketId = ticketId;
        this.method = method;
        this.amount = amount;
        this.paidAt = paidAt;
        this.success = success;
        this.failureReason = failureReason;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaidAt() { return paidAt; }
    public void setPaidAt(String paidAt) { this.paidAt = paidAt; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", ticketId='" + ticketId + '\'' +
                ", method='" + method + '\'' +
                ", amount=" + amount +
                ", paidAt='" + paidAt + '\'' +
                ", success=" + success +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}
