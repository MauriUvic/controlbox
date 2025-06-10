package cat.uvic.teknos.dam.controlbox.model;

public abstract class TestProduct implements Product {
    private Long id;
    private String name;
    private String description;
    private Double unitPrice;
    private String barcode;

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


}
