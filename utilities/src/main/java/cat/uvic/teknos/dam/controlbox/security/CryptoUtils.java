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

/**
 * Classe d'utilitat per a operacions criptogràfiques.
 * Proporciona mètodes per a hashing, xifrat/desxifrat simètric i asimètric.
 */
public class CryptoUtils {

    private static final Properties properties = new Properties();

    /**
     * Bloc estàtic per carregar les propietats de configuració criptogràfica.
     * Llegeix el fitxer 'crypto.properties' per obtenir algorismes, claus i altres paràmetres.
     */
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

    /**
     * Calcula el hash d'un array de bytes utilitzant l'algorisme i el salt especificats a les propietats.
     * El salt s'afegeix per protegir contra atacs de taules precalculades (rainbow tables).
     */
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

    /**
     * Mètode de conveniència per calcular el hash d'una cadena de text.
     * Converteix la cadena a bytes i crida al mètode hash principal.
     */
    public String hash(String plainText) {
        if (plainText == null) {
            return null;
        }
        return hash(plainText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converteix un array de bytes a la seva representació hexadecimal.
     * S'utilitza per obtenir una cadena llegible del hash binari.
     */
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

    /**
     * Xifra un text pla utilitzant una clau simètrica per defecte de les propietats.
     * És un mètode de conveniència per a casos d'ús simples.
     */
    public String crypt(String plainText) {
        return crypt(plainText, properties.getProperty("symmetric.key"));
    }

    /**
     * Xifra un text pla utilitzant una clau de sessió simètrica (AES).
     * Utilitza un vector d'inicialització (IV) per garantir que el mateix text no generi el mateix xifrat.
     */
    public String crypt(String plainText, String base64SessionKey) {
        try {
            Cipher cipher = Cipher.getInstance(properties.getProperty("symmetric.algorithm"));
            byte[] decodedKey = Base64.getDecoder().decode(base64SessionKey);
            SecretKeySpec key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            byte[] ivBytesFromProperties = properties.getProperty("symmetric.iv").getBytes(StandardCharsets.UTF_8);
            byte[] iv = new byte[16];
            System.arraycopy(ivBytesFromProperties, 0, iv, 0, Math.min(ivBytesFromProperties.length, 16));
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Desxifra un text xifrat utilitzant una clau simètrica per defecte de les propietats.
     * És un mètode de conveniència.
     */
    public String decrypt(String base64CipherText) {
        return decrypt(base64CipherText, properties.getProperty("symmetric.key"));
    }

    /**
     * Desxifra un text xifrat amb Base64 utilitzant una clau de sessió simètrica (AES).
     * Requereix la mateixa clau i IV que es van utilitzar per al xifrat.
     */
    public String decrypt(String base64CipherText, String base64SessionKey) {
        try {
            Cipher cipher = Cipher.getInstance(properties.getProperty("symmetric.algorithm"));
            byte[] decodedKey = Base64.getDecoder().decode(base64SessionKey);
            SecretKeySpec key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            byte[] ivBytesFromProperties = properties.getProperty("symmetric.iv").getBytes(StandardCharsets.UTF_8);
            byte[] iv = new byte[16];
            System.arraycopy(ivBytesFromProperties, 0, iv, 0, Math.min(ivBytesFromProperties.length, 16));
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(base64CipherText));
            return new String(plainText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Xifra un text pla utilitzant la clau pública d'un certificat emmagatzemat en un KeyStore.
     * Mètode de conveniència que utilitza la configuració per defecte del KeyStore.
     */
    public String asymmetricEncrypt(String certificateKeyStoreAlias, String plainText) {
        return asymmetricEncrypt(
                certificateKeyStoreAlias,
                plainText,
                properties.getProperty("keystore.path"),
                properties.getProperty("keystore.password"),
                properties.getProperty("keystore.type")
        );
    }

    /**
     * Xifra un text pla amb la clau pública obtinguda d'un certificat en un KeyStore.
     * S'utilitza per a l'intercanvi segur de claus, on només el posseïdor de la clau privada pot desxifrar.
     */
    public String asymmetricEncrypt(String certificateKeyStoreAlias, String plainText, String keystorePath, String keystorePassword, String keystoreType) {
        try (InputStream fis = CryptoUtils.class.getClassLoader().getResourceAsStream(keystorePath)) {
            if (fis == null) {
                throw new IOException("Keystore file not found in classpath: " + keystorePath);
            }
            KeyStore keystore = KeyStore.getInstance(keystoreType);
            keystore.load(fis, keystorePassword.toCharArray());
            Certificate certificate = keystore.getCertificate(certificateKeyStoreAlias);
            if (certificate == null) {
                throw new RuntimeException("Certificate alias not found in keystore: " + certificateKeyStoreAlias);
            }
            Cipher cipher = Cipher.getInstance(properties.getProperty("asymmetric.algorithm"));
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Desxifra un text xifrat utilitzant una clau privada d'un KeyStore.
     * Mètode de conveniència que utilitza la configuració per defecte del KeyStore.
     */
    public String asymmetricDecrypt(String privateKeyStoreAlias, String base64CipherText) {
        return asymmetricDecrypt(
                privateKeyStoreAlias,
                base64CipherText,
                properties.getProperty("keystore.path"),
                properties.getProperty("keystore.password"),
                properties.getProperty("keystore.type")
        );
    }

    /**
     * Desxifra un text xifrat amb la clau privada corresponent a un àlies en un KeyStore.
     * Aquest mètode és crucial per desxifrar la clau de sessió enviada pel servidor.
     */
    public String asymmetricDecrypt(String privateKeyStoreAlias, String base64CipherText, String keystorePath, String keystorePassword, String keystoreType) {
        try (InputStream fis = CryptoUtils.class.getClassLoader().getResourceAsStream(keystorePath)) {
            if (fis == null) {
                throw new IOException("Keystore file not found in classpath: " + keystorePath);
            }
            KeyStore keystore = KeyStore.getInstance(keystoreType);
            keystore.load(fis, keystorePassword.toCharArray());
            Key key = keystore.getKey(privateKeyStoreAlias, keystorePassword.toCharArray());
            if (key == null) {
                throw new RuntimeException("Private key alias not found in keystore: " + privateKeyStoreAlias);
            }
            Cipher cipher = Cipher.getInstance(properties.getProperty("asymmetric.algorithm"));
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(base64CipherText));
            return new String(plainText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
