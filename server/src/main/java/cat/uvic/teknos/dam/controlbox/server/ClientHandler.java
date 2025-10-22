package cat.uvic.teknos.dam.controlbox.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ProductController productController;

    public ClientHandler(Socket clientSocket, ProductController productController) {
        this.clientSocket = clientSocket;
        this.productController = productController;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            while (!clientSocket.isClosed()) {
                String response;
                try {
                    String requestLine = in.readLine();
                    if (requestLine == null) {
                        System.out.println("Server: Client closed connection.");
                        break;
                    }

                    System.out.println("Server: Received request line: " + requestLine);

                    String[] requestParts = requestLine.split(" ");
                    if (requestParts.length < 2) {
                        out.print("HTTP/1.1 400 Bad Request\r\n\r\nInvalid request");
                        out.flush();
                        continue;
                    }
                    String method = requestParts[0];
                    String path = requestParts[1];

                    Map<String, String> headers = new HashMap<>();
                    String headerLine;
                    while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                        String[] headerParts = headerLine.split(": ", 2);
                        if (headerParts.length == 2) {
                            headers.put(headerParts[0], headerParts[1]);
                        }
                    }

                    String body = null;
                    if (headers.containsKey("Content-Length")) {
                        int contentLength = Integer.parseInt(headers.get("Content-Length"));
                        if (contentLength > 0) {
                            char[] bodyChars = new char[contentLength];
                            in.read(bodyChars, 0, contentLength);
                            body = new String(bodyChars);
                        }
                    }

                    response = productController.processRequest(method, path, body);
                } catch (SocketException e) {
                    System.out.println("Server: Client disconnected abruptly: " + e.getMessage());
                    break;
                } catch (Exception e) {
                    System.err.println("Server: An error occurred while handling client: " + e.getMessage());
                    e.printStackTrace();
                    response = "HTTP/1.1 500 Internal Server Error\r\n\r\nInternal Server Error";
                }

                out.print(response);
                out.flush();
                System.out.println("Server: Response sent.");
            }
        } catch (IOException e) {
            System.err.println("Server: I/O error with client: " + e.getMessage());
        }
    }
}
