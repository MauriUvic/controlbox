package cat.uvic.teknos.dam.controlbox.repositories;


import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.model.Order;

public interface OrderRepository extends Repository<Order, Integer> {

    Order getByProduct(Product id);
}