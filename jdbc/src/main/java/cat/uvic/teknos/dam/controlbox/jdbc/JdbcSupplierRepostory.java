package cat.uvic.teknos.dam.controlbox.jdbc;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.modal.JdbcSupplier;
import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.model.Supplier;
import cat.uvic.teknos.dam.controlbox.repositories.SupplierRepostory;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcSupplierRepostory implements SupplierRepostory {
    private final DataSource dataSource;

    public JdbcSupplierRepostory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Supplier getSupplierById(Integer id) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "SELECT * FROM SUPPLIER WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var supplier = new JdbcSupplier();
                supplier.setId(resultSet.getLong("ID"));
                supplier.setCompanyName(resultSet.getString("COMPANY_NAME"));
                supplier.setContactName(resultSet.getString("CONTACT_NAME"));
                supplier.setEmail(resultSet.getString("EMAIL"));
                supplier.setPhone(resultSet.getString("PHONE"));
                supplier.setAddress(resultSet.getString("ADDRESS"));
                return supplier;
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
                "INSERT INTO SUPPLIER (COMPANY_NAME, CONTACT_NAME, EMAIL, PHONE, ADDRESS) VALUES (?, ?, ?, ?, ?)")) {
            Supplier supplier = getSupplierById(value);
            preparedStatement.setString(1, supplier.getCompanyName());
            preparedStatement.setString(2, supplier.getContactName());
            preparedStatement.setString(3, supplier.getEmail());
            preparedStatement.setString(4, supplier.getPhone());
            preparedStatement.setString(5, supplier.getAddress());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer value) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "DELETE FROM SUPPLIER WHERE ID = ?")) {
            preparedStatement.setInt(1, value);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Set<Integer> getAll() {
        var connection = dataSource.getConnection();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT ID FROM SUPPLIER");
            Set<Integer> suppliers = new HashSet<>();
            while (resultSet.next()) {
                suppliers.add(resultSet.getInt("ID"));
            }
            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SupplierRepostory getSupplierById(int id) {
        return null;
    }


}