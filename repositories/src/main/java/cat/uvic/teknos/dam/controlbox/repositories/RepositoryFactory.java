package cat.uvic.teknos.dam.controlbox.repositories;



public interface RepositoryFactory {
    ProductRepository getProductRepository();
    RequestRepostory getRequestRepository();
    SupplierRepostory getSupplierRepository();
    OrderRepository getOrderRepository();
    ProductSupplierRepostory getProductSupplierRepository();
    MovementRepository getMovementRepository();
}