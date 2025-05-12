package cat.uvic.teknos.dam.controlbox.repositories.model.impl;

import cat.uvic.teknos.dam.controlbox.repositories.model.OrderDetail;
import cat.uvic.teknos.dam.controlbox.repositories.model.Product;

import java.util.Set;

public class Stock implements cat.uvic.teknos.dam.controlbox.repositories.model.Stock {
    private Long id;
    private Integer quantity;
    private String location;
    private String lastCounted;
    private Product product;
    private Set<OrderDetail> orderDetails;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long stockId) {
        this.id = stockId;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getLastCounted() {
        return lastCounted;
    }

    @Override
    public void setLastCounted(String lastCounted) {
        this.lastCounted = lastCounted;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public Set<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    @Override
    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}