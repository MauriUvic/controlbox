package cat.uvic.teknos.dam.controlbox.clients;

import cat.uvic.teknos.dam.controlbox.clients.exceptions.ClientException;
import cat.uvic.teknos.dam.controlbox.clients.models.ProductImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final RawHttp rawHttp = new RawHttp();
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket(HOST, PORT)) { // Create a single socket for the session

            System.out.println("Connected to server at " + HOST + ":" + PORT);

            String choice;
            while (true) {
                printMainMenu();
                choice = scanner.nextLine();

                if ("exit".equalsIgnoreCase(choice)) {
                    break;
                }

                switch (choice) {
                    case "1":
                        manageProducts(socket, scanner); // Pass the socket to manageProducts
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            throw new ClientException("Error connecting to server or during communication", e);
        } catch (Exception e) {
            System.err.println("An unexpected client error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Client disconnected.");
    }

    private static void printMainMenu() {
        System.out.println("\n--- Client Terminal ---");
        System.out.println("1. Manage Products");
        System.out.println("Type 'exit' to quit.");
        System.out.print("Choose an option: ");
    }

    // Pass the socket to manageProducts
    private static void manageProducts(Socket socket, Scanner scanner) {
        String productChoice;
        while (true) {
            printProductMenu();
            productChoice = scanner.nextLine();

            if ("back".equalsIgnoreCase(productChoice)) {
                break;
            }

            // No try-catch here, let sendRequestAndPrintResponse handle its own errors
            switch (productChoice) {
                case "1": // Get all products
                    sendGetRequest(socket, "/products");
                    break;
                case "2": // Get by id
                    System.out.print("Enter product ID: ");
                    String id = scanner.nextLine();
                    sendGetRequest(socket, "/products/" + id);
                    break;
                case "3": // Create product
                    createProduct(socket, scanner);
                    break;
                case "4": // Update product
                    updateProduct(socket, scanner);
                    break;
                case "5": // Delete product
                    deleteProduct(socket, scanner);
                    break;
                default:
                    System.out.println("Invalid product option.");
            }
        }
    }

    private static void printProductMenu() {
        System.out.println("\n--- Product Management ---");
        System.out.println("1. Get all products");
        System.out.println("2. Get product by ID");
        System.out.println("3. Create new product");
        System.out.println("4. Update product");
        System.out.println("5. Delete product");
        System.out.println("Type 'back' to return to main menu.");
        System.out.print("Choose an option: ");
    }

    private static void sendGetRequest(Socket socket, String path) {
        RawHttpRequest request = rawHttp.parseRequest(String.format(
                "GET %s HTTP/1.1\r\n" +
                        "User-Agent: console\r\n" +
                        "Host: %s:%d\r\n" +
                        "\r\n", path, HOST, PORT));

        sendRequestAndPrintResponse(socket, request, path.equals("/products"));
    }

    private static void createProduct(Socket socket, Scanner scanner) {
        ProductImpl newProduct = getProductDetailsFromUser(scanner, false);
        String productJson = null;
        try {
            productJson = mapper.writeValueAsString(newProduct);
        } catch (IOException e) {
            System.err.println("Error serializing product to JSON: " + e.getMessage());
            return;
        }

        RawHttpRequest request = rawHttp.parseRequest(String.format(
                "POST /products HTTP/1.1\r\n" +
                        "User-Agent: console\r\n" +
                        "Host: %s:%d\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: %d\r\n" +
                        "\r\n" +
                        "%s", HOST, PORT, productJson.length(), productJson));

        sendRequestAndPrintResponse(socket, request, false);
    }

    private static void updateProduct(Socket socket, Scanner scanner) {
        System.out.print("Enter product ID to update: ");
        String idStr = scanner.nextLine();
        try {
            long id = Long.parseLong(idStr);
            ProductImpl updatedProduct = getProductDetailsFromUser(scanner, true);
            updatedProduct.setId(id);
            String productJson = null;
            try {
                productJson = mapper.writeValueAsString(updatedProduct);
            } catch (IOException e) {
                System.err.println("Error serializing product to JSON: " + e.getMessage());
                return;
            }

            RawHttpRequest request = rawHttp.parseRequest(String.format(
                    "PUT /products/%d HTTP/1.1\r\n" +
                            "User-Agent: console\r\n" +
                            "Host: %s:%d\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Content-Length: %d\r\n" +
                            "\r\n" +
                            "%s", id, HOST, PORT, productJson.length(), productJson));

            sendRequestAndPrintResponse(socket, request, false);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
        }
    }

    private static void deleteProduct(Socket socket, Scanner scanner) {
        System.out.print("Enter product ID to delete: ");
        String idStr = scanner.nextLine();
        try {
            int id = Integer.parseInt(idStr);
            RawHttpRequest request = rawHttp.parseRequest(String.format(
                    "DELETE /products/%d HTTP/1.1\r\n" +
                            "User-Agent: console\r\n" +
                            "Host: %s:%d\r\n" +
                            "\r\n", id, HOST, PORT));

            sendRequestAndPrintResponse(socket, request, false);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
        }
    }

    private static ProductImpl getProductDetailsFromUser(Scanner scanner, boolean isUpdate) {
        ProductImpl product = new ProductImpl();
        System.out.print("Enter product name: ");
        product.setName(scanner.nextLine());
        System.out.print("Enter product description: ");
        product.setDescription(scanner.nextLine());
        System.out.print("Enter product unit price: ");
        product.setUnitPrice(Double.parseDouble(scanner.nextLine()));
        System.out.print("Enter product stock: ");
        product.setStock(Double.parseDouble(scanner.nextLine()));
        return product;
    }

    private static void sendRequestAndPrintResponse(Socket socket, RawHttpRequest request, boolean isGetAllProducts) {
        try {
            request.writeTo(socket.getOutputStream());
            socket.getOutputStream().flush(); // Ensure request is sent

            RawHttpResponse<?> response = rawHttp.parseResponse(socket.getInputStream()).eagerly();

            System.out.println("\n--- Server Response ---");
            System.out.println("Status: " + response.getStartLine().getStatusCode() + " " + response.getStartLine().getReason());

            if (response.getBody().isPresent()) {
                String json = response.getBody().get().toString();
                if (isGetAllProducts) {
                    ProductImpl[] products = mapper.readValue(json, ProductImpl[].class);
                    System.out.println("\n--- All Products ---");
                    for (ProductImpl product : products) {
                        System.out.println("ID: " + product.getId() + ", Name: " + product.getName() + ", Description: " + product.getDescription() + ", Price: " + product.getUnitPrice() + ", Stock: " + product.getStock());
                    }
                } else {
                    // Try to parse as a single product, or just print raw JSON
                    try {
                        ProductImpl product = mapper.readValue(json, ProductImpl.class);
                        System.out.println("\n--- Product Details ---");
                        System.out.println("ID: " + product.getId());
                        System.out.println("Name: " + product.getName());
                        System.out.println("Description: " + product.getDescription());
                        System.out.println("Unit Price: " + product.getUnitPrice());
                        System.out.println("Stock: " + product.getStock());
                    } catch (Exception e) {
                        // If it's not a single product, print the raw JSON (e.g., for 204 No Content responses)
                        System.out.println("Raw JSON or non-JSON response: " + json);
                    }
                }
            } else {
                System.out.println("No response body received.");
            }
            System.out.println("-----------------------\n");
        } catch (IOException e) {
            System.err.println("Communication error during request: " + e.getMessage());
            // Do not re-throw, allow the client to continue
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while processing response: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
