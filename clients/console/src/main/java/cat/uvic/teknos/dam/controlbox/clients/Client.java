package cat.uvic.teknos.dam.controlbox.client;

import cat.uvic.teknos.dam.controlbox.client.exceptions.ClientException;
import cat.uvic.teknos.dam.controlbox.client.models.ProductImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static RawHttp rawHttp = new RawHttp();
    private static final String HOST = "localhost";

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);

        var inputStream = new Scanner(new InputStreamReader(socket.getInputStream()));
        var outputStream = new PrintWriter(socket.getOutputStream());

        var scanner = new Scanner(System.in);
        var request = "";
        while (! (request = scanner.nextLine()).equals("exit")) {

            switch (request) {
                case "1":
                    manageProduct(socket, scanner);
                    break;
            }
            outputStream.println(request);
            outputStream.flush();

            System.out.println(inputStream.nextLine());
        }

        socket.close();
    }

    private static void manageProduct(Socket socket, Scanner scanner) {
        System.out.println("Introduce el ID del producto:");
        var productId = scanner.nextLine();

        switch (scanner.nextLine()) {
            case "1": // Get by id
                RawHttpRequest request = rawHttp.parseRequest(String.format(
                        "GET /products/%s HTTP/1.1\r\n" +
                                "User-Agent: console\r\n" +
                                "Host: %s \r\n", productId, HOST));

                try {
                    request.writeTo(socket.getOutputStream());
                } catch (IOException e) {
                    throw new ClientException(e);
                }

                try {
                    var response = rawHttp.parseResponse(socket.getInputStream()).eagerly();
                    var json = response.getBody().get().toString();

                    var mapper = new ObjectMapper();
                    var product = mapper.readValue(json, ProductImpl.class);

                    System.out.println("ID: " + product.getId());
                    System.out.println("Nombre: " + product.getName());
                    System.out.println("Descripción: " + product.getDescription());
                    System.out.println("Precio Unitario: " + product.getUnitPrice());
                    System.out.println("Stock: " + product.getStock());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                break;
        }
    }
}