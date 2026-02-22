package model;

import java.time.LocalDateTime;

public class Shipment {
    private int id;
    private Order order;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveryDate;
    private String trackingNumber;

    public Shipment() {}
    public Shipment(int id, Order order, LocalDateTime shippedDate, LocalDateTime deliveryDate, String trackingNumber) {
        this.id = id; this.order = order; this.shippedDate = shippedDate; this.deliveryDate = deliveryDate;
        this.trackingNumber = trackingNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public LocalDateTime getShippedDate() { return shippedDate; }
    public void setShippedDate(LocalDateTime shippedDate) { this.shippedDate = shippedDate; }
    public LocalDateTime getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
}