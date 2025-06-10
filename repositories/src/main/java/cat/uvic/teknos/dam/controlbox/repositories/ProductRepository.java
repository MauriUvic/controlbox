package cat.uvic.teknos.dam.controlbox.repositories;


import cat.uvic.teknos.dam.controlbox.model.Product;

public interface ProductRepository extends Repository<Product, Integer>{
    Product getProductByName(String id);
}