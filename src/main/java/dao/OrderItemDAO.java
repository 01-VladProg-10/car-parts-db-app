package dao;

import model.OrderItem;
import java.util.List;

public interface OrderItemDAO {
    void addOrderItem(OrderItem orderItem);
    OrderItem getOrderItemById(int id);
    List<OrderItem> getOrderItemsByOrderId(int orderId);
    List<OrderItem> getAllOrderItems();
    void updateOrderItem(OrderItem orderItem);
    void deleteOrderItem(int id);
}