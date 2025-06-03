package cat.uvic.teknos.dam.controlbox.jdbc.repositories;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcMovementRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.jupiter.LoadDatabaseExtension;
import cat.uvic.teknos.dam.controlbox.model.Movement;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcMovement;
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
class JdbcMovementRepositoryTest {
    private JdbcMovementRepository repository;
    private Connection connection;

    @BeforeEach
    @ExtendWith(LoadDatabaseExtension.class)
    void setUp() throws SQLException {
        var dataSource = new SingleConnectionDataSource();
        repository = new JdbcMovementRepository(dataSource);
    }

    @Test
    void testSaveAndGetMovement() {
        // Arrange
        Movement movement = new JdbcMovement();
        movement.setType("IN");
        movement.setQuantity(100);
        movement.setDate(LocalDateTime.now().toString());
        movement.setReference("Order 1");

        // Act
        repository.save(movement);
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
        //repository.save(1);

        // Act
        repository.delete(1);
        Movement retrieved = repository.getMovementById(1);

        // Assert
        assertNull(retrieved);
    }



}