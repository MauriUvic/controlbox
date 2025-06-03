package cat.uvic.teknos.dam.controlbox.jdbc.repositories;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductRepository;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.jupiter.LoadDatabaseExtension;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcProduct;
import cat.uvic.teknos.dam.controlbox.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(LoadDatabaseExtension.class)
class JdbcProductRepositoryTest {
    private JdbcProductRepository repository;
    private Connection connection;

    @BeforeEach
    @ExtendWith(LoadDatabaseExtension.class)
    void setUp() throws SQLException {
        var dataSource = new SingleConnectionDataSource();
        repository = new JdbcProductRepository(dataSource);
    }

    @Test
    void testSaveAndGetproduct() {
        // Arrange
        Product product = new JdbcProduct();
        product.setId(1L);
        product.setName("Product A");
        product.setDescription("Description A");
        product.setUnitPrice(10.0);
        product.setStock(100);

        // Act
        repository.save(product);
        Product retrieved = repository.getProductByName("Product A");

        // Assert
        assertNotNull(retrieved);

    }

    @Test
    void testDeleteproduct() {
        // Arrange
        //repository.save(1);

        // Act
        repository.delete(1);
        Product retrieved = repository.get(1);

        // Assert
        assertNull(retrieved);
    }



}