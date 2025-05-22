package cat.uvic.teknos.dam.controlbox.jdbc;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.modal.JdbcRequest;
import cat.uvic.teknos.dam.controlbox.model.impl.Request;
import cat.uvic.teknos.dam.controlbox.repositories.RequestRepostory;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcRequestRepository implements RequestRepostory {
    private final DataSource dataSource;

    public JdbcRequestRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Request getRequestById(int id) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "SELECT * FROM REQUEST WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var request = new JdbcRequest();
                request.setId(resultSet.getLong("ID"));
                request.setProduct(resultSet.getLong("PRODUCT_ID"));
                request.setedQuantity(resultSet.getInt("REQUESTED_QUANTITY"));
                request.setDate(resultSet.getString("REQUEST_DATE"));
                request.setStatus(resultSet.getString("STATUS"));
                request.seter(resultSet.getString("REQUESTER"));
                return request;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Integer value) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "INSERT INTO REQUEST (PRODUCT_ID, REQUESTED_QUANTITY, REQUEST_DATE, STATUS, REQUESTER) VALUES (?, ?, ?, ?, ?)")) {
            JdbcRequest request = getRequestById(value);
            preparedStatement.setLong(1, request.getProduct());
            preparedStatement.setInt(2, request.getedQuantity());
            preparedStatement.setString(3, request.getDate());
            preparedStatement.setString(4, request.getStatus());
            preparedStatement.setString(5, request.geter());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer value) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "DELETE FROM REQUEST WHERE ID = ?")) {
            preparedStatement.setInt(1, value);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer get(Request id) {
        return Math.toIntExact(id.getId());
    }

    @Override
    public Set<Integer> getAll() {
        var connection = dataSource.getConnection();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT ID FROM REQUEST");
            Set<Integer> requests = new HashSet<>();
            while (resultSet.next()) {
                requests.add(resultSet.getInt("ID"));
            }
            return requests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}