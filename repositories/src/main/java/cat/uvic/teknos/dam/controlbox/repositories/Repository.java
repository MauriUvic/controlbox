package cat.uvic.teknos.dam.controlbox.repositories;

import java.util.Set;

public interface Repository<V, K> {
    void save(V value);
    void delete(K id);
    V get(K id);
    Set<V> getAll();
}
