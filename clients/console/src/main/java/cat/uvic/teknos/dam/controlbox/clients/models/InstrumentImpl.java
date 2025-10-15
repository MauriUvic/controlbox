package cat.uvic.teknos.dam.bandhub.client.models;

import cat.uvic.teknos.dam.bandhub.client.exceptions.ClientException;
import cat.uvic.teknos.dam.bandhub.model.Instrument;
import cat.uvic.teknos.dam.bandhub.model.Musician;

import java.util.Set;

public class InstrumentImpl implements Instrument {
    private int id;
    private String description;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Set<Musician> getMusicians() {
        return Set.of();
    }

    @Override
    public void setMusicians(Set<Musician> musicians) {

    }
}
