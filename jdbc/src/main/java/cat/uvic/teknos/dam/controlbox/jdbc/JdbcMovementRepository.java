package cat.uvic.teknos.dam.controlbox.jdbc;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.modal.JdbcMovement;
import cat.uvic.teknos.dam.controlbox.model.Movement;
import cat.uvic.teknos.dam.controlbox.repositories.MovementRepostory;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcMovementRepository implements MovementRepostory {
    private final DataSource dataSource;

    public JdbcMovementRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Movement getMovementById(Integer id) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "SELECT * FROM MOVEMENT WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var movement = new JdbcMovement();
                movement.setId(resultSet.getLong("ID"));
                movement.setType(resultSet.getString("TYPE"));
                movement.setQuantity(resultSet.getInt("QUANTITY"));
                movement.setDate(resultSet.getString("DATE"));
                movement.setReference(resultSet.getString("REFERENCE"));
                return movement;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(int value) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "INSERT INTO MOVEMENT (TYPE, QUANTITY, DATE, REFERENCE) VALUES (?, ?, ?, ?)")) {
            Movement movement = getMovementById(value);
            preparedStatement.setString(1, movement.getType());
            preparedStatement.setInt(2, movement.getQuantity());
            preparedStatement.setString(3, movement.getDate());
            preparedStatement.setString(4, movement.getReference());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer value) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "DELETE FROM MOVEMENT WHERE ID = ?")) {
            preparedStatement.setInt(1, value);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer get(Integer id) {
        return Math.toIntExact(id); 
    }

    @Override
    public Set<Integer> getAll() {
        var connection = dataSource.getConnection();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT ID FROM MOVEMENT");
            Set<Integer> movements = new HashSet<>();
            while (resultSet.next()) {
                movements.add(resultSet.getInt("ID"));
            }
            return movements;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}