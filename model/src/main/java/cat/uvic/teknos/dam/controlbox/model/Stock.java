package cat.uvic.teknos.dam.controlbox.model;

import java.util.Set;

public interface Stock {

    Long getId();
    void setId(Long stockId);

    Integer getQuantity();
    void setQuantity(Integer quantity);

    String getLocation();
    void setLocation(String location);

    String getLastCounted();
    void setLastCounted(String lastCounted);

    Product getProduct();
    void setProduct(Product product);

    Set<OrderDetail> getOrderDetails();
    void setOrderDetails(Set<OrderDetail> orderDetails);
}
