package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private int id;
    private Order order;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String method;

    public Payment() {}
    public Payment(int id, Order order, BigDecimal amount, LocalDateTime paymentDate, String method) {
        this.id = id; this.order = order; this.amount = amount; this.paymentDate = paymentDate; this.method = method;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}