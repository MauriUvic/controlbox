package cat.uvic.teknos.dam.controlbox.repositories.model.impl;

import cat.uvic.teknos.dam.controlbox.repositories.model.Product;
import cat.uvic.teknos.dam.controlbox.repositories.model.Supplier;

public class ProductSupplier implements cat.uvic.teknos.dam.controlbox.repositories.model.ProductSupplier {
    private Product product;
    private Supplier supplier;

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
}