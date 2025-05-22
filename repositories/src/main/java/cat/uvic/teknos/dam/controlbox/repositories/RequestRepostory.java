package cat.uvic.teknos.dam.controlbox.repositories;


import cat.uvic.teknos.dam.controlbox.model.impl.Request;

public interface RequestRepostory extends Repository<Integer, Request> {

    cat.uvic.teknos.dam.controlbox.jdbc.modal.JdbcRequest getRequestById(int id);
}