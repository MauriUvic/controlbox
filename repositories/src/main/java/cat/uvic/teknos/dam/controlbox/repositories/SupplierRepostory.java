package cat.uvic.teknos.dam.controlbox.repositories;


import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.model.Supplier;

public interface SupplierRepostory extends Repository<Supplier, Integer>{

    SupplierRepostory getSupplierById(Product id);
}

