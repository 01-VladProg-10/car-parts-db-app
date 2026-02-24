package dao;

import model.Shipment;
import java.util.List;

public interface ShipmentDAO {
    void addShipment(Shipment shipment);
    Shipment getShipmentById(int id);
    Shipment getShipmentByOrderId(int orderId);
    List<Shipment> getAllShipments();
    void updateShipment(Shipment shipment);
    void deleteShipment(int id);
}