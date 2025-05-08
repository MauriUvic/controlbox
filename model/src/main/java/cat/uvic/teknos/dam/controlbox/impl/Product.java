package cat.uvic.teknos.dam.controlbox.impl;


import cat.uvic.teknos.dam.controlbox.Stock;
import cat.uvic.teknos.dam.controlbox.Movement;
import cat.uvic.teknos.dam.controlbox.OrderDetail;
import cat.uvic.teknos.dam.controlbox.Request;
import cat.uvic.teknos.dam.controlbox.Supplier;
import cat.uvic.teknos.dam.controlbox.ProductDetail;
import java.util.Set;

public class Product implements cat.uvic.teknos.dam.controlbox.Product {
    private String barcode;
    private Long id;
    private String name;
    private String description;
    private Double unitPrice;
    private String category;
    private String createdAt;
    private Set<Stock> stock;
    private Set<OrderDetail> orderDetail;
    private Set<Movement> movement;
    private Set<Request> request;
    private Set<Supplier> supplier;
    private ProductDetail detail;

    @Override
    public String getBarcode() {
        return barcode;
    }

    @Override
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long productId) {
        this.id = productId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String productName) {
        this.name = productName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String productDescription) {
        this.description = productDescription;
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
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Set<Stock> getStock() {
        return stock;
    }

    @Override
    public void setStock(Set<Stock> stock) {
        this.stock = stock;
    }

    @Override
    public Set<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    @Override
    public void setOrderDetail(Set<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public Set<Movement> getMovement() {
        return movement;
    }

    @Override
    public void setMovement(Set<Movement> movement) {
        this.movement = movement;
    }

    @Override
    public Set<Request> getRequest() {
        return request;
    }

    @Override
    public void setRequest(Set<Request> request) {
        this.request = request;
    }

    @Override
    public Set<Supplier> getSupplier() {
        return supplier;
    }

    @Override
    public void setSupplier(Set<Supplier> supplier) {
        this.supplier = supplier;
    }

    @Override
    public ProductDetail getDetail() {
        return detail;
    }

    @Override
    public void setDetail(ProductDetail productDetail) {
        this.detail = productDetail;
    }
}