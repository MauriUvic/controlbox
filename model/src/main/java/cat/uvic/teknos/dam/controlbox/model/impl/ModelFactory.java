package cat.uvic.teknos.dam.controlbox.model.impl;

import cat.uvic.teknos.dam.controlbox.model.Movement;
import cat.uvic.teknos.dam.controlbox.model.Order;
import cat.uvic.teknos.dam.controlbox.model.OrderDetail;
import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.model.ProductDetail;
import cat.uvic.teknos.dam.controlbox.model.ProductSupplier;
import cat.uvic.teknos.dam.controlbox.model.Request;
import cat.uvic.teknos.dam.controlbox.model.Supplier;

public class ModelFactory implements cat.uvic.teknos.dam.controlbox.model.ModelFactory {
    @Override
    public Product newProduct() {
        return null;
    }

    @Override
    public Request newRequest() {
        return null;
    }

    @Override
    public Order newOrder() {
        return null;
    }

    @Override
    public Supplier newSupplier() {
        return null;
    }

    @Override
    public ProductSupplier newProductSupplier() {
        return null;
    }

    @Override
    public Movement newMovement() {
        return null;
    }

    @Override
    public ProductDetail newProductDetail() {
        return null;
    }

    @Override
    public OrderDetail newOrderDetail() {
        return null;
    }
}
