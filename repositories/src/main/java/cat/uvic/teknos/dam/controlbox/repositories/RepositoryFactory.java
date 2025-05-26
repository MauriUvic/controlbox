package cat.uvic.teknos.dam.controlbox.repositories;



public interface RepositoryFactory {
    ProductRepostory getProductRepository();
    RequestRepostory getRequestRepository();
    StockRepostory getStockRepository();
    SupplierRepostory getSupplierRepository();
    OrderRepostory getOrderRepository();
    ProductSupplierRepostory getProductSupplierRepository();
    MovementRepostory getMovementRepository();
}