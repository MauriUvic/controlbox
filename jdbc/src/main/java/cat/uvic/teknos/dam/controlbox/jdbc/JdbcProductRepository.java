package cat.uvic.teknos.dam.controlbox.jdbc;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcMovement;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcProduct;
import cat.uvic.teknos.dam.controlbox.model.Movement;
import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.repositories.ProductRepository;

public class JdbcProductRepository implements ProductRepository {
    private final DataSource dataSource;

    public JdbcProductRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Product getProductByName(String name) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "SELECT * FROM PRODUCT WHERE NAME = ?")) {
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var product = new JdbcProduct();
                product.setId(resultSet.getLong("ID"));
                product.setName(resultSet.getString("NAME"));
                product.setDescription(resultSet.getString("DESCRIPTION"));
                product.setUnitPrice(resultSet.getDouble("PRICE"));
                product.setStock(resultSet.getDouble("STOCK"));

                return product;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Product product) {
        try (var connection = dataSource.getConnection()) {
            if(product.getId() == null) {
                try (var preparedStatement = connection.prepareStatement(
                        "INSERT INTO PRODUCT (NAME, DESCRIPTION, PRICE, STOCK) VALUES (?, ?, ?, ?)",
                        java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, product.getName());
                    preparedStatement.setString(2, product.getDescription());
                    preparedStatement.setDouble(3, product.getUnitPrice());
                    preparedStatement.setDouble(4, product.getStock());

                    preparedStatement.executeUpdate();

                    try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            product.setId(generatedKeys.getLong(1));
                        }
                    }
                }
            } else {
                try (var preparedStatement = connection.prepareStatement(
                        "UPDATE PRODUCT SET NAME = ?, DESCRIPTION = ?, PRICE = ?, STOCK = ? WHERE ID = ?")) {
                    preparedStatement.setString(1, product.getName());
                    preparedStatement.setString(2, product.getDescription());
                    preparedStatement.setDouble(3, product.getUnitPrice());
                    preparedStatement.setDouble(4, product.getStock());
                    preparedStatement.setLong(5, product.getId());

                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product get(Integer id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement("SELECT * FROM PRODUCT WHERE ID = ?")) {
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var product = new JdbcProduct();
                product.setId(resultSet.getLong("ID"));
                product.setName(resultSet.getString("NAME"));
                product.setDescription(resultSet.getString("DESCRIPTION"));
                product.setUnitPrice(resultSet.getDouble("PRICE"));
                product.setStock(resultSet.getDouble("STOCK"));
                return product;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer value) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "DELETE FROM PRODUCT WHERE ID = ?")) {
            preparedStatement.setInt(1, value);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<Product> getAll() {
        var connection = dataSource.getConnection();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT ID FROM PRODUCT");
            Set<Integer> products = new HashSet<>();
            while (resultSet.next()) {
                products.add(resultSet.getInt("ID"));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}