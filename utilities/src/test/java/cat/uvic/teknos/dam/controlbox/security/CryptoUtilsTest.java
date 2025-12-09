package cat.uvic.teknos.dam.controlbox.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    void hash_DeterministicOutput() {
        CryptoUtils cryptoUtils = new CryptoUtils();
        String text = "hello world";
        String hash1 = cryptoUtils.hash(text);
        String hash2 = cryptoUtils.hash(text);
        assertEquals(hash1, hash2);
    }

    @Test
    void hash_DifferentOutputForDifferentInput() {
        CryptoUtils cryptoUtils = new CryptoUtils();
        String text1 = "hello world";
        String text2 = "hello world!";
        String hash1 = cryptoUtils.hash(text1);
        String hash2 = cryptoUtils.hash(text2);
        assertNotEquals(hash1, hash2);
    }

    @Test
    void hash_NullInput() {
        CryptoUtils cryptoUtils = new CryptoUtils();
        assertNull(cryptoUtils.hash((String) null));
    }

    @Test
    void hash_EmptyInput() {
        CryptoUtils cryptoUtils = new CryptoUtils();
        String hash = cryptoUtils.hash("");
        assertNotNull(hash);
    }
}
