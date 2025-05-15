package cat.uvic.teknos.dam.controlbox.jdbc;

import cat.uvic.teknos.dam.controlbox.model.Movement;
import cat.uvic.teknos.dam.controlbox.repositories.MovementRepostory;

import java.util.Set;

public class JdbcMovementRepository implements MovementRepostory {

    @Override
    public void save(Integer value) {

    }

    @Override
    public void delete(Integer value) {

    }

    @Override
    public Integer get(Movement id) {
        return 0;
    }

    @Override
    public Set<Integer> getAll() {
        return Set.of();
    }
}