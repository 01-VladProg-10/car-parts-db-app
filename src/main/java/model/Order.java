package model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private User user;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItem> items;

    public Order() {}
    public Order(int id, User user, LocalDateTime orderDate, OrderStatus status, List<OrderItem> items) {
        this.id = id; this.user = user; this.orderDate = orderDate; this.status = status; this.items = items;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}