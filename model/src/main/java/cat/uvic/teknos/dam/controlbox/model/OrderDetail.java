package cat.uvic.teknos.dam.controlbox.model;

public interface OrderDetail {

    Long getId();
    void setId(Long orderDetailId);

    Double getUnitPrice();
    void setUnitPrice(Double unitPrice);

    Double getQuantity();
    void setQuantity(Double subtotal);

    Order getOrder();
    void setOrder(Order order);

    Product getProduct();
    void setProduct(Product product);


}
