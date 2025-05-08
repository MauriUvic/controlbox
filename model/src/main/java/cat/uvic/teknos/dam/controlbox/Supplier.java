package cat.uvic.teknos.dam.controlbox;

import java.util.Set;

public interface Supplier {

    Long getId();
    void setId(Long supplierId);

    String getCompanyName();
    void setCompanyName(String companyName);

    String getContactName();
    void setContactName(String contactName);

    String getEmail();
    void setEmail(String email);

    String getPhone();
    void setPhone(String phone);

    String getAddress();
    void setAddress(String supplierAddress);

    Set<Order> getOrders();
    void setOrders(Set<Order> orders);

    Set<Product> getProducts();
    void setProducts(Set<Product> products);
}
