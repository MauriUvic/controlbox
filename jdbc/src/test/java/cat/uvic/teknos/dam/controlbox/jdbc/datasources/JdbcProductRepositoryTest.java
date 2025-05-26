package cat.uvic.teknos.dam.controlbox.jdbc.datasources;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductRepository;
import cat.uvic.teknos.dam.controlbox.model.impl.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcProductRepositoryTest {
    private JdbcProductRepository repository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // Setup H2 in-memory database
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        var dataSource = new DataSource() {
            @Override
            public Connection getConnection() {
                return connection;
            }
        };
        repository = new JdbcProductRepository(dataSource);

        // Create test table
        try (var statement = connection.createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS PRODUCT (
                    ID IDENTITY PRIMARY KEY,
                    NAME VARCHAR(255),
                    DESCRIPTION VARCHAR(255),
                    UNIT_PRICE DOUBLE,
                    CATEGORY VARCHAR(255),
                    BARCODE VARCHAR(255),
                    CREATED_AT VARCHAR(255)
                )
            """);
        }
    }

    @Test
    void testSaveAndGetProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setUnitPrice(9.99);
        product.setCategory("Test Category");
        product.setBarcode("123456789");
        product.setCreatedAt(java.time.LocalDateTime.now().toString());

        // Act
        repository.save(1);
        Product retrieved = (Product) repository.getProductByName("Test Product");

        // Assert
        assertNotNull(retrieved);
        assertEquals(product.getName(), retrieved.getName());
        assertEquals(product.getDescription(), retrieved.getDescription());
        assertEquals(product.getUnitPrice(), retrieved.getUnitPrice());
        assertEquals(product.getCategory(), retrieved.getCategory());
        assertEquals(product.getBarcode(), retrieved.getBarcode());
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        repository.save(1);

        // Act
        repository.delete(1);
        Product retrieved = (Product) repository.getProductByName("Test Product");

        // Assert
        assertNull(retrieved);
    }

    @Test
    void testGetAll() {
        // Arrange
        repository.save(1);
        repository.save(2);

        // Act
        Set<Integer> products = repository.getAll();

        // Assert
        assertEquals(2, products.size());
        assertTrue(products.contains(1));
        assertTrue(products.contains(2));
    }
}