package cat.uvic.teknos.dam.controlbox.main;

import cat.uvic.teknos.dam.controlbox.model.ModelFactory;
import cat.uvic.teknos.dam.controlbox.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.repositories.ProductRepostory;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class ProductManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    public ProductManager(ModelFactory modelFactory, RepositoryFactory repositoryFactory, Scanner scanner) {
        this.modelFactory = modelFactory;
        this.repositoryFactory = repositoryFactory;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("Product Manager - Available commands:");
        System.out.println("1 - List all products");
        System.out.println("2 - Show product details");
        System.out.println("3 - Add new product");
        System.out.println("exit - Close the manager");

        var repository = repositoryFactory.getProductRepository();
        String command;

        while (!Objects.equals(command = scanner.nextLine(), "exit")) {
            switch (command) {
                case "1":
                    var products = repository.getAll();
                    System.out.println(AsciiTable.getTable(products, Arrays.asList(
                            new Column<Product>("ID", p -> String.valueOf(p.getId())),
                            new Column<Product>("Name", p -> p.getName()),
                            new Column<Product>("Quantity", p -> String.valueOf(p.getStock().size()))
                    )));
                    break;

                case "2":
                    System.out.println("Enter product ID:");
                    try {
                        Integer id = Integer.parseInt(scanner.nextLine());
                        var product = repository.get(id);
                        if (product != null) {
                            System.out.println(AsciiTable.getTable(Arrays.asList(product), Arrays.asList(
                                    new Column<Product>("ID", p -> String.valueOf(p.getId())),
                                    new Column<Product>("Name", p -> p.getName()),
                                    new Column<Product>("Quantity", p -> String.valueOf(p.getStock().size())),
                                    new Column<Product>("Description", p -> p.getDescription())
                            )));
                        } else {
                            System.out.println("Product not found");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format");
                    }
                    break;

                case "3":
                    var product = modelFactory.newProduct();

                    System.out.println("Enter product name:");
                    product.setName(scanner.nextLine());

                    System.out.println("Enter product description:");
                    product.setDescription(scanner.nextLine());

                    System.out.println("Enter initial quantity:");
                    try {
                        product.setStock(Set.of(modelFactory.newStock()));
                        repository.save(product);
                        System.out.println("Product saved successfully");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity format");
                    }
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }

            System.out.println("\nEnter command (1-3) or 'exit':");
        }
    }
}