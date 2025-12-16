package cat.uvic.teknos.dam.controlbox.server;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.datasources.SingleConnectionDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    /**
     * Gestiona un pool de fils per atendre múltiples clients de forma concurrent.
     * S'utilitza un CachedThreadPool perquè crea fils segons la demanda i els reutilitza.
     */
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Llista concurrent per emmagatzemar els gestors de clients connectats.
     * CopyOnWriteArrayList és segura per a lectures i escriptures concurrents sense bloquejos explícits.
     */
    private static final List<ClientHandler> connectedClients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        // Inicialització de la capa de dades i controladors per gestionar les peticions.
        JdbcProductRepository productRepository = new JdbcProductRepository(new SingleConnectionDataSource());
        ObjectMapper mapper = new ObjectMapper();
        ProductController productController = new ProductController(productRepository, mapper);
        KeyController keyController = new KeyController();

        // Mapa de controladors per dirigir les peticions a la lògica corresponent segons la ruta.
        Map<String, Object> controllers = new HashMap<>();
        controllers.put("/products", productController);
        controllers.put("/keys", keyController);


        startClientCountReporter();

        /**
         * Obre un ServerSocket al port 5000 i espera connexions de clients en un bucle infinit.
         * Per cada connexió, crea un ClientHandler i el submou a l'ExecutorService.
         */
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started on port 5000");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Server: Client connected from " + clientSocket.getInetAddress());

                    // Cada client és gestionat per un ClientHandler en un fil separat.
                    ClientHandler clientHandler = new ClientHandler(clientSocket, controllers, connectedClients);
                    connectedClients.add(clientHandler);

                    executorService.submit(clientHandler);
                } catch (IOException e) {
                    System.err.println("Server: Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Inicia un fil en segon pla que informa periòdicament del nombre de clients connectats.
     * És útil per monitorar l'estat del servidor sense interrompre la seva funció principal.
     */
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
