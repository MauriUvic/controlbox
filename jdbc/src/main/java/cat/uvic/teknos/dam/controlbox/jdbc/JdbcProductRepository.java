package cat.uvic.teknos.dam.controlbox.jdbc;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cat.uvic.teknos.dam.controlbox.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcProduct;
import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.repositories.ProductRepository;

/**
 * Implementació del repositori de productes utilitzant JDBC.
 * Gestiona les operacions CRUD (Crear, Llegir, Actualitzar, Esborrar) per a l'entitat Product a la base de dades.
 */
public class JdbcProductRepository implements ProductRepository {
    private final DataSource dataSource;

    /**
     * Constructor que injecta una font de dades (DataSource).
     * El DataSource proporciona les connexions a la base de dades.
     */
    public JdbcProductRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Obté un producte de la base de dades a partir del seu nom.
     * Retorna el producte si el troba, o null si no existeix.
     */
    @Override
    public Product getProductByName(String name) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(
                "SELECT * FROM PRODUCT WHERE NAME = ?")) {
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var product = new JdbcProduct();
                product.setId(resultSet.getLong("ID"));
                product.setName(resultSet.getString("NAME"));
                product.setDescription(resultSet.getString("DESCRIPTION"));
                product.setUnitPrice(resultSet.getDouble("PRICE"));
                product.setStock(resultSet.getDouble("STOCK"));
                return product;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Desa un producte a la base de dades.
     * Si el producte no té ID, realitza una inserció (INSERT). Si té ID, realitza una actualització (UPDATE).
     */
    @Override
    public void save(Product product) {
        try (var connection = dataSource.getConnection()) {
            if(product.getId() == null) {
                // Inserció d'un nou producte i obtenció de l'ID generat.
                try (var preparedStatement = connection.prepareStatement(
                        "INSERT INTO PRODUCT (NAME, DESCRIPTION, PRICE, STOCK) VALUES (?, ?, ?, ?)",
                        java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, product.getName());
                    preparedStatement.setString(2, product.getDescription());
                    preparedStatement.setDouble(3, product.getUnitPrice());
                    preparedStatement.setDouble(4, product.getStock());

                    preparedStatement.executeUpdate();

                    try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            product.setId(generatedKeys.getLong(1));
                        }
                    }
                }
            } else {
                // Actualització d'un producte existent.
                try (var preparedStatement = connection.prepareStatement(
                        "UPDATE PRODUCT SET NAME = ?, DESCRIPTION = ?, PRICE = ?, STOCK = ? WHERE ID = ?")) {
                    preparedStatement.setString(1, product.getName());
                    preparedStatement.setString(2, product.getDescription());
                    preparedStatement.setDouble(3, product.getUnitPrice());
                    preparedStatement.setDouble(4, product.getStock());
                    preparedStatement.setLong(5, product.getId());

                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obté un producte de la base de dades a partir del seu ID.
     * Retorna el producte si el troba, o null si no existeix.
     */
    @Override
    public Product get(Integer id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement("SELECT * FROM PRODUCT WHERE ID = ?")) {
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var product = new JdbcProduct();
                product.setId(resultSet.getLong("ID"));
                product.setName(resultSet.getString("NAME"));
                product.setDescription(resultSet.getString("DESCRIPTION"));
                product.setUnitPrice(resultSet.getDouble("PRICE"));
                product.setStock(resultSet.getDouble("STOCK"));
                return product;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Elimina un producte de la base de dades a partir del seu ID.
     */
    @Override
    public void delete(Integer value) {
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(
                "DELETE FROM PRODUCT WHERE ID = ?")) {
            preparedStatement.setInt(1, value);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obté tots els productes de la base de dades.
     * Retorna un conjunt (Set) de tots els productes trobats.
     */
    @Override
    public Set<Product> getAll() {
        Set<Product> products = new HashSet<>();
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery("SELECT * FROM PRODUCT");
            while (resultSet.next()) {
                var product = new JdbcProduct();
                product.setId(resultSet.getLong("ID"));
                product.setName(resultSet.getString("NAME"));
                product.setDescription(resultSet.getString("DESCRIPTION"));
                product.setUnitPrice(resultSet.getDouble("PRICE"));
                product.setStock(resultSet.getDouble("STOCK"));
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
