package cat.uvic.teknos.dam.controlbox.repositories;


import cat.uvic.teknos.dam.controlbox.model.impl.Supplier;

public interface SupplierRepostory extends Repository<Integer, Supplier>{

    SupplierRepostory getSupplierById(int id);
}

