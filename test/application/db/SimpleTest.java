package application.db;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTest {

    @Test
    public void testAddition() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testStringNotNull() {
        String name = "ChocoRoute";
        assertNotNull(name);
        assertEquals("ChocoRoute", name);
    }
}