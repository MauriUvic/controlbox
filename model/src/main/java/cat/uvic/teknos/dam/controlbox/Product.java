package cat.uvic.teknos.dam.controlbox;

import java.util.Set;

public interface Product {

    String getBarcode();
    void setBarcode(String barcode);

    Long getId();
    void setId(Long productId);

    String getName();
    void setName(String productName);

    String getDescription();
    void setDescription(String productDescription);

    Double getUnitPrice();
    void setUnitPrice(Double unitPrice);

    String getCategory();
    void setCategory(String category);

    String getCreatedAt();
    void setCreatedAt(String createdAt);

    Set<Stock> getStock();
    void setStock(Set<Stock> stock);

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
}
