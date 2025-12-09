package cat.uvic.teknos.dam.controlbox.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Properties;

public class CryptoUtils {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = CryptoUtils.class.getClassLoader().getResourceAsStream("crypto.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find crypto.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String hash(byte[] bytes) {
        String algorithm = properties.getProperty("hash.algorithm");
        String salt = properties.getProperty("hash.salt");
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] encodedhash = digest.digest(bytes);
            return bytesToHex(encodedhash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String hash(String plainText) {
        if (plainText == null) {
            return null;
        }
        return hash(plainText.getBytes(StandardCharsets.UTF_8));
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String crypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(properties.getProperty("symmetric.algorithm"));
            SecretKeySpec key = new SecretKeySpec(properties.getProperty("symmetric.key").getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec iv = new IvParameterSpec(properties.getProperty("symmetric.iv").getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String base64CipherText) {
        try {
            Cipher cipher = Cipher.getInstance(properties.getProperty("symmetric.algorithm"));
            SecretKeySpec key = new SecretKeySpec(properties.getProperty("symmetric.key").getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec iv = new IvParameterSpec(properties.getProperty("symmetric.iv").getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(base64CipherText));
            return new String(plainText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String asymmetricEncrypt(String certificateKeyStoreAlias, String plainText) {
        try (InputStream fis = CryptoUtils.class.getClassLoader().getResourceAsStream(properties.getProperty("keystore.path"))) {
            KeyStore keystore = KeyStore.getInstance(properties.getProperty("keystore.type"));
            keystore.load(fis, properties.getProperty("keystore.password").toCharArray());
            Certificate certificate = keystore.getCertificate(certificateKeyStoreAlias);
            Cipher cipher = Cipher.getInstance(properties.getProperty("asymmetric.algorithm"));
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String asymmetricDecrypt(String privateKeyStoreAlias, String base64CipherText) {
        try (InputStream fis = CryptoUtils.class.getClassLoader().getResourceAsStream(properties.getProperty("keystore.path"))) {
            KeyStore keystore = KeyStore.getInstance(properties.getProperty("keystore.type"));
            keystore.load(fis, properties.getProperty("keystore.password").toCharArray());
            Key key = keystore.getKey(privateKeyStoreAlias, properties.getProperty("keystore.password").toCharArray());
            Cipher cipher = Cipher.getInstance(properties.getProperty("asymmetric.algorithm"));
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(base64CipherText));
            return new String(plainText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
