package cat.uvic.teknos.dam.controlbox.jdbc;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.modal.JdbcRequest;
import cat.uvic.teknos.dam.controlbox.model.impl.Request;
import cat.uvic.teknos.dam.controlbox.repositories.RequestRepostory;

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
                var jdbcRequest = new JdbcRequest();
                jdbcRequest.setId(resultSet.getLong("ID"));
                jdbcRequest.setProduct(resultSet.getLong("PRODUCT_ID"));
                jdbcRequest.setedQuantity(resultSet.getInt("REQUESTED_QUANTITY"));
                jdbcRequest.setDate(resultSet.getString("REQUEST_DATE"));
                jdbcRequest.setStatus(resultSet.getString("STATUS"));
                jdbcRequest.seter(resultSet.getString("REQUESTER"));

                var request = new Request();
                request.setId(jdbcRequest.getId());
                request.setProduct(jdbcRequest.getProduct());
                request.setRequestedQuantity(jdbcRequest.getedQuantity());
                request.setRequestDate(jdbcRequest.getDate());
                request.setStatus(jdbcRequest.getStatus());
                request.setRequester(jdbcRequest.geter());
                return request;
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
                "INSERT INTO REQUEST (PRODUCT_ID, REQUESTED_QUANTITY, REQUEST_DATE, STATUS, REQUESTER) VALUES (?, ?, ?, ?, ?)")) {
            Request request = getRequestById(value);
            preparedStatement.setLong(1, request.getProduct());
            preparedStatement.setInt(2, request.getRequestedQuantity());
            preparedStatement.setString(3, request.getRequestDate());
            preparedStatement.setString(4, request.getStatus());
            preparedStatement.setString(5, request.getRequester());
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
    public Integer get(Integer id) {
        return id;
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