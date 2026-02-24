package dao.impl;

import dao.ProductCarModelDAO;
import lombok.extern.slf4j.Slf4j;
import model.ProductCarModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductCarModelDAOImpl implements ProductCarModelDAO {

    private final Connection connection;

    public ProductCarModelDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addProductCarModel(ProductCarModel pcm) {
        String sql = "INSERT INTO product_car_model (product_id, car_model_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pcm.getProductId());
            stmt.setInt(2, pcm.getCarModelId());
            stmt.executeUpdate();
            log.info("ProductCarModel added: productId={}, carModelId={}", pcm.getProductId(), pcm.getCarModelId());
        } catch (SQLException e) {
            log.error("Error adding ProductCarModel: productId={}, carModelId={}", pcm.getProductId(), pcm.getCarModelId(), e);
        }
    }

    @Override
    public List<ProductCarModel> getAllByProductId(int productId) {
        List<ProductCarModel> list = new ArrayList<>();
        String sql = "SELECT * FROM product_car_model WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new ProductCarModel(rs.getInt("product_id"), rs.getInt("car_model_id")));
            }
            log.info("Fetched ProductCarModels for productId={}, count={}", productId, list.size());
        } catch (SQLException e) {
            log.error("Error fetching ProductCarModels for productId={}", productId, e);
        }
        return list;
    }

    @Override
    public List<ProductCarModel> getAllByCarModelId(int carModelId) {
        List<ProductCarModel> list = new ArrayList<>();
        String sql = "SELECT * FROM product_car_model WHERE car_model_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, carModelId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new ProductCarModel(rs.getInt("product_id"), rs.getInt("car_model_id")));
            }
            log.info("Fetched ProductCarModels for carModelId={}, count={}", carModelId, list.size());
        } catch (SQLException e) {
            log.error("Error fetching ProductCarModels for carModelId={}", carModelId, e);
        }
        return list;
    }

    @Override
    public void delete(ProductCarModel pcm) {
        String sql = "DELETE FROM product_car_model WHERE product_id = ? AND car_model_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pcm.getProductId());
            stmt.setInt(2, pcm.getCarModelId());
            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                log.info("ProductCarModel deleted: productId={}, carModelId={}", pcm.getProductId(), pcm.getCarModelId());
            } else {
                log.warn("No ProductCarModel found to delete: productId={}, carModelId={}", pcm.getProductId(), pcm.getCarModelId());
            }
        } catch (SQLException e) {
            log.error("Error deleting ProductCarModel: productId={}, carModelId={}", pcm.getProductId(), pcm.getCarModelId(), e);
        }
    }
}