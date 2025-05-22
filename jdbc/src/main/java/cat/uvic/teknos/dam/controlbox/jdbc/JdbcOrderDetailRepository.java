package cat.uvic.teknos.dam.controlbox.jdbc;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.modal.JdbcOrderDetail;
import cat.uvic.teknos.dam.controlbox.model.OrderDetail;


import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcOrderDetailRepository implements OrderRepository {
    private final DataSource dataSource;

    public JdbcOrderDetailRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public OrderDetail getOrderDetailById(Integer id) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "SELECT * FROM ORDER_DETAIL WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var orderDetail = new JdbcOrderDetail();
                orderDetail.setId(resultSet.getLong("ID"));
                orderDetail.setQuantity(resultSet.getInt("QUANTITY"));
                orderDetail.setUnitPrice(resultSet.getDouble("UNIT_PRICE"));
                orderDetail.setSubtotal(resultSet.getDouble("SUBTOTAL"));
                return orderDetail;
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
                "INSERT INTO ORDER_DETAIL (QUANTITY, UNIT_PRICE, SUBTOTAL) VALUES (?, ?, ?)")) {
            OrderDetail orderDetail = getOrderDetailById(value);
            preparedStatement.setInt(1, orderDetail.getQuantity());
            preparedStatement.setDouble(2, orderDetail.getUnitPrice());
            preparedStatement.setDouble(3, orderDetail.getSubtotal());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer value) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "DELETE FROM ORDER_DETAIL WHERE ID = ?")) {
            preparedStatement.setInt(1, value);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer get(OrderDetail id) {
        return Math.toIntExact(id.getId());
    }

    @Override
    public Set<Integer> getAll() {
        var connection = dataSource.getConnection();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT ID FROM ORDER_DETAIL");
            Set<Integer> orderDetails = new HashSet<>();
            while (resultSet.next()) {
                orderDetails.add(resultSet.getInt("ID"));
            }
            return orderDetails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}