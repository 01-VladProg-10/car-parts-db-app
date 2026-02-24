package dao;

import model.OrderStatus;
import java.util.List;

public interface OrderStatusDAO {
    void addOrderStatus(OrderStatus status);
    OrderStatus getOrderStatusById(int id);
    List<OrderStatus> getAllOrderStatuses();
    void updateOrderStatus(OrderStatus status);
    void deleteOrderStatus(int id);
}