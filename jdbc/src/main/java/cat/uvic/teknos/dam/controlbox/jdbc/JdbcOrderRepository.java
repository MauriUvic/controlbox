package cat.uvic.teknos.dam.controlbox.jdbc;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcOrder;
import cat.uvic.teknos.dam.controlbox.model.Order;
import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.repositories.OrderRepository;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcOrderRepository implements OrderRepository {
    private final DataSource dataSource;

    public JdbcOrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Order getOrderById(Integer id) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "SELECT * FROM `ORDER` WHERE ID = ?")) {
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
    public void save(Order order) {
        var connection = dataSource.getConnection();
        if (order.getId() == null) {
            try (var preparedStatement = connection.prepareStatement(
                    "INSERT INTO `ORDER`(DATE, TOTAL_AMOUNT, STATUS, DELIVERY_DATE) VALUES (?, ?, ?, ?)")) {
                preparedStatement.setString(1, order.getDate());
                preparedStatement.setDouble(2, order.getTotalAmount());
                preparedStatement.setString(3, order.getStatus());
                preparedStatement.setString(4, order.getDeliveryDate());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try (var preparedStatement = connection.prepareStatement(
                    "UPDATE `ORDER` SET DATE = ?, TOTAL_AMOUNT = ?, STATUS = ?, DELIVERY_DATE = ? WHERE ID = ?")) {
                preparedStatement.setString(1, order.getDate());
                preparedStatement.setDouble(2, order.getTotalAmount());
                preparedStatement.setString(3, order.getStatus());
                preparedStatement.setString(4, order.getDeliveryDate());
                preparedStatement.setLong(5, order.getId());
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
                "DELETE FROM `ORDER` WHERE ID = ?")) {
            preparedStatement.setInt(1, value);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order get(Integer id) {
        return null;
    }



    @Override
    public Set<Order> getAll() {
        var connection = dataSource.getConnection();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT ID FROM `ORDER`");
            Set<Integer> orders = new HashSet<>();
            while (resultSet.next()) {
                orders.add(resultSet.getInt("ID"));
            }
            // TODO
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public cat.uvic.teknos.dam.controlbox.model.impl.Order getByProduct(Product id) {
        return null;
    }
}
