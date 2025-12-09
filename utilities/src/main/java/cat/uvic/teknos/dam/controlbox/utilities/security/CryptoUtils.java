package cat.uvic.teknos.dam.controlbox.utilities.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

    private static final Properties properties = new Properties();

    static {
        try (var input = CryptoUtils.class.getClassLoader().getResourceAsStream("crypto.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find crypto.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String hash(String plainText) {
        if (plainText == null) {
            return null;
        }
        return hash(plainText.getBytes());
    }

    public static String hash(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(properties.getProperty("hash.algorithm"));
            md.update(properties.getProperty("hash.salt").getBytes());
            byte[] digest = md.digest(bytes);
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String crypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(properties.getProperty("symmetric.algorithm"));
            SecretKeySpec key = new SecretKeySpec(properties.getProperty("symmetric.key").getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(properties.getProperty("symmetric.iv").getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String base64CipherText) {
        try {
            Cipher cipher = Cipher.getInstance(properties.getProperty("symmetric.algorithm"));
            SecretKeySpec key = new SecretKeySpec(properties.getProperty("symmetric.key").getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(properties.getProperty("symmetric.iv").getBytes());
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(base64CipherText));
            return new String(plainText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String asymmetricEncrypt(String certificateKeyStoreAlias, String plainText) {
        try {
            KeyStore keyStore = KeyStore.getInstance(properties.getProperty("asymmetric.keystore.type"));
            try (FileInputStream fis = new FileInputStream(properties.getProperty("asymmetric.keystore.path"))) {
                keyStore.load(fis, properties.getProperty("asymmetric.keystore.password").toCharArray());
            }
            Certificate certificate = keyStore.getCertificate(certificateKeyStoreAlias);
            PublicKey publicKey = certificate.getPublicKey();
            Cipher cipher = Cipher.getInstance(properties.getProperty("asymmetric.algorithm"));
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String asymmetricDecrypt(String privateKeyStoreAlias, String base64CipherText) {
        try {
            KeyStore keyStore = KeyStore.getInstance(properties.getProperty("asymmetric.keystore.type"));
            try (FileInputStream fis = new FileInputStream(properties.getProperty("asymmetric.keystore.path"))) {
                keyStore.load(fis, properties.getProperty("asymmetric.keystore.password").toCharArray());
            }
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(privateKeyStoreAlias, properties.getProperty("asymmetric.keystore.password").toCharArray());
            Cipher cipher = Cipher.getInstance(properties.getProperty("asymmetric.algorithm"));
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(base64CipherText));
            return new String(plainText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
