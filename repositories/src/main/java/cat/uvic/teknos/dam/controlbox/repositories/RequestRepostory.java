package cat.uvic.teknos.dam.controlbox.repositories;


import cat.uvic.teknos.dam.controlbox.model.Request;

public interface RequestRepostory extends Repository<Request, Integer> {

    Request getRequestById(int id);
}