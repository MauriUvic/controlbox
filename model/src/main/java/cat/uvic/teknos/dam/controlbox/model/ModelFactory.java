package cat.uvic.teknos.dam.controlbox.model;

public interface ModelFactory {
    Product newProduct();
    Request newRequest();
    Stock newStock();
    Order newOrder();
    Supplier newSupplier();
    ProductSupplier newProductSupplier();
    Movement newMovement();
    ProductDetail newProductDetail();
    OrderDetail newOrderDetail();

}