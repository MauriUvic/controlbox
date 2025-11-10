package cat.uvic.teknos.dam.controlbox.server;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.SingleConnectionDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final List<ClientHandler> connectedClients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        JdbcProductRepository productRepository = new JdbcProductRepository(new SingleConnectionDataSource());
        ObjectMapper mapper = new ObjectMapper();
        ProductController productController = new ProductController(productRepository, mapper);

        startClientCountReporter();

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started on port 5000");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Server: Client connected from " + clientSocket.getInetAddress());

                    ClientHandler clientHandler = new ClientHandler(clientSocket, productController, connectedClients);
                    connectedClients.add(clientHandler);

                    executorService.submit(clientHandler);
                } catch (IOException e) {
                    System.err.println("Server: Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }

    private static void startClientCountReporter() {
        Thread reporterThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000); // 1 minute
                    System.out.println("[STATUS] Currently connected clients: " + connectedClients.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Client count reporter was interrupted.");
                }
            }
        });

        reporterThread.setDaemon(true);
        reporterThread.start();
    }
}
