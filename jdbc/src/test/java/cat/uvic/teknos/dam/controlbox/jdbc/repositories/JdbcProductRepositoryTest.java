package cat.uvic.teknos.dam.controlbox.jdbc.repositories;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.jupiter.LoadDatabaseExtension;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcProduct;
import cat.uvic.teknos.dam.controlbox.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoadDatabaseExtension.class)
class JdbcProductRepositoryTest {
    private JdbcProductRepository repository;

    @BeforeEach
    void setUp() {
        var dataSource = new SingleConnectionDataSource();
        repository = new JdbcProductRepository(dataSource);
    }

    @Test
    void testSaveAndGetProduct() {
        // Arrange
        Product product = new JdbcProduct();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setUnitPrice(10.99);
        product.setStock(100.0);

        // Act
        repository.save(product);
        Product retrieved = repository.get(2);

        // Assert
        assertNotNull(retrieved);
        assertEquals(product.getName(), retrieved.getName());
        assertEquals(product.getDescription(), retrieved.getDescription());
        assertEquals(product.getUnitPrice(), retrieved.getUnitPrice());
        assertEquals(product.getStock(), retrieved.getStock());
    }

    @Test
    void testDeleteProduct() {
        // Act
        repository.delete(2);
        Product retrieved = repository.get(2);

        // Assert
        assertNull(retrieved);
    }

}