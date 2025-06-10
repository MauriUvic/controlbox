package cat.uvic.teknos.dam.controlbox.repositories;


import cat.uvic.teknos.dam.controlbox.model.ProductSupplier;

public interface RepositoryFactory {
    ProductRepository getProductRepository();
    RequestRepostory getRequestRepository();
    SupplierRepostory getSupplierRepository();
    OrderRepository getOrderRepository();
    ProductSupplierRepository getProductSupplierRepository();
    MovementRepository getMovementRepository();
    ProductSupplier getProductSupplier();
}