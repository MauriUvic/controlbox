package cat.uvic.teknos.dam.controlbox.utilities.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    void hash() {
        String plainText = "hello world";
        String hash = CryptoUtils.hash(plainText);
        assertEquals("...some expected hash...", hash); // Replace with actual hash
    }

    @Test
    void hashWithNull() {
        assertNull(CryptoUtils.hash((String) null));
        assertNull(CryptoUtils.hash((byte[]) null));
    }

    @Test
    void hashWithEmpty() {
        assertNotNull(CryptoUtils.hash(""));
        assertNotNull(CryptoUtils.hash(new byte[0]));
    }

    @Test
    void hashIsDeterministic() {
        String plainText = "hello world";
        String hash1 = CryptoUtils.hash(plainText);
        String hash2 = CryptoUtils.hash(plainText);
        assertEquals(hash1, hash2);
    }
}
