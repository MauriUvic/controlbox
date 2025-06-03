package cat.uvic.teknos.dam.controlbox.model;

public interface Request {

    Long getId();
    void setId(Long requestId);

    Long getProduct();
    void setProduct(Long productId);

    Integer getQuantity();
    void setQuantity(Integer requestedQuantity);

    String getDate();
    void setDate(String requestDate);

    String getStatus();
    void setStatus(String requestStatus);

    String getRequester();
    void setRequester(String requester);


}
