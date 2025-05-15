package cat.uvic.teknos.dam.controlbox.model.impl;

import cat.uvic.teknos.dam.controlbox.model.Order;
import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.model.Stock;
import cat.uvic.teknos.dam.controlbox.model.OrderDetail;

public class OrderDetall implements OrderDetail {

    private Long id;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
    private Order order;
    private Product product;
    private Stock stock;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long orderDetailId) {
        this.id = orderDetailId;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public Double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public Double getSubtotal() {
        return subtotal;
    }

    @Override
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public Stock getStock() {
        return stock;
    }

    @Override
    public void setStock(Stock stock) {
        this.stock = stock;
    }
}