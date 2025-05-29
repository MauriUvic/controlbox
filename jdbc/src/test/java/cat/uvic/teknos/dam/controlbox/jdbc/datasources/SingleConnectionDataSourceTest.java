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
        var dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource.getServer());
    }

    @Test
    void getDatabase() {
        var dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource.getDatabase());
    }

    @Test
    void getUser() {
        var dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource.getUser());
    }

    @Test
    void getPassword() {
        var dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource.getPassword());
    }

    @Test
    void getConnection() {
        var dataSource = new SingleConnectionDataSource();
        var connection = dataSource.getConnection();
        assertNotNull(connection);
    }
}