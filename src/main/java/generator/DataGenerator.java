package generator;

import dao.*;
import dao.impl.*;
import model.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;

public class DataGenerator {

    private final Connection connection;
    private final RandomUtils randomUtils = new RandomUtils();
    private final FakerService fakerService = new FakerService();
    private final ProductConfig productConfig = new ProductConfig();

    // DAO
    private final RoleDAO roleDAO;
    private final UserDAO userDAO;
    private final CategoryDAO categoryDAO;
    private final SupplierDAO supplierDAO;
    private final ProductDAO productDAO;
    private final CarModelDAO carModelDAO;
    private final CarDAO carDAO;
    private final OrderStatusDAO orderStatusDAO;
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final PaymentDAO paymentDAO;
    private final ShipmentDAO shipmentDAO;
    private final ProductCarModelDAO productCarModelDAO;

    // pamięć tymczasowa
    private final List<Product> products = new ArrayList<>();
    private final List<CarModel> carModels = new ArrayList<>();
    private final List<Category> categories = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Supplier> suppliers = new ArrayList<>();
    private final List<OrderStatus> orderStatuses = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    public DataGenerator(Connection connection) {
        this.connection = connection;

        roleDAO = new RoleDAOImpl(connection);
        userDAO = new UserDAOImpl(connection);
        categoryDAO = new CategoryDAOImpl(connection);
        supplierDAO = new SupplierDAOImpl(connection);
        productDAO = new ProductDAOImpl(connection);
        carModelDAO = new CarModelDAOImpl(connection);
        carDAO = new CarDAOImpl(connection);
        orderStatusDAO = new OrderStatusDAOImpl(connection);
        orderDAO = new OrderDAOImpl(connection);
        orderItemDAO = new OrderItemDAOImpl(connection);
        paymentDAO = new PaymentDAOImpl(connection);
        shipmentDAO = new ShipmentDAOImpl(connection);
        productCarModelDAO = new ProductCarModelDAOImpl(connection);
    }

    // Czyszczenie bazy
    public void clearDatabase() {
        try (var stmt = connection.createStatement()) {
            stmt.execute("""
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
                    """);
            System.out.println("🧹 Database cleared successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Generatory =====

    public void generateRoles() {
        Role role = new Role(0, "ADMIN");
        roleDAO.addRole(role);
    }

    public void generateUsers(int count) {
        Role role = roleDAO.getRoleById(1); // ADMIN
        for (int i = 0; i < count; i++) {
            String name = fakerService.randomFullName();
            String email = fakerService.randomEmail(name);
            User user = new User(0, name, email, randomUtils.randomPassword(), role);
            userDAO.addUser(user);
            users.add(user);
        }
    }

    public void generateCategories() {
        Set<String> categoryNames = new HashSet<>(productConfig.getAllCategories());
        for (String name : categoryNames) {
            Category category = new Category(0, name);
            categoryDAO.addCategory(category);
            categories.add(category);
        }
    }

    public void generateSuppliers(int count) {
        for (int i = 0; i < count; i++) {
            String name = fakerService.randomFullName();
            String email = fakerService.randomEmail(name);
            Supplier supplier = new Supplier(0,
                    fakerService.randomCompany(),
                    email,
                    fakerService.randomPhone(),
                    fakerService.randomAddress());
            supplierDAO.addSupplier(supplier);
            suppliers.add(supplier);
        }
    }

    public void generateProducts(int count) {
        Random rand = new Random();
        List<String> productNames = productConfig.getAllProductNames();

        for (int i = 0; i < count; i++) {
            String name = productNames.get(rand.nextInt(productNames.size()));
            String categoryName = productConfig.getCategoryForProduct(name);
            Category category = categories.stream()
                    .filter(c -> c.getName().equals(categoryName))
                    .findFirst()
                    .orElseThrow();
            Supplier supplier = suppliers.get(rand.nextInt(suppliers.size()));

            Product product = new Product(0,
                    name,
                    "High quality " + name,
                    randomUtils.randomPrice(),
                    randomUtils.randomStock(),
                    category,
                    supplier);
            productDAO.addProduct(product);
            products.add(product);
        }
    }

    public void generateCarModels(int count) {
        for (int i = 0; i < count; i++) {
            CarModel carModel = new CarModel(0,
                    fakerService.randomCarMake(),
                    fakerService.randomCarModel(),
                    randomUtils.randomYear());
            carModelDAO.addCarModel(carModel);
            carModels.add(carModel);
        }
    }

    public void generateCars(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            Car car = new Car(0,
                    carModels.get(rand.nextInt(carModels.size())),
                    fakerService.randomVIN(),
                    fakerService.randomColor(),
                    fakerService.randomFuel());
            carDAO.addCar(car);
        }
    }

    public void generateProductCarModels() {
        Random rand = new Random();
        for (Product product : products) {
            CarModel carModel = carModels.get(rand.nextInt(carModels.size()));
            productCarModelDAO.addProductCarModel(new ProductCarModel(product.getId(), carModel.getId()));
        }
    }

    public void generateOrderStatuses() {
        OrderStatus status = new OrderStatus(0, "NEW");
        orderStatusDAO.addOrderStatus(status);
        orderStatuses.add(status);
    }

    public void generateOrders(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            User user = users.get(rand.nextInt(users.size()));
            OrderStatus status = orderStatuses.get(rand.nextInt(orderStatuses.size()));
            Order order = new Order(0, user, LocalDateTime.now(), status, new ArrayList<>());
            orderDAO.addOrder(order);
            orders.add(order);

            int itemsCount = rand.nextInt(3) + 1; // 1-3 produkty
            BigDecimal total = BigDecimal.ZERO;
            for (int j = 0; j < itemsCount; j++) {
                Product product = products.get(rand.nextInt(products.size()));
                int qty = rand.nextInt(5) + 1;
                BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(qty));
                OrderItem item = new OrderItem(0, order, product, qty, price);
                orderItemDAO.addOrderItem(item);
                total = total.add(price);
            }

            Payment payment = new Payment(0, order, total, LocalDateTime.now(), "CARD");
            paymentDAO.addPayment(payment);

            Shipment shipment = new Shipment(0, order, LocalDateTime.now(), null, fakerService.randomTrackingNumber());
            shipmentDAO.addShipment(shipment);
        }
    }
}