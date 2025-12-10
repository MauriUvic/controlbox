package cat.uvic.teknos.dam.controlbox.clients;

import cat.uvic.teknos.dam.controlbox.clients.models.ProductImpl;
import cat.uvic.teknos.dam.controlbox.security.CryptoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class Client2 {
    private static final RawHttp rawHttp = new RawHttp();
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final long INACTIVITY_TIMEOUT_MS = 2 * 60 * 1000; // 2 minutes
    private static final AtomicLong lastActivityTime = new AtomicLong(System.currentTimeMillis());
    private static String sessionKey = null;
    private static final CryptoUtils cryptoUtils = new CryptoUtils();


    public static void main(String[] args) {
        String clientAlias = "client2";

        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to server at " + HOST + ":" + PORT + " as " + clientAlias);

            requestSessionKey(socket, clientAlias);

            Thread inputThread = new Thread(() -> handleUserInput(socket));
            inputThread.setDaemon(true);
            inputThread.start();

            while (inputThread.isAlive()) {
                long inactivityDuration = System.currentTimeMillis() - lastActivityTime.get();
                if (inactivityDuration > INACTIVITY_TIMEOUT_MS) {
                    System.out.println("\nInactivity detected. Sending disconnect message.");
                    sendDisconnectMessage(socket);
                    break;
                }
                Thread.sleep(1000);
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Client main thread interrupted.");
        }
        System.out.println("Client disconnected.");
    }

    private static void handleUserInput(Socket socket) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMainMenu();
                String choice = scanner.nextLine();
                lastActivityTime.set(System.currentTimeMillis());

                if ("exit".equalsIgnoreCase(choice)) {
                    sendDisconnectMessage(socket);
                    break;
                }

                switch (choice) {
                    case "1":
                        manageProducts(socket, scanner);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Input handler finished.");
        }
    }

    private static void sendDisconnectMessage(Socket socket) {
        try {
            RawHttpRequest request = rawHttp.parseRequest(String.format(
                    "DISCONNECT / HTTP/1.1\r\n" +
                            "Host: %s:%d\r\n\r\n", HOST, PORT));
            request.writeTo(socket.getOutputStream());

            RawHttpResponse<?> response = rawHttp.parseResponse(sanitizeResponseStream(socket.getInputStream())).eagerly();
            System.out.println("Server ACK: " + response.getStartLine().getReason());
        } catch (IOException e) {
            System.err.println("Error sending disconnect message: " + e.getMessage());
        }
    }


    private static void printMainMenu() {
        System.out.println("\n--- Client Terminal ---");
        System.out.println("1. Manage Products");
        System.out.println("Type 'exit' to quit.");
        System.out.print("Choose an option: ");
    }

    private static void manageProducts(Socket socket, Scanner scanner) {
        String productChoice;
        while (true) {
            printProductMenu();
            productChoice = scanner.nextLine();
            lastActivityTime.set(System.currentTimeMillis());


            if ("back".equalsIgnoreCase(productChoice)) {
                break;
            }

            switch (productChoice) {
                case "1":
                    sendGetRequest(socket, "/products");
                    break;
                case "2":
                    System.out.print("Enter product ID: ");
                    String id = scanner.nextLine();
                    lastActivityTime.set(System.currentTimeMillis());
                    sendGetRequest(socket, "/products/" + id);
                    break;
                case "3":
                    createProduct(socket, scanner);
                    break;
                case "4":
                    updateProduct(socket, scanner);
                    break;
                case "5":
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

    private static void requestSessionKey(Socket socket, String clientAlias) {
        RawHttpRequest request = rawHttp.parseRequest(String.format(
                "GET /keys/%s HTTP/1.1\r\n" +
                        "User-Agent: console\r\n" +
                        "Host: %s:%d\r\n" +
                        "\r\n", clientAlias, HOST, PORT));
        try {
            request.writeTo(socket.getOutputStream());
            socket.getOutputStream().flush();

            RawHttpResponse<?> response = rawHttp.parseResponse(sanitizeResponseStream(socket.getInputStream())).eagerly();
            if (response.getBody().isPresent()) {
                String encryptedKey = response.getBody().get().toString();

                String keystorePath = clientAlias + ".p12";
                String keystorePassword = "password";
                String keystoreType = "PKCS12";

                sessionKey = cryptoUtils.asymmetricDecrypt(clientAlias, encryptedKey, keystorePath, keystorePassword, keystoreType);
                System.out.println("Session key successfully received and decrypted.");
            }
        } catch (Exception e) {
            System.err.println("Error requesting session key: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void sendGetRequest(Socket socket, String path) {
        String headers = "";
        if (sessionKey != null) {
            headers += "X-Encrypted: true\r\n";
        }

        RawHttpRequest request = rawHttp.parseRequest(String.format(
                "GET %s HTTP/1.1\r\n" +
                        "User-Agent: console\r\n" +
                        "Host: %s:%d\r\n" +
                        "%s" +
                        "\r\n", path, HOST, PORT, headers));

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

        String body = productJson;
        String headers = "";
        if (sessionKey != null) {
            body = cryptoUtils.crypt(productJson, sessionKey);
            headers += "X-Encrypted: true\r\n";
        }
        String hash = cryptoUtils.hash(body);
        headers += "X-Hash: " + hash + "\r\n";


        RawHttpRequest request = rawHttp.parseRequest(String.format(
                "POST /products HTTP/1.1\r\n" +
                        "User-Agent: console\r\n" +
                        "Host: %s:%d\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: %d\r\n" +
                        "%s" +
                        "\r\n" +
                        "%s", HOST, PORT, body.length(), headers, body));

        sendRequestAndPrintResponse(socket, request, false);
    }

    private static void updateProduct(Socket socket, Scanner scanner) {
        System.out.print("Enter product ID to update: ");
        String idStr = scanner.nextLine();
        lastActivityTime.set(System.currentTimeMillis());
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

            String body = productJson;
            String headers = "";
            if (sessionKey != null) {
                body = cryptoUtils.crypt(productJson, sessionKey);
                headers += "X-Encrypted: true\r\n";
            }
            String hash = cryptoUtils.hash(body);
            headers += "X-Hash: " + hash + "\r\n";

            RawHttpRequest request = rawHttp.parseRequest(String.format(
                    "PUT /products/%d HTTP/1.1\r\n" +
                            "User-Agent: console\r\n" +
                            "Host: %s:%d\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Content-Length: %d\r\n" +
                            "%s" +
                            "\r\n" +
                            "%s", id, HOST, PORT, body.length(), headers, body));

            sendRequestAndPrintResponse(socket, request, false);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
        }
    }

    private static void deleteProduct(Socket socket, Scanner scanner) {
        System.out.print("Enter product ID to delete: ");
        String idStr = scanner.nextLine();
        lastActivityTime.set(System.currentTimeMillis());
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
        lastActivityTime.set(System.currentTimeMillis());
        System.out.print("Enter product description: ");
        product.setDescription(scanner.nextLine());
        lastActivityTime.set(System.currentTimeMillis());
        System.out.print("Enter product unit price: ");
        product.setUnitPrice(Double.parseDouble(scanner.nextLine()));
        lastActivityTime.set(System.currentTimeMillis());
        System.out.print("Enter product stock: ");
        product.setStock(Double.parseDouble(scanner.nextLine()));
        lastActivityTime.set(System.currentTimeMillis());
        return product;
    }

    private static void sendRequestAndPrintResponse(Socket socket, RawHttpRequest request, boolean isGetAllProducts) {
        try {
            request.writeTo(socket.getOutputStream());
            socket.getOutputStream().flush();

            RawHttpResponse<?> response = rawHttp.parseResponse(sanitizeResponseStream(socket.getInputStream())).eagerly();

            System.out.println("\n--- Server Response ---");
            System.out.println("Status: " + response.getStartLine().getStatusCode() + " " + response.getStartLine().getReason());

            if (response.getBody().isPresent()) {
                String body = response.getBody().get().toString();

                String receivedHash = response.getHeaders().getFirst("X-Hash").orElse(null);
                String computedHash = cryptoUtils.hash(body);

                if (receivedHash != null && !receivedHash.equals(computedHash)) {
                    System.err.println("Error: Response hash does not match!");
                    return;
                }

                String json = body;
                if (response.getHeaders().getFirst("X-Encrypted").orElse("false").equals("true")) {
                    if (sessionKey == null) {
                        System.err.println("Error: Encrypted response received but no session key is available.");
                        return;
                    }
                    json = cryptoUtils.decrypt(body, sessionKey);
                }


                if (isGetAllProducts) {
                    ProductImpl[] products = mapper.readValue(json, ProductImpl[].class);
                    System.out.println("\n--- All Products ---");
                    for (ProductImpl product : products) {
                        System.out.println("ID: " + product.getId() + ", Name: " + product.getName() + ", Description: " + product.getDescription() + ", Price: " + product.getUnitPrice() + ", Stock: " + product.getStock());
                    }
                } else {
                    try {
                        ProductImpl product = mapper.readValue(json, ProductImpl.class);
                        System.out.println("\n--- Product Details ---");
                        System.out.println("ID: " + product.getId());
                        System.out.println("Name: " + product.getName());
                        System.out.println("Description: " + product.getDescription());
                        System.out.println("Unit Price: " + product.getUnitPrice());
                        System.out.println("Stock: " + product.getStock());
                    } catch (Exception e) {
                        System.out.println("Raw JSON or non-JSON response: " + json);
                    }
                }
            } else {
                System.out.println("No response body received.");
            }
            System.out.println("-----------------------\n");
        } catch (IOException e) {
            System.err.println("Communication error during request: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while processing response: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static InputStream sanitizeResponseStream(InputStream in) throws IOException {
        ByteArrayOutputStream headerBuf = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            headerBuf.write(b);
            byte[] bytes = headerBuf.toByteArray();
            int len = bytes.length;
            if (len >= 4 && bytes[len - 4] == '\r' && bytes[len - 3] == '\n' && bytes[len - 2] == '\r' && bytes[len - 1] == '\n') {
                break;
            }
        }

        if (headerBuf.size() == 0) {
            return in;
        }

        String headerStr = headerBuf.toString(StandardCharsets.ISO_8859_1.name());
        String[] lines = headerStr.split("\r\n");
        if (lines.length == 0) {
            return new SequenceInputStream(new ByteArrayInputStream(headerBuf.toByteArray()), in);
        }

        String startLine = lines[0];
        List<String> keptHeaders = new ArrayList<>();
        boolean firstContentLengthSeen = false;
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line == null || line.isEmpty()) continue;
            int idx = line.indexOf(':');
            if (idx > 0) {
                String name = line.substring(0, idx).trim();
                if ("content-length".equalsIgnoreCase(name)) {
                    if (!firstContentLengthSeen) {
                        keptHeaders.add(line);
                        firstContentLengthSeen = true;
                    }
                } else {
                    keptHeaders.add(line);
                }
            } else {
                keptHeaders.add(line);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(startLine).append("\r\n");
        for (String h : keptHeaders) sb.append(h).append("\r\n");
        sb.append("\r\n");

        byte[] sanitizedHeaderBytes = sb.toString().getBytes(StandardCharsets.ISO_8859_1);
        return new SequenceInputStream(new ByteArrayInputStream(sanitizedHeaderBytes), in);
    }
}
