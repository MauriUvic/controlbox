package cat.uvic.teknos.dam.controlbox.jdbc.modal;


import cat.uvic.teknos.dam.controlbox.model.*;

import java.util.Set;

public class JdbcOrderDetail implements OrderDetail {


    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public void setId(Long orderDetailId) {

    }

    @Override
    public Integer getQuantity() {
        return 0;
    }

    @Override
    public void setQuantity(Integer quantity) {

    }

    @Override
    public Double getUnitPrice() {
        return 0.0;
    }

    @Override
    public void setUnitPrice(Double unitPrice) {

    }

    @Override
    public Double getSubtotal() {
        return 0.0;
    }

    @Override
    public void setSubtotal(Double subtotal) {

    }

    @Override
    public Order getOrder() {
        return null;
    }

    @Override
    public void setOrder(Order order) {

    }

    @Override
    public Product getProduct() {
        return null;
    }

    @Override
    public void setProduct(Product product) {

    }

    @Override
    public Stock getStock() {
        return null;
    }

    @Override
    public void setStock(Stock stock) {

    }
}
