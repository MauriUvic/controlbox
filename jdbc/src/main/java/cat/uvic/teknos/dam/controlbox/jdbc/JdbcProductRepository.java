package cat.uvic.teknos.dam.controlbox.jdbc;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.modal.JdbcProduct;
import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.repositories.ProductRepostory;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcProductRepository implements ProductRepostory {
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
                product.setUnitPrice(resultSet.getDouble("UNIT_PRICE"));
                product.setCategory(resultSet.getString("CATEGORY"));
                product.setBarcode(resultSet.getString("BARCODE"));
                product.setCreatedAt(resultSet.getString("CREATED_AT"));
                return product;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Product value) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "INSERT INTO PRODUCT (NAME, DESCRIPTION, UNIT_PRICE, CATEGORY, BARCODE, CREATED_AT) VALUES (?, ?, ?, ?, ?, ?)")) {
            Product product = getProductByName(value.toString());
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getUnitPrice());
            preparedStatement.setString(4, product.getCategory());
            preparedStatement.setString(5, product.getBarcode());
            preparedStatement.setString(6, product.getCreatedAt());
            preparedStatement.executeUpdate();
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
    public Integer get(cat.uvic.teknos.dam.controlbox.model.impl.Product id) {
        return 0;
    }


    public Integer get(Product product) {
        return Math.toIntExact(product.getId());
    }

    @Override
    public Set<Integer> getAll() {
        var connection = dataSource.getConnection();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT ID FROM PRODUCT");
            Set<Integer> products = new HashSet<>();
            while (resultSet.next()) {
                products.add(resultSet.getInt("ID"));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}