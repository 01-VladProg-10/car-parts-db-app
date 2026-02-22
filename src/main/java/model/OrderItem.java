package model;

import java.math.BigDecimal;

public class OrderItem {
    private int id;
    private Order order;
    private Product product;
    private int quantity;
    private BigDecimal price;

    public OrderItem() {}
    public OrderItem(int id, Order order, Product product, int quantity, BigDecimal price) {
        this.id = id; this.order = order; this.product = product; this.quantity = quantity; this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}