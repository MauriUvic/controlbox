package cat.uvic.teknos.dam.controlbox.server;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.SingleConnectionDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        JdbcProductRepository productRepository = new JdbcProductRepository(new SingleConnectionDataSource());
        ObjectMapper mapper = new ObjectMapper();
        ProductController productController = new ProductController(productRepository, mapper);

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started on port 5000");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Server: Client connected from " + clientSocket.getInetAddress());
                    new Thread(new ClientHandler(clientSocket, productController)).start();
                } catch (IOException e) {
                    System.err.println("Server: Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }
}
