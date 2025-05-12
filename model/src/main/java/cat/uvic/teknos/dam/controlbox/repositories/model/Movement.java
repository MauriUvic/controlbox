package cat.uvic.teknos.dam.controlbox.repositories.model;

public interface Movement {

    Long getId();
    void setId(Long movementId);

    String getType();
    void setType(String movementType);

    Integer getQuantity();
    void setQuantity(Integer quantity);

    String getDate();
    void setDate(String movementDate);

    String getReference();
    void setReference(String reference);

    Product getProduct();
    void setProduct(Product product);
}
