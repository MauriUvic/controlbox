package cat.uvic.teknos.dam.controlbox.jdbc.model;

import cat.uvic.teknos.dam.controlbox.model.Request;

public class JdbcRequest implements Request {
    private Long id;
    private Long product;
    private Integer quantity;
    private String date;
    private String status;
    private String requester;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long requestId) {
        this.id = requestId;
    }

    @Override
    public Long getProduct() {
        return product;
    }

    @Override
    public void setProduct(Long productId) {
        this.product = productId;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer requestedQuantity) {
        this.quantity = requestedQuantity;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String requestDate) {
        this.date = requestDate;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getRequester() {
        return requester;
    }

    @Override
    public void setRequester(String requester) {
        this.requester = requester;
    }
}

