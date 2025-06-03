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




    Integer getStock();
    void setStock(Integer stock);

    Set<OrderDetail> getOrderDetail();
    void setOrderDetail(Set<OrderDetail> orderDetail);

    Set<Movement> getMovement();
    void setMovement(Set<Movement> movement);

    Set<Request> getRequest();
    void setRequest(Set<Request> request);

    Set<Supplier> getSupplier();
    void setSupplier(Set<Supplier> supplier);

    ProductDetail getDetail();
    void setDetail(ProductDetail productDetail);

    ProductSupplier getProductSupplier();
    void setProductSupplier(ProductSupplier productSupplier);
}
