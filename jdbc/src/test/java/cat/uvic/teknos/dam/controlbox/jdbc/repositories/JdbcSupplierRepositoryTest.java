package cat.uvic.teknos.dam.controlbox.jdbc.repositories;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcSupplierRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.jupiter.LoadDatabaseExtension;
import cat.uvic.teknos.dam.controlbox.model.Supplier;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcSupplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoadDatabaseExtension.class)
class JdbcSupplierRepositoryTest {
    private JdbcSupplierRepository repository;

    @BeforeEach
    void setUp() {
        var dataSource = new SingleConnectionDataSource();
        repository = new JdbcSupplierRepository(dataSource);
    }

    @Test
    void testSaveAndGetSupplier() {
        // Arrange
        Supplier supplier = new JdbcSupplier();
        supplier.setName("Supplier B");
        supplier.setContactName("Contact B");
        supplier.setEmail("contactB@example.com");
        supplier.setPhone("0987654321");
        supplier.setAddress("Address B");

        // Act
        repository.save(supplier);
        Supplier retrieved = repository.get(2);

        // Assert
        assertNotNull(retrieved);
        assertEquals(supplier.getName(), retrieved.getName());
        assertEquals(supplier.getContactName(), retrieved.getContactName());
        assertEquals(supplier.getEmail(), retrieved.getEmail());
        assertEquals(supplier.getPhone(), retrieved.getPhone());
        assertEquals(supplier.getAddress(), retrieved.getAddress());
    }

    @Test
    void testDeleteSupplier() {
        // Arrange
        //repository.save(supplier); // Si es necesario, crea un supplier antes

        // Act
        repository.delete(1);
        Supplier retrieved = repository.get(1);

        // Assert
        assertNull(retrieved);
    }
}

