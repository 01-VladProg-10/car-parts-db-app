package dao;

import model.Supplier;
import java.util.List;

public interface SupplierDAO {
    void addSupplier(Supplier supplier);
    Supplier getSupplierById(int id);
    List<Supplier> getAllSuppliers();
    void updateSupplier(Supplier supplier);
    void deleteSupplier(int id);
}