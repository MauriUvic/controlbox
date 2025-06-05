package cat.uvic.teknos.dam.controlbox.model;

public interface Supplier {
    Long getId();
    void setId(Long supplierId);

    String getName();
    void setName(String supplierName);

    String getContactName();
    void setContactName(String contactName);

    String getEmail();
    void setEmail(String email);

    String getPhone();
    void setPhone(String phone);

    String getAddress();
    void setAddress(String address);

}
