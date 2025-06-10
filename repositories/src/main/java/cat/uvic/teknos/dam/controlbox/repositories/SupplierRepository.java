package cat.uvic.teknos.dam.controlbox.repositories;

import cat.uvic.teknos.dam.controlbox.model.Supplier;
import java.util.Set;

public interface SupplierRepository {
    Supplier get(Integer id);
    void save(Supplier supplier);
    void delete(Integer id);
    Set<Supplier> getAll();
}
