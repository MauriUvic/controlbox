package cat.uvic.teknos.dam.controlbox.model;

import java.util.Set;

public interface Product {

    Long getId();
    void setId(Long productId);

    String getName();
    void setName(String productName);

    String getDescription();
    void setDescription(String productDescription);

    Double getUnitPrice();
    void setUnitPrice(Double unitPrice);

    Double getStock();
    void setStock(Double stock);




}
