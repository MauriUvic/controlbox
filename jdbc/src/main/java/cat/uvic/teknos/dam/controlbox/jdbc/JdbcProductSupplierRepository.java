package cat.uvic.teknos.dam.controlbox.jdbc;

import cat.uvic.teknos.dam.controlbox.model.ProductSupplier;
import cat.uvic.teknos.dam.controlbox.repositories.ProductSupplierRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcProductSupplier;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcProductSupplierRepository implements ProductSupplierRepository {
    private final Connection connection;

    public JdbcProductSupplierRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(ProductSupplier productSupplier) {
        try {
            if (productSupplier.getId() == null) {
                String sql = "INSERT INTO PRODUCT_SUPPLIER (PRODUCT_ID, SUPPLIER_ID) VALUES (?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setLong(1, productSupplier.getProduct().getId());
                    stmt.setLong(2, productSupplier.getSupplier().getId());
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            productSupplier.setId(rs.getLong(1));
                        }
                    }
                }
            } else {
                String sql = "UPDATE PRODUCT_SUPPLIER SET PRODUCT_ID = ?, SUPPLIER_ID = ? WHERE ID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setLong(1, productSupplier.getProduct().getId());
                    stmt.setLong(2, productSupplier.getSupplier().getId());
                    stmt.setLong(3, productSupplier.getId());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            String sql = "DELETE FROM PRODUCT_SUPPLIER WHERE ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductSupplier get(Integer id) {
        try {
            String sql = "SELECT * FROM PRODUCT_SUPPLIER WHERE ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        JdbcProductSupplier ps = new JdbcProductSupplier();
                        ps.setId(rs.getLong("ID"));
                        // Set Product and Supplier as needed
                        return ps;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ProductSupplier getProductSupplierById(Long id) {
        return null;
    }

    @Override
    public void delete(ProductSupplier productSupplier) {
        if (productSupplier == null || productSupplier.getId() == null) {
            throw new IllegalArgumentException("ProductSupplier or its ID must not be null");
        }
        try {
            String sql = "DELETE FROM PRODUCT_SUPPLIER WHERE ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, productSupplier.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<ProductSupplier> getAll() {
        Set<ProductSupplier> result = new HashSet<>();
        String sql = "SELECT * FROM PRODUCT_SUPPLIER";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                JdbcProductSupplier ps = new JdbcProductSupplier();
                ps.setId(rs.getLong("ID"));
                // Set Product and Supplier as needed
                result.add(ps);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}