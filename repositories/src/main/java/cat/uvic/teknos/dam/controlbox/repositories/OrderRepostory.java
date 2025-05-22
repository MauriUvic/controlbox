package cat.uvic.teknos.dam.controlbox.repositories;


import cat.uvic.teknos.dam.controlbox.model.impl.Order;

public interface OrderRepostory extends Repository<Integer, Order> {

    Order getOrderById(int id);
}