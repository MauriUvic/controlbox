package cat.uvic.teknos.dam.controlbox.repositories.model.impl;

public class Request implements cat.uvic.teknos.dam.controlbox.repositories.model.Request {
    private Long id;
    private Long productId;
    private Integer requestedQuantity;
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
    public Long getProductId() {
        return productId;
    }

    @Override
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public Integer getedQuantity() {
        return requestedQuantity;
    }

    @Override
    public void setedQuantity(Integer requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
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
    public void setStatus(String requestStatus) {
        this.status = requestStatus;
    }

    @Override
    public String geter() {
        return requester;
    }

    @Override
    public void seter(String requester) {
        this.requester = requester;
    }
}