package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
    private int id;
    private Order order;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveryDate;
    private String trackingNumber;
}