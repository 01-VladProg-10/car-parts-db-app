package dao;

import model.ProductCarModel;
import java.util.List;

public interface ProductCarModelDAO {
    void addProductCarModel(ProductCarModel pcm);
    List<ProductCarModel> getAllByProductId(int productId);
    List<ProductCarModel> getAllByCarModelId(int carModelId);
    void delete(ProductCarModel pcm);
}