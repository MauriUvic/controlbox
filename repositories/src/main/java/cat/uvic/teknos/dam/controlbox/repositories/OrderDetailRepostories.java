package cat.uvic.teknos.dam.controlbox.repositories;



public interface OrderDetailRepostories {
    void insert(OrderDetail orderdetail);
    void update(OrderDetail orderdetail);
    void delete(OrderDetail orderdetail);
}