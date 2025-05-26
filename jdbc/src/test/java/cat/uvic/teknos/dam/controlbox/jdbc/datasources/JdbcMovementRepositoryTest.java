package cat.uvic.teknos.dam.controlbox.jdbc.datasources;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcMovementRepository;
import cat.uvic.teknos.dam.controlbox.model.Movement;
import cat.uvic.teknos.dam.controlbox.jdbc.modal.JdbcMovement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcMovementRepositoryTest {
    private JdbcMovementRepository repository;
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
        repository = new JdbcMovementRepository(dataSource);

        // Create test table
        try (var statement = connection.createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS MOVEMENT (
                    ID IDENTITY PRIMARY KEY,
                    TYPE VARCHAR(255),
                    QUANTITY INT,
                    DATE VARCHAR(255),
                    REFERENCE VARCHAR(255)
                )
            """);
        }
    }

    @Test
    void testSaveAndGetMovement() {
        // Arrange
        Movement movement = new JdbcMovement();
        movement.setType("INPUT");
        movement.setQuantity(10);
        movement.setDate(LocalDateTime.now().toString());
        movement.setReference("REF-001");

        // Act
        repository.save(1);
        Movement retrieved = repository.getMovementById(1);

        // Assert
        assertNotNull(retrieved);
        assertEquals(movement.getType(), retrieved.getType());
        assertEquals(movement.getQuantity(), retrieved.getQuantity());
        assertEquals(movement.getReference(), retrieved.getReference());
    }

    @Test
    void testDeleteMovement() {
        // Arrange
        repository.save(1);

        // Act
        repository.delete(1);
        Movement retrieved = repository.getMovementById(1);

        // Assert
        assertNull(retrieved);
    }

    @Test
    void testGetAll() {
        // Arrange
        repository.save(1);
        repository.save(2);

        // Act
        Set<Integer> movements = repository.getAll();

        // Assert
        assertEquals(2, movements.size());
        assertTrue(movements.contains(1));
        assertTrue(movements.contains(2));
    }
}