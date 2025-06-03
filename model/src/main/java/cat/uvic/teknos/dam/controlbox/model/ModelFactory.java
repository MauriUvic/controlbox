package cat.uvic.teknos.dam.controlbox.model;

public interface ModelFactory {
    Product newProduct();
    Request newRequest();
    Order newOrder();
    Supplier newSupplier();
    ProductSupplier newProductSupplier();
    Movement newMovement();
    ProductDetail newProductDetail();
    OrderDetail newOrderDetail();

}