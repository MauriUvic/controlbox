package cat.uvic.teknos.dam.controlbox.main;
import cat.uvic.teknos.dam.controlbox.repositories.ProductRepository;


import java.util.Scanner;

public class controlbox {
    private final ProductRepository productRepository;
    private final Scanner scanner;

    public controlbox(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.scanner = new Scanner(System.in);
    }

    public void showProductMenu() {
        while (true) {
            System.out.println("\n=== Product Management ===");
            System.out.println("1. List all products");
            System.out.println("2. Find product by ID");
            System.out.println("3. Add new product");
            System.out.println("4. Update product");
            System.out.println("5. Delete product");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    listProducts();
                    break;
                case 2:
                    findProductById();
                    break;
                case 3:
                    addProduct();
                    break;
                case 4:
                    updateProduct();
                    break;
                case 5:
                    deleteProduct();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void listProducts() {
        System.out.println("\n=== All Products ===");
        productRepository.findAll().forEach(product ->
                System.out.println("ID: " + product.getId() +
                        " | Name: " + product.getName() +
                        " | Stock: " + product.getStock())
        );
    }

    private void findProductById() {
        System.out.print("Enter product ID: ");
        Long id = scanner.nextLong();
        productRepository.findById(id).ifPresent(product ->
                System.out.println("Found: ID: " + product.getId() +
                        " | Name: " + product.getName() +
                        " | Stock: " + product.getStock())
        );
    }

    private void addProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter stock quantity: ");
        int stock = scanner.nextInt();

        Product product = new Product();
        product.setName(name);
        product.setStock(stock);

        productRepository.save(product);
        System.out.println("Product added successfully!");
    }

    private void updateProduct() {
        System.out.print("Enter product ID to update: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Clear buffer

        productRepository.findById(id).ifPresent(product -> {
            System.out.print("Enter new name (current: " + product.getName() + "): ");
            String name = scanner.nextLine();
            System.out.print("Enter new stock (current: " + product.getStock() + "): ");
            int stock = scanner.nextInt();

            product.setName(name);
            product.setStock(stock);
            productRepository.save(product);
            System.out.println("Product updated successfully!");
        });
    }

    private void deleteProduct() {
        System.out.print("Enter product ID to delete: ");
        Long id = scanner.nextLong();
        productRepository.deleteById(id);
        System.out.println("Product deleted successfully!");
    }
}