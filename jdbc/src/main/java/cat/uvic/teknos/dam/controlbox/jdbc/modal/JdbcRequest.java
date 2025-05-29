package cat.uvic.teknos.dam.controlbox.jdbc.modal;

import cat.uvic.teknos.dam.controlbox.model.Request;

public class JdbcRequest implements Request{
    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public void setId(Long requestId) {

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
        return 0;
    }

    @Override
    public void setedQuantity(Integer requestedQuantity) {

    }

    @Override
    public String getDate() {
        return "";
    }

    @Override
    public void setDate(String requestDate) {

    }

    @Override
    public String getStatus() {
        return "";
    }

    @Override
    public void setStatus(String requestStatus) {

    }

    @Override
    public String geter() {
        return "";
    }

    @Override
    public void seter(String requester) {

    }

    public Integer getRequestedQuantity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}