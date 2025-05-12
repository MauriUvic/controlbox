package cat.uvic.teknos.dam.controlbox.repositories;



public interface OrderRepostories {
    void insert(Order order);
    void update(Order order);
    void delete(Order order);
}