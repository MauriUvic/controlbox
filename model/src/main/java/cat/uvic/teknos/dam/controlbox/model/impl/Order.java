package cat.uvic.teknos.dam.controlbox.model.impl;

import cat.uvic.teknos.dam.controlbox.model.Supplier;
import cat.uvic.teknos.dam.controlbox.model.OrderDetail;

import java.util.Set;

public class Order implements cat.uvic.teknos.dam.controlbox.model.Order {

    private Long id;
    private String date;
    private Double totalAmount;
    private String status;
    private String deliveryDate;
    private Supplier supplier;
    private Set<OrderDetail> details;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long orderId) {
        this.id = orderId;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String orderDate) {
        this.date = orderDate;
    }

    @Override
    public Double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String orderStatus) {
        this.status = orderStatus;
    }

    @Override
    public String getDeliveryDate() {
        return deliveryDate;
    }

    @Override
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
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
    public Set<OrderDetail> getDetails() {
        return details;
    }

    @Override
    public void setDetails(Set<OrderDetail> orderDetails) {
        this.details = orderDetails;
    }
}