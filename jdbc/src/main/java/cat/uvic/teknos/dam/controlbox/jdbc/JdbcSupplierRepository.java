package cat.uvic.teknos.dam.controlbox.jdbc;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcSupplier;
import cat.uvic.teknos.dam.controlbox.model.Supplier;
import cat.uvic.teknos.dam.controlbox.repositories.SupplierRepository;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class JdbcSupplierRepository implements SupplierRepository {
    private final DataSource dataSource;

    public JdbcSupplierRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Supplier get(Integer id) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "SELECT * FROM SUPPLIER WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var supplier = new JdbcSupplier();
                supplier.setId(resultSet.getLong("ID"));
                supplier.setName(resultSet.getString("NAME"));
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
    public void save(Supplier supplier) {
        var connection = dataSource.getConnection();
        if(supplier.getId() == null) {
            try (var preparedStatement = connection.prepareStatement(
                    "INSERT INTO SUPPLIER (NAME, CONTACT_NAME, EMAIL, PHONE, ADDRESS) VALUES (?, ?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, supplier.getName());
                preparedStatement.setString(2, supplier.getContactName());
                preparedStatement.setString(3, supplier.getEmail());
                preparedStatement.setString(4, supplier.getPhone());
                preparedStatement.setString(5, supplier.getAddress());
                preparedStatement.executeUpdate();

                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        supplier.setId(generatedKeys.getLong(1)); // Set the generated ID
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try (var preparedStatement = connection.prepareStatement(
                    "UPDATE SUPPLIER SET NAME = ?, CONTACT_NAME = ?, EMAIL = ?, PHONE = ?, ADDRESS = ? WHERE ID = ?")) {
                preparedStatement.setString(1, supplier.getName());
                preparedStatement.setString(2, supplier.getContactName());
                preparedStatement.setString(3, supplier.getEmail());
                preparedStatement.setString(4, supplier.getPhone());
                preparedStatement.setString(5, supplier.getAddress());
                preparedStatement.setLong(6, supplier.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


    @Override
    public void delete(Integer id) {
        var connection = dataSource.getConnection();
        try (var preparedStatement = connection.prepareStatement(
                "DELETE FROM SUPPLIER WHERE ID = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Supplier> getAll() {
        var connection = dataSource.getConnection();
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT * FROM SUPPLIER");
            Set<Supplier> suppliers = new HashSet<>();
            while (resultSet.next()) {
                var supplier = new JdbcSupplier();
                supplier.setId(resultSet.getLong("ID"));
                supplier.setName(resultSet.getString("NAME"));
                supplier.setContactName(resultSet.getString("CONTACT_NAME"));
                supplier.setEmail(resultSet.getString("EMAIL"));
                supplier.setPhone(resultSet.getString("PHONE"));
                supplier.setAddress(resultSet.getString("ADDRESS"));
                suppliers.add(supplier);
            }
            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
