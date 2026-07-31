package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Test unitari di {@link IdGenerator}: valida che gli id generati siano
 * valorizzati, in formato UUID e univoci su un numero elevato di chiamate.
 *
 * @version 1.0
 */
public class IdGeneratorTest {

    @Test
    public void generateId_ShouldNotReturnNullOrBlank() {
        String id = IdGenerator.generateId();
        assertAll("L'id generato deve essere valorizzato",
                () -> assertNotNull(id),
                () -> assertFalse(id.isBlank())
        );
    }

    @Test
    public void generateId_ShouldReturnValidUuidFormat() {
        String id = IdGenerator.generateId();
        // Se non è un UUID valido, fromString solleva IllegalArgumentException
        assertDoesNotThrow(() -> UUID.fromString(id),
                "L'id deve avere il formato di un UUID valido");
    }

    @Test
    public void generateId_ShouldBeUnique_AcrossManyCalls() {
        Set<String> ids = new HashSet<>();
        int iterazioni = 1000;
        for (int i = 0; i < iterazioni; i++) {
            ids.add(IdGenerator.generateId());
        }
        assertEquals(iterazioni, ids.size(), "Ogni id generato deve essere univoco");
    }
}
