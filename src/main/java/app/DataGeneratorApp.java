package app;

import generator.DataGenerator;
import util.DBConnection;

import java.sql.Connection;

public class DataGeneratorApp {

    public static void main(String[] args) {
        try (Connection connection = DBConnection.getConnection()) {
            System.out.println("Database connection established successfully.");

            DataGenerator generator = new DataGenerator(connection);

            generator.clearDatabase();
            generator.generateRoles();
            generator.generateUsers(50);       // np. 50 użytkowników
            generator.generateCategories();
            generator.generateSuppliers(20);   // np. 20 dostawców
            generator.generateProducts(1000);  // 1000 produktów
            generator.generateCarModels(20);   // 20 modeli aut
            generator.generateCars(100);       // 100 samochodów
            generator.generateProductCarModels();
            generator.generateOrderStatuses();
            generator.generateOrders(100);      // 100 zamówień

            System.out.println("\n✅ TEST DATA GENERATED SUCCESSFULLY\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}