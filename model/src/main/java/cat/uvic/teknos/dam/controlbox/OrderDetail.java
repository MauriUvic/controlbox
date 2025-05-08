package cat.uvic.teknos.dam.controlbox;

public interface OrderDetail {

    Long getId();
    void setId(Long orderDetailId);

    Integer getQuantity();
    void setQuantity(Integer quantity);

    Double getUnitPrice();
    void setUnitPrice(Double unitPrice);

    Double getSubtotal();
    void setSubtotal(Double subtotal);

    Order getOrder();
    void setOrder(Order order);

    Product getProduct();
    void setProduct(Product product);

    Stock getStock();
    void setStock(Stock stock);
}
