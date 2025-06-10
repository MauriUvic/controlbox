package cat.uvic.teknos.dam.controlbox.jdbc.model;

import cat.uvic.teknos.dam.controlbox.model.Supplier;

public class JdbcSupplier implements Supplier {
    private Long id;
    private String name;
    private String contactName;
    private String email;
    private String phone;
    private String address;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long supplierId) {
        this.id = supplierId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String supplierName) {
        this.name = supplierName;
    }

    @Override
    public String getContactName() {
        return contactName;
    }

    @Override
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }
}
