package dao.impl;

import dao.CarDAO;
import lombok.extern.slf4j.Slf4j;
import model.Car;
import model.CarModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CarDAOImpl implements CarDAO {

    private final Connection connection;

    public CarDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCar(Car car) {
        String sql = "INSERT INTO cars (car_model_id, vin, color, fuel_type) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, car.getCarModel().getId());
            stmt.setString(2, car.getVin());
            stmt.setString(3, car.getColor());
            stmt.setString(4, car.getFuelType());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    car.setId(rs.getInt(1)); // ✅ AUTO ID
                }
            }

            log.info("Car added: VIN {} with id={}", car.getVin(), car.getId());

        } catch (SQLException e) {
            log.error("Error adding Car: VIN {}", car.getVin(), e);
        }
    }

    @Override
    public Car getCarById(int id) {
        String sql = "SELECT c.*, cm.id as car_model_id, cm.make, cm.model, cm.year " +
                "FROM cars c " +
                "JOIN car_models cm ON c.car_model_id = cm.id " +
                "WHERE c.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CarModel carModel = new CarModel(
                        rs.getInt("car_model_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year")
                );
                Car car = new Car(
                        rs.getInt("id"),
                        carModel,
                        rs.getString("vin"),
                        rs.getString("color"),
                        rs.getString("fuel_type")
                );
                log.info("Car fetched by id: {}", id);
                return car;
            }
        } catch (SQLException e) {
            log.error("Error fetching Car by id: {}", id, e);
        }
        return null;
    }

    @Override
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT c.*, cm.id as car_model_id, cm.make, cm.model, cm.year " +
                "FROM cars c " +
                "JOIN car_models cm ON c.car_model_id = cm.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CarModel carModel = new CarModel(
                        rs.getInt("car_model_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year")
                );
                cars.add(new Car(
                        rs.getInt("id"),
                        carModel,
                        rs.getString("vin"),
                        rs.getString("color"),
                        rs.getString("fuel_type")
                ));
            }
            log.info("Fetched all Cars, count: {}", cars.size());
        } catch (SQLException e) {
            log.error("Error fetching all Cars", e);
        }
        return cars;
    }

    @Override
    public void updateCar(Car car) {
        String sql = "UPDATE cars SET car_model_id = ?, vin = ?, color = ?, fuel_type = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, car.getCarModel().getId());
            stmt.setString(2, car.getVin());
            stmt.setString(3, car.getColor());
            stmt.setString(4, car.getFuelType());
            stmt.setInt(5, car.getId());

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                log.info("Car updated: ID {}", car.getId());
            } else {
                log.warn("No Car found to update with ID {}", car.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating Car: ID {}", car.getId(), e);
        }
    }

    @Override
    public void deleteCar(int id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                log.info("Car deleted: ID {}", id);
            } else {
                log.warn("No Car found to delete with ID {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting Car: ID {}", id, e);
        }
    }
}