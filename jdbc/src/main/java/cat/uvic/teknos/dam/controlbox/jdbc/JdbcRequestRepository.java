package cat.uvic.teknos.dam.controlbox.jdbc;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcRequest;
import cat.uvic.teknos.dam.controlbox.model.Request;

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
                jdbcRequest.setQuantity(resultSet.getInt("QUANTITY"));
                jdbcRequest.setDate(resultSet.getString("REQUEST_DATE"));
                jdbcRequest.setStatus(resultSet.getString("STATUS"));
                jdbcRequest.setRequester(resultSet.getString("REQUESTER"));

                return jdbcRequest;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Request request) {
        var connection = dataSource.getConnection();
        if (request.getId() == null) {
            try (var preparedStatement = connection.prepareStatement(
                    "INSERT INTO REQUEST (PRODUCT_ID, QUANTITY, REQUEST_DATE, STATUS, REQUESTER) VALUES (?, ?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, request.getProduct());
                preparedStatement.setInt(2, request.getQuantity());
                preparedStatement.setString(3, request.getDate());
                preparedStatement.setString(4, request.getStatus());
                preparedStatement.setString(5, request.getRequester());
                preparedStatement.executeUpdate();
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        request.setId(generatedKeys.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (var preparedStatement = connection.prepareStatement(
                    "UPDATE REQUEST SET PRODUCT_ID = ?, QUANTITY = ?, REQUEST_DATE = ?, STATUS = ?, REQUESTER = ? WHERE ID = ?")) {
                preparedStatement.setLong(1, request.getProduct());
                preparedStatement.setInt(2, request.getQuantity());
                preparedStatement.setString(3, request.getDate());
                preparedStatement.setString(4, request.getStatus());
                preparedStatement.setString(5, request.getRequester());
                preparedStatement.setLong(6, request.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
    public Request get(Integer id) {
        // TODO: Implement this method if needed
        return null;
    }

    @Override
    public Set<Request> getAll() {
        var connection = dataSource.getConnection();
        Set<Request> requests = new HashSet<>();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT * FROM REQUEST");
            while (resultSet.next()) {
                var jdbcRequest = new JdbcRequest();
                jdbcRequest.setId(resultSet.getLong("ID"));
                jdbcRequest.setProduct(resultSet.getLong("PRODUCT_ID"));
                jdbcRequest.setQuantity(resultSet.getInt("QUANTITY"));
                jdbcRequest.setDate(resultSet.getString("REQUEST_DATE"));
                jdbcRequest.setStatus(resultSet.getString("STATUS"));
                jdbcRequest.setRequester(resultSet.getString("REQUESTER"));
                requests.add(jdbcRequest);
            }
            return requests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

