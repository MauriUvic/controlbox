package cat.uvic.teknos.dam.controlbox.model;

public interface ProductDetail {

    Product getProduct();
    void setProduct(Product product);

    String getManufacturer();
    void setManufacturer(String manufacturer);

    Integer getWarrantyMonths();
    void setWarrantyMonths(Integer warrantyMonths);

    String getManualUrl();
    void setManualUrl(String manualUrl);
}
