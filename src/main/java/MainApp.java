import dao.*;
import dao.impl.*;
import model.*;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainApp {

    public static void main(String[] args) {

        try {
            Connection connection = DBConnection.getConnection();

            // 🧹 1️⃣ CZYSZCZENIE BAZY
            clearDatabase(connection);

            // ===== DAO =====
            RoleDAO roleDAO = new RoleDAOImpl(connection);
            UserDAO userDAO = new UserDAOImpl(connection);
            CategoryDAO categoryDAO = new CategoryDAOImpl(connection);
            SupplierDAO supplierDAO = new SupplierDAOImpl(connection);
            ProductDAO productDAO = new ProductDAOImpl(connection);
            CarModelDAO carModelDAO = new CarModelDAOImpl(connection);
            CarDAO carDAO = new CarDAOImpl(connection);
            OrderStatusDAO orderStatusDAO = new OrderStatusDAOImpl(connection);
            OrderDAO orderDAO = new OrderDAOImpl(connection);
            OrderItemDAO orderItemDAO = new OrderItemDAOImpl(connection);
            PaymentDAO paymentDAO = new PaymentDAOImpl(connection);
            ShipmentDAO shipmentDAO = new ShipmentDAOImpl(connection);
            ProductCarModelDAO productCarModelDAO = new ProductCarModelDAOImpl(connection);

            // ===== INSERTY =====

            // 1️⃣ Role
            Role role = new Role(0, "ADMIN");
            roleDAO.addRole(role);
            System.out.println("Role added with id=" + role.getId());

            // 2️⃣ User
            User user = new User(0, "Jan Kowalski", "jan@test.pl", "1234", role);
            userDAO.addUser(user);
            System.out.println("User added with id=" + user.getId());

            // 3️⃣ Category
            Category category = new Category(0, "Silnik");
            categoryDAO.addCategory(category);
            System.out.println("Category added with id=" + category.getId());

            // 4️⃣ Supplier
            Supplier supplier = new Supplier(0, "AutoParts", "kontakt@auto.pl", "123456789", "Warszawa 1");
            supplierDAO.addSupplier(supplier);
            System.out.println("Supplier added with id=" + supplier.getId());

            // 5️⃣ Product
            Product product = new Product(0, "Filtr oleju", "Wysokiej jakości filtr", new BigDecimal("49.99"), 10, category, supplier);
            productDAO.addProduct(product);
            System.out.println("Product added with id=" + product.getId());

            // 6️⃣ CarModel
            CarModel carModel = new CarModel(0, "Toyota", "Corolla", 2020);
            carModelDAO.addCarModel(carModel);
            System.out.println("CarModel added with id=" + carModel.getId());

            // 7️⃣ Car
            Car car = new Car(0, carModel, "VIN123456789", "Czarny", "Benzyna");
            carDAO.addCar(car);
            System.out.println("Car added with VIN=" + car.getVin());

            // 8️⃣ ProductCarModel (many-to-many)
            ProductCarModel pcm = new ProductCarModel(product.getId(), carModel.getId());
            productCarModelDAO.addProductCarModel(pcm);
            System.out.println("ProductCarModel added: productId=" + pcm.getProductId() + ", carModelId=" + pcm.getCarModelId());

            // 9️⃣ OrderStatus
            OrderStatus status = new OrderStatus(0, "NEW");
            orderStatusDAO.addOrderStatus(status);
            System.out.println("OrderStatus added with id=" + status.getId());

            // 🔟 Order
            Order order = new Order(0, user, LocalDateTime.now(), status, new ArrayList<>());
            orderDAO.addOrder(order);
            System.out.println("Order added with id=" + order.getId());

            // 1️⃣1️⃣ OrderItem
            OrderItem orderItem = new OrderItem(0, order, product, 2, new BigDecimal("99.98"));
            orderItemDAO.addOrderItem(orderItem);
            System.out.println("OrderItem added for orderId=" + order.getId());

            // 1️⃣2️⃣ Payment
            Payment payment = new Payment(0, order, new BigDecimal("99.98"), LocalDateTime.now(), "CARD");
            paymentDAO.addPayment(payment);
            System.out.println("Payment added with id=" + payment.getId());

            // 1️⃣3️⃣ Shipment
            Shipment shipment = new Shipment(0, order, LocalDateTime.now(), null, "TRACK123");
            shipmentDAO.addShipment(shipment);
            System.out.println("Shipment added with id=" + shipment.getId());

            System.out.println("\n✅ TEST DATA INSERTED SUCCESSFULLY\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🧹 Metoda czyszcząca bazę
    private static void clearDatabase(Connection connection) {

        String sql = """
                TRUNCATE TABLE
                    shipments,
                    payments,
                    order_items,
                    orders,
                    product_car_model,
                    cars,
                    car_models,
                    products,
                    suppliers,
                    categories,
                    users,
                    roles,
                    order_status
                RESTART IDENTITY CASCADE;
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("🧹 Database cleared successfully.");
        } catch (Exception e) {
            System.err.println("Error clearing database");
            e.printStackTrace();
        }
    }
}