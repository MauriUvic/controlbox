package cat.uvic.teknos.dam.controlbox.repositories;

import cat.uvic.teknos.dam.controlbox.model.ProductSupplier;

// Rename interface to ProductSupplierRepository
public interface ProductSupplierRepository extends Repository<ProductSupplier, Integer> {

    ProductSupplier get(Integer id);

    ProductSupplier getProductSupplierById(Long id);

    void delete(ProductSupplier productSupplier);
}
