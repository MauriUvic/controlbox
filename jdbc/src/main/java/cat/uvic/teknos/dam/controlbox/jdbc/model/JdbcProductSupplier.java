package cat.uvic.teknos.dam.controlbox.jdbc.model;

import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.model.ProductSupplier;
import cat.uvic.teknos.dam.controlbox.model.Supplier;

public class JdbcProductSupplier implements ProductSupplier {
    private Product product;
    private Supplier supplier;
    private Long id;

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public Supplier getSupplier() {
        return supplier;
    }

    @Override
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
