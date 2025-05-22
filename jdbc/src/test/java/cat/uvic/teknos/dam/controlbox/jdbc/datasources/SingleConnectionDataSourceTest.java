package cat.uvic.teknos.dam.controlbox.jdbc.datasources;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleConnectionDataSourceTest {

    @Test
    void getDriver() {
        var dataSource = new SingleConnectionDataSource();
        assertEquals("mysql", dataSource.getDriver());
    }

    @Test
    void getServer() {
    }

    @Test
    void getDatabase() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getPassword() {
    }

    @Test
    void getConnection() {
        var dataSource = new SingleConnectionDataSource();
        var connection = dataSource.getConnection();

        assertNotNull(connection);

    }
}