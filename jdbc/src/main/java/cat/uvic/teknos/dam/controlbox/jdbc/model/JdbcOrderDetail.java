package cat.uvic.teknos.dam.controlbox.jdbc.model;


import cat.uvic.teknos.dam.controlbox.model.*;

public class JdbcOrderDetail implements OrderDetail {


    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public void setId(Long orderDetailId) {

    }

    @Override
    public Double getQuantity() {
        return 0.0;
    }

    @Override
    public void setQuantity(Double quantity) {

    }

    @Override
    public Double getUnitPrice() {
        return 0.0;
    }

    @Override
    public void setUnitPrice(Double unitPrice) {

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

}
