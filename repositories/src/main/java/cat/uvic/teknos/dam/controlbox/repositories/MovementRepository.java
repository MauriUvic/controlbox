package cat.uvic.teknos.dam.controlbox.repositories;

import cat.uvic.teknos.dam.controlbox.model.Movement;

public interface MovementRepository extends Repository<Movement, Integer> {

    Movement getMovementById(Integer id);

}