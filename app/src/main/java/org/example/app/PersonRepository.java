package org.example.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {
    private static final String URL = "jdbc:mysql://localhost:3306/m0864";
    private static final String USER = "root";
    private static final String PASSWORD = "rootpassword";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public void save(Person person) {
        String sql;
        if (person.getId() != 0) {
            sql = "INSERT INTO PERSON (ID, FIRST_NAME, LAST_NAME) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE FIRST_NAME = ?, LAST_NAME = ?";
        } else {
            sql = "UPDATE PERSON SET FIRST_NAME = ?, LAST_NAME = ? WHERE ID = ?";
        }

        try (Connection conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {

            if (person.getId() != 0) {
                stmt.setInt(1, person.getId());
                stmt.setString(2, person.getFirstName());
                stmt.setString(3, person.getLastName());
                stmt.setString(4, person.getFirstName());
                stmt.setString(5, person.getLastName());
            } else {
                stmt.setString(1, person.getFirstName());
                stmt.setString(2, person.getLastName());
                stmt.setInt(3, person.getId());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving person", e);
        }
    }


    public void delete(Person person) {
        String sql = "DELETE FROM PERSON WHERE ID = ?";

        try (Connection conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, person.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting person", e);
        }
    }

    public Person get(int id) {
        String sql = "SELECT * FROM PERSON WHERE ID = ?";

        try (Connection conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                Person person = new Person();
                person.setId(rs.getInt("ID"));
                person.setFirstName(rs.getString("FIRST_NAME"));
                person.setLastName(rs.getString("LAST_NAME"));
                return person;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving person", e);
        }
    }

    public List<Person> getAll() {
        String sql = "SELECT * FROM PERSON";
        List<Person> persons = new ArrayList<>();

        try (Connection conn = getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Person person = new Person();
                person.setId(rs.getInt("ID"));
                person.setFirstName(rs.getString("FIRST_NAME"));
                person.setLastName(rs.getString("LAST_NAME"));
                persons.add(person);
            }
            return persons;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all persons", e);
        }
    }
}