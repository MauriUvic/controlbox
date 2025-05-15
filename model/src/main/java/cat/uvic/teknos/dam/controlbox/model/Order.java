package cat.uvic.teknos.dam.controlbox.model;

import java.util.Set;

public interface Order {

    Long getId();
    void setId(Long orderId);

    String getDate();
    void setDate(String orderDate);

    Double getTotalAmount();
    void setTotalAmount(Double totalAmount);

    String getStatus();
    void setStatus(String orderStatus);

    String getDeliveryDate();
    void setDeliveryDate(String deliveryDate);

    Supplier getSupplier();
    void setSupplier(Supplier supplier);

    Set<OrderDetail> getDetails();
    void setDetails(Set<OrderDetail> orderDetails);
}
