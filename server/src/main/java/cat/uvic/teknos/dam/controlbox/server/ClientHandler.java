package cat.uvic.teknos.dam.controlbox.server;

import cat.uvic.teknos.dam.controlbox.utilities.security.CryptoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Map<String, Object> controllers;
    private final List<ClientHandler> connectedClients;
    private String sessionKey = null;

    public ClientHandler(Socket clientSocket, Map<String, Object> controllers, List<ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.controllers = controllers;
        this.connectedClients = clients;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            while (!clientSocket.isClosed()) {
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

                if ("DISCONNECT".equalsIgnoreCase(method)) {
                    System.out.println("Server: Received disconnect from " + clientSocket.getInetAddress());
                    out.print("HTTP/1.1 200 OK\r\n\r\nDisconnecting\r\n");
                    out.flush();
                    Thread.sleep(1000);
                    break;
                }

                try {
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

                            String receivedHash = headers.get("X-Hash");
                            String computedHash = CryptoUtils.hash(body);

                            if (receivedHash == null || !receivedHash.equals(computedHash)) {
                                out.print("HTTP/1.1 400 Bad Request\r\n\r\nInvalid hash");
                                out.flush();
                                continue;
                            }

                            if (headers.containsKey("X-Encrypted") && "true".equals(headers.get("X-Encrypted"))) {
                                body = CryptoUtils.decrypt(body);
                            }
                        }
                    }

                    String response;
                    if (path.startsWith("/products")) {
                        ProductController controller = (ProductController) controllers.get("/products");
                        response = controller.processRequest(method, path, body);
                    } else if (path.startsWith("/keys")) {
                        KeyController controller = (KeyController) controllers.get("/keys");
                        response = controller.processRequest(method, path);
                        int bodyIndex = response.indexOf("\r\n\r\n");
                        if (bodyIndex != -1 && bodyIndex + 4 < response.length()) {
                            sessionKey = response.substring(bodyIndex + 4);
                        }
                    } else {
                        response = "HTTP/1.1 404 Not Found\r\n\r\n";
                    }


                    String responseBody = "";
                    int bodyIndex = response.indexOf("\r\n\r\n");
                    if (bodyIndex != -1 && bodyIndex + 4 < response.length()) {
                        responseBody = response.substring(bodyIndex + 4);
                    }

                    String headersAndBody;

                    if (sessionKey != null && !path.startsWith("/keys")) {
                        String encryptedBody = CryptoUtils.crypt(responseBody);
                        String responseHash = CryptoUtils.hash(encryptedBody);
                        headersAndBody = "X-Hash: " + responseHash + "\r\n" +
                                "X-Encrypted: true\r\n" +
                                "Content-Length: " + encryptedBody.length() + "\r\n\r\n" +
                                encryptedBody;
                    } else {
                        String responseHash = CryptoUtils.hash(responseBody);
                        headersAndBody = "X-Hash: " + responseHash + "\r\n" +
                                "Content-Length: " + responseBody.length() + "\r\n\r\n" +
                                responseBody;
                    }


                    out.print(response.substring(0, response.indexOf("\r\n\r\n")) + "\r\n" + headersAndBody);
                    out.flush();
                    System.out.println("Server: Response sent.");

                } catch (SocketException e) {
                    System.out.println("Server: Client disconnected abruptly: " + e.getMessage());
                    break;
                } catch (Exception e) {
                    System.err.println("Server: An error occurred while handling client: " + e.getMessage());
                    e.printStackTrace();
                    String response = "HTTP/1.1 500 Internal Server Error\r\n\r\nInternal Server Error";
                    out.print(response);
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("Server: I/O error with client: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Server: Client handler interrupted.");
        } finally {
            try {
                connectedClients.remove(this);
                if (!clientSocket.isClosed()) {
                    clientSocket.close();
                }
                System.out.println("Server: Connection closed for " + clientSocket.getInetAddress() + ". Total clients: " + connectedClients.size());
            } catch (IOException e) {
                System.err.println("Server: Error closing client socket: " + e.getMessage());
            }
        }
    }
}
