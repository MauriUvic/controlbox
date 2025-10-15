package cat.uvic.teknos.dam.controlbox.server;

import cat.uvic.teknos.dam.controlbox.model.Product;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductSupplierRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
    private static RawHttp rawHttp = new RawHttp();

    public static void main(String[] args) throws IOException {
        var server = new ServerSocket(5000);

        var client = server.accept();

        var inputStream = new Scanner(new InputStreamReader(client.getInputStream()));
        var outputStream = new PrintWriter(client.getOutputStream());

        while (true) {
            var request = rawHttp.parseRequest(client.getInputStream());
            var method = request.getMethod();

            var pathParts = request.getUri().getPath().split("/");
            var resource = pathParts[1];

            if (pathParts.length > 2) {
                id = Integer.parseInt(pathParts[2]);
            }

            switch (resource) {
                case "products":
                    if (method.equals("GET")) {
                        if (id > 0) {
                            var product = new JdbcProductSupplierRepository(get(id));

                            var mapper = new ObjectMapper();
                            var productJson = mapper.writeValueAsString(product);

                            rawHttp.parseResponse("HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: application/json\r\n" +
                                    "Content-Length: " + productJson.length() + "\r\n" +
                                    "\r\n" +
                                    productJson).writeTo(client.getOutputStream());
                        }
                    }
                    break;
            }
        }
    }
}