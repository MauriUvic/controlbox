package cat.uvic.teknos.dam.controlbox.repositories.model;

public interface ProductSupplier {

    Product getProduct();
    void setProduct(Product product);

    Supplier getSupplier();
    void setSupplier(Supplier supplier);
}
