package dao;

import model.Order;
import java.util.List;

public interface OrderDAO {
    void addOrder(Order order);
    Order getOrderById(int id);
    List<Order> getAllOrders();
    void updateOrderStatus(int orderId, int statusId);
    void deleteOrder(int id);
}