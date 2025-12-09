package cat.uvic.teknos.dam.controlbox.server;

import cat.uvic.teknos.dam.controlbox.security.CryptoUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class KeyController {

    private final CryptoUtils cryptoUtils = new CryptoUtils();

    public String processRequest(String method, String path) {
        if ("GET".equalsIgnoreCase(method) && path.startsWith("/keys/")) {
            String clientAlias = path.substring("/keys/".length());
            try {
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(256);
                SecretKey secretKey = keyGen.generateKey();
                String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

                String encryptedKey = cryptoUtils.asymmetricEncrypt(clientAlias, encodedKey);

                return "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: " + encryptedKey.length() + "\r\n" +
                        "\r\n" +
                        encryptedKey;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return "HTTP/1.1 404 Not Found\r\n\r\n";
    }
}
