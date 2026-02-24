package dao.impl;

import dao.CarModelDAO;
import lombok.extern.slf4j.Slf4j;
import model.CarModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CarModelDAOImpl implements CarModelDAO {

    private final Connection connection;

    public CarModelDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCarModel(CarModel carModel) {
        String sql = "INSERT INTO car_models (make, model, year) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, carModel.getMake());
            stmt.setString(2, carModel.getModel());
            stmt.setInt(3, carModel.getYear());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    carModel.setId(rs.getInt(1));
                }
            }

            log.info("CarModel added: {} {} ({}) with id={}", carModel.getMake(), carModel.getModel(), carModel.getYear(), carModel.getId());

        } catch (SQLException e) {
            log.error("Error adding CarModel: {} {}", carModel.getMake(), carModel.getModel(), e);
        }
    }

    @Override
    public CarModel getCarModelById(int id) {
        String sql = "SELECT * FROM car_models WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CarModel carModel = new CarModel(
                        rs.getInt("id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year")
                );
                log.info("CarModel fetched by id: {}", id);
                return carModel;
            }
        } catch (SQLException e) {
            log.error("Error fetching CarModel by id: {}", id, e);
        }
        return null;
    }

    @Override
    public List<CarModel> getAllCarModels() {
        List<CarModel> carModels = new ArrayList<>();
        String sql = "SELECT * FROM car_models";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                carModels.add(new CarModel(
                        rs.getInt("id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year")
                ));
            }
            log.info("Fetched all CarModels, count: {}", carModels.size());
        } catch (SQLException e) {
            log.error("Error fetching all CarModels", e);
        }
        return carModels;
    }

    @Override
    public void updateCarModel(CarModel carModel) {
        String sql = "UPDATE car_models SET make = ?, model = ?, year = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, carModel.getMake());
            stmt.setString(2, carModel.getModel());
            stmt.setInt(3, carModel.getYear());
            stmt.setInt(4, carModel.getId());

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                log.info("CarModel updated: {}", carModel.getId());
            } else {
                log.warn("No CarModel found to update with id: {}", carModel.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating CarModel: {}", carModel.getId(), e);
        }
    }

    @Override
    public void deleteCarModel(int id) {
        String sql = "DELETE FROM car_models WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                log.info("CarModel deleted: {}", id);
            } else {
                log.warn("No CarModel found to delete with id: {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting CarModel: {}", id, e);
        }
    }
}