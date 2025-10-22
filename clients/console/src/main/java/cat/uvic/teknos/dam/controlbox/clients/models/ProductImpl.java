package cat.uvic.teknos.dam.controlbox.clients.models;

import cat.uvic.teknos.dam.controlbox.model.Product;

// Used by Jackson ObjectMapper for deserialization
public class ProductImpl implements Product {
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
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
