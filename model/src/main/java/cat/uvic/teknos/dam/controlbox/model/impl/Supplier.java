package cat.uvic.teknos.dam.controlbox.model.impl;

import cat.uvic.teknos.dam.controlbox.model.Order;
import cat.uvic.teknos.dam.controlbox.model.Product;

import java.util.Set;

public class Supplier implements cat.uvic.teknos.dam.controlbox.model.Supplier {
    private Long id;
    private String companyName;
    private String contactName;
    private String email;
    private String phone;
    private String address;
    private Set<Order> orders;
    private Set<Product> products;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long supplierId) {
        this.id = supplierId;
    }

    @Override
    public String getCompanyName() {
        return companyName;
    }

    @Override
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
    public void setAddress(String supplierAddress) {
        this.address = supplierAddress;
    }

    @Override
    public Set<Order> getOrders() {
        return orders;
    }

    @Override
    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public Set<Product> getProducts() {
        return products;
    }

    @Override
    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}