package cat.uvic.teknos.dam.controlbox.repositories.model.impl;

import cat.uvic.teknos.dam.controlbox.repositories.model.Product;

public class ProductDetail implements cat.uvic.teknos.dam.controlbox.repositories.model.ProductDetail {
    private Product product;
    private String manufacturer;
    private Integer warrantyMonths;
    private String manualUrl;

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public Integer getWarrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getManualUrl() {
        return manualUrl;
    }

    @Override
    public void setManualUrl(String manualUrl) {
        this.manualUrl = manualUrl;
    }
}