package cat.uvic.teknos.dam.controlbox.model.impl;

public class Request implements cat.uvic.teknos.dam.controlbox.model.Request {
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
    public Long getProduct() {
        return 0L;
    }

    @Override
    public void setProduct(Long productId) {

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

    public int getRequestedQuantity() {
        return requestedQuantity != null ? requestedQuantity : 0;
    }

    public void setRequestedQuantity(Integer integer) {
        this.requestedQuantity = integer;
    }

    public void setRequestDate(String date) {
        this.date = date;
    }

    public void setRequester(String geter) {
        this.requester = geter;
    }

    public String getRequestDate() {
        return date != null ? date : "";
    }

    public String getRequester() {
        return requester != null ? requester : "";
    }
}