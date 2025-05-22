package cat.uvic.teknos.dam.controlbox.jdbc;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.modal.JdbcOrder;
import cat.uvic.teknos.dam.controlbox.model.Order;
import cat.uvic.teknos.dam.controlbox.repositories.OrderRepostory;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcOrderRepository implements OrderRepostory {
    private final DataSource dataSource;

    public JdbcOrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Order getOrderById(Integer id) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "SELECT * FROM ORDER_TABLE WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var order = new JdbcOrder();
                order.setId(resultSet.getLong("ID"));
                order.setDate(resultSet.getString("DATE"));
                order.setTotalAmount(resultSet.getDouble("TOTAL_AMOUNT"));
                order.setStatus(resultSet.getString("STATUS"));
                order.setDeliveryDate(resultSet.getString("DELIVERY_DATE"));
                return order;
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
                "INSERT INTO ORDER_TABLE (DATE, TOTAL_AMOUNT, STATUS, DELIVERY_DATE) VALUES (?, ?, ?, ?)")) {
            Order order = getOrderById(value);
            preparedStatement.setString(1, order.getDate());
            preparedStatement.setDouble(2, order.getTotalAmount());
            preparedStatement.setString(3, order.getStatus());
            preparedStatement.setString(4, order.getDeliveryDate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer value) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "DELETE FROM ORDER_TABLE WHERE ID = ?")) {
            preparedStatement.setInt(1, value);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer get(cat.uvic.teknos.dam.controlbox.model.impl.Order id) {
        return 0;
    }



    @Override
    public Set<Integer> getAll() {
        var connection = dataSource.getConnection();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT ID FROM ORDER_TABLE");
            Set<Integer> orders = new HashSet<>();
            while (resultSet.next()) {
                orders.add(resultSet.getInt("ID"));
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public cat.uvic.teknos.dam.controlbox.model.impl.Order getOrderById(int id) {
        return null;
    }
}