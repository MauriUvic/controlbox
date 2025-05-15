package cat.uvic.teknos.dam.controlbox.jdbc.datasources;

import java.sql.Connection;

public interface DataSource {
    Connection getConnection();
}
