package cat.uvic.teknos.dam.controlbox.impl;

import cat.uvic.teknos.dam.controlbox.Product;
import cat.uvic.teknos.dam.controlbox.Supplier;

public class ProductSupplier implements cat.uvic.teknos.dam.controlbox.ProductSupplier {
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