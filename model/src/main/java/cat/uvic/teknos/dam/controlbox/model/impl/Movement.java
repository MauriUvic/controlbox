package cat.uvic.teknos.dam.controlbox.model.impl;

import cat.uvic.teknos.dam.controlbox.model.Product;

public class Movement implements cat.uvic.teknos.dam.controlbox.model.Movement {

    private Long id;
    private String type;
    private Integer quantity;
    private String date;
    private String reference;
    private Product product;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long movementId) {
        this.id = movementId;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String movementType) {
        this.type = movementType;
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
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String movementDate) {
        this.date = movementDate;
    }

    @Override
    public String getReference() {
        return reference;
    }

    @Override
    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }
}