package cat.uvic.teknos.dam.controlbox.jdbc.repositories;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.model.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import cat.uvic.teknos.dam.controlbox.jdbc.JdbcRequestRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcRequestRepositoryTest {
    private JdbcRequestRepository repository;
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
        repository = new JdbcRequestRepository(dataSource);

        // Create test table
        try (var statement = connection.createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS REQUEST (
                    ID IDENTITY PRIMARY KEY,
                    PRODUCT_ID BIGINT,
                    REQUESTED_QUANTITY INT,
                    REQUEST_DATE VARCHAR(255),
                    STATUS VARCHAR(255),
                    REQUESTER VARCHAR(255)
                )
            """);
        }
    }

    @Test
    void testSaveAndGetRequest() {
        // Arrange
        Request request = new cat.uvic.teknos.dam.controlbox.model.impl.Request();
        request.setProduct(1L);
        request.setQuantity(0);
        request.setDate(LocalDate.now().toString());
        request.setStatus("");
        request.setRequester("");

        // Act
        repository.save(request);
        var retrieved = repository.getRequestById(1);

        // Assert
        assertNotNull(retrieved);
        assertEquals(request.getProduct(), retrieved.getProduct());
        assertEquals(request.getQuantity(), retrieved.getQuantity());
        assertEquals(request.getStatus(), retrieved.getStatus());
        assertEquals(request.getRequester(), retrieved.getRequester());
    }

    @Test
    void testDeleteRequest() {
        // Arrange
        //repository.save(1);

        // Act
        repository.delete(1);
        var retrieved = repository.getRequestById(1);

        // Assert
        assertNull(retrieved);
    }

}

