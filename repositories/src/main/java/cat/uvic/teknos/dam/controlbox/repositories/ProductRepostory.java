package cat.uvic.teknos.dam.controlbox.repositories;


import cat.uvic.teknos.dam.controlbox.model.impl.Product;

public interface ProductRepostory extends Repository<Integer, Product>{
    cat.uvic.teknos.dam.controlbox.model.Product getProductByName(String id);
}