package cat.uvic.teknos.dam.controlbox.repositories;



public interface RequestRepostories {
    void insert(Request request);
    void update(Request request);
    void delete(Request request);
}