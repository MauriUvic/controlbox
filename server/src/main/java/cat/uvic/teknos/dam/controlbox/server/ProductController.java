package cat.uvic.teknos.dam.controlbox.server;

import cat.uvic.teknos.dam.controlbox.jdbc.JdbcProductRepository;
import cat.uvic.teknos.dam.controlbox.jdbc.model.JdbcProduct;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductController {
    private final JdbcProductRepository productRepository;
    private final ObjectMapper mapper;

    public ProductController(JdbcProductRepository productRepository, ObjectMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    public String processRequest(String method, String path, String body) throws Exception {
        if (!path.startsWith("/products")) {
            return "HTTP/1.1 404 Not Found\r\n\r\nNot Found";
        }

        String[] pathParts = path.split("/");
        int id = 0;
        if (pathParts.length > 2) {
            try {
                id = Integer.parseInt(pathParts[2]);
            } catch (NumberFormatException e) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid product ID format";
            }
        }

        switch (method) {
            case "GET":
                if (id > 0) {
                    var product = productRepository.get(id);
                    if (product != null) {
                        var productJson = mapper.writeValueAsString(product);
                        return createJsonResponse(productJson, "200 OK");
                    } else {
                        return "HTTP/1.1 404 Not Found\r\n\r\nProduct not found";
                    }
                } else {
                    var products = productRepository.getAll();
                    var productsJson = mapper.writeValueAsString(products);
                    return createJsonResponse(productsJson, "200 OK");
                }
            case "POST":
                if (body == null) {
                    return "HTTP/1.1 400 Bad Request\r\n\r\nRequest body is missing";
                }
                var newProduct = mapper.readValue(body, JdbcProduct.class);
                productRepository.save(newProduct);
                var newProductJson = mapper.writeValueAsString(newProduct);
                return createJsonResponse(newProductJson, "201 Created");
            case "PUT":
                if (id <= 0) {
                    return "HTTP/1.1 400 Bad Request\r\n\r\nProduct ID is required for PUT request";
                }
                if (body == null) {
                    return "HTTP/1.1 400 Bad Request\r\n\r\nRequest body is missing";
                }
                var updatedProduct = mapper.readValue(body, JdbcProduct.class);
                updatedProduct.setId((long) id);
                productRepository.save(updatedProduct);
                return "HTTP/1.1 204 No Content\r\n\r\n";
            case "DELETE":
                if (id <= 0) {
                    return "HTTP/1.1 400 Bad Request\r\n\r\nProduct ID is required for DELETE request";
                }
                productRepository.delete(id);
                return "HTTP/1.1 204 No Content\r\n\r\n";
            default:
                return "HTTP/1.1 405 Method Not Allowed\r\n\r\nMethod not supported";
        }
    }

    private String createJsonResponse(String json, String status) {
        return "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + json.length() + "\r\n" +
                "\r\n" +
                json;
    }
}