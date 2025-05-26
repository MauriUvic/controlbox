package cat.uvic.teknos.dam.controlbox.jdbc.datasources;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcSupplierRepostory;
import cat.uvic.teknos.dam.controlbox.jdbc.modal.JdbcSupplier;
import cat.uvic.teknos.dam.controlbox.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSupplierRepositoryTest {
    private JdbcSupplierRepostory repository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {

        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        var dataSource = new DataSource() {
            @Override
            public Connection getConnection() {
                return connection;
            }
        };
        repository = new JdbcSupplierRepostory(dataSource);


        try (var statement = connection.createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS SUPPLIER (
                    ID IDENTITY PRIMARY KEY,
                    COMPANY_NAME VARCHAR(255),
                    CONTACT_NAME VARCHAR(255),
                    EMAIL VARCHAR(255),
                    PHONE VARCHAR(255),
                    ADDRESS VARCHAR(255)
                )
            """);
        }
    }

    @Test
    void testSaveAndGetSupplier() {

        Supplier supplier = new JdbcSupplier();
        supplier.setCompanyName("Test Company");
        supplier.setContactName("John Doe");
        supplier.setEmail("john@example.com");
        supplier.setPhone("123456789");
        supplier.setAddress("Test Address");



        Supplier retrieved = (Supplier) repository.getSupplierById(1);


        assertNotNull(retrieved);
        assertEquals(supplier.getCompanyName(), retrieved.getCompanyName());
        assertEquals(supplier.getContactName(), retrieved.getContactName());
        assertEquals(supplier.getEmail(), retrieved.getEmail());
        assertEquals(supplier.getPhone(), retrieved.getPhone());
        assertEquals(supplier.getAddress(), retrieved.getAddress());
    }

    @Test
    void testDeleteSupplier() {




        repository.delete(1);
        Supplier retrieved = (Supplier) repository.getSupplierById(1);


        assertNull(retrieved);
    }

    @Test
    void testGetAll() {



        Set<Integer> suppliers = repository.getAll();


        assertEquals(2, suppliers.size());
        assertTrue(suppliers.contains(1));
        assertTrue(suppliers.contains(2));
    }
}