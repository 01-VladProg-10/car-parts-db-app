package dao;

import model.CarModel;
import java.util.List;

public interface CarModelDAO {
    void addCarModel(CarModel carModel);
    CarModel getCarModelById(int id);
    List<CarModel> getAllCarModels();
    void updateCarModel(CarModel carModel);
    void deleteCarModel(int id);
}