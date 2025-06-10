package cat.uvic.teknos.dam.controlbox.jdbc.model;

import cat.uvic.teknos.dam.controlbox.model.*;
import java.util.Set;

public class JdbcProduct implements Product {
    private Long id;
    private String name;
    private String description;
    private Double unitPrice;
    private Double stock;



    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long productId) {
        this.id = productId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String productName) {
        this.name = productName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String productDescription) {
        this.description = productDescription;
    }

    @Override
    public Double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public Double getStock() {
        return stock;
    }

    @Override
    public void setStock(Double stock) {
        this.stock = stock;
    }



}