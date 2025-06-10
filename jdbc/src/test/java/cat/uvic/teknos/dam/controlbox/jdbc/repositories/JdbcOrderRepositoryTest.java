package cat.uvic.teknos.dam.controlbox.jdbc.repositories;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcOrderRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.jupiter.LoadDatabaseExtension;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcOrder;
import cat.uvic.teknos.dam.controlbox.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoadDatabaseExtension.class)
class JdbcOrderRepositoryTest {
    private JdbcOrderRepository repository;

    @BeforeEach
    @ExtendWith(LoadDatabaseExtension.class)
    void setUp() {
        var dataSource = new SingleConnectionDataSource();
        repository = new JdbcOrderRepository(dataSource);
    }

    @Test
    void testSaveAndGetOrder() {
        // Arrange
        Order order = new JdbcOrder();
        order.setDate("2023-01-02");
        order.setTotalAmount(500.0);
        order.setStatus("Pending");
        order.setDeliveryDate("2023-01-06");

        // Act
        repository.save(order);
        Order retrieved = repository.getOrderById(2);

        // Assert
        assertNotNull(retrieved);
        assertEquals(order.getDate(), retrieved.getDate());
        assertEquals(order.getTotalAmount(), retrieved.getTotalAmount());
        assertEquals(order.getStatus(), retrieved.getStatus());
        assertEquals(order.getDeliveryDate(), retrieved.getDeliveryDate());
    }

    @Test
    void testDeleteOrder() {
        // Arrange
        //repository.save(order); // Asume que hay un order con ID 1

        // Act
        repository.delete(1);
        Order retrieved = repository.getOrderById(1);

        // Assert
        assertNull(retrieved);
    }
}
