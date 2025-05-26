package cat.uvic.teknos.dam.controlbox.repositories;

import cat.uvic.teknos.dam.controlbox.model.Product;

import java.util.Set;

public interface Repository<V, K> {
    void save(Product value);
    void delete(V value);
    V get(K id);
    Set<V> getAll();
}
