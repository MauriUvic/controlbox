package cat.uvic.teknos.dam.controlbox;

public interface Request {

    Long getId();
    void setId(Long requestId);

    Long getProductId();
    void setProductId(Long productId);

    Integer getedQuantity();
    void setedQuantity(Integer requestedQuantity);

    String getDate();
    void setDate(String requestDate);

    String getStatus();
    void setStatus(String requestStatus);

    String geter();
    void seter(String requester);
}
