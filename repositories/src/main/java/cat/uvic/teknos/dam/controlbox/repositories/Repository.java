package cat.uvic.teknos.dam.controlbox.repositories;

import java.util.Set;

public interface Repository<V, K> {
    void save(int value);
    void delete(V value);
    V get(Integer id);
    Set<V> getAll();
}
