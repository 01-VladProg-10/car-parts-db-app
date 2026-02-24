package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int id;
    private User user;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItem> items;
}