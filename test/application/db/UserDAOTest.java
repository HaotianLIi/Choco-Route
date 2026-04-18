package application.db;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserDAOTest {

    @Before
    public void setUp() throws Exception {
        TestSetup.setupTestDatabase();
    }

    @After
    public void tearDown() throws Exception {
        TestSetup.teardown();
    }

    // --- authenticate ---

    @Test
    public void testLoginAdmin() {
        String role = UserDAO.authenticate("admin", "admin123");
        assertNotNull(role);
        assertEquals("admin", role);
    }

    @Test
    public void testLoginCustomer() {
        String role = UserDAO.authenticate("Frankie", "Frankie");
        assertNotNull(role);
        assertEquals("customer", role);
    }

    @Test
    public void testLoginWrongPassword() {
        String role = UserDAO.authenticate("admin", "wrong");
        assertNull(role);
    }

    @Test
    public void testLoginNonExistentUser() {
        String role = UserDAO.authenticate("ghost", "ghost");
        assertNull(role);
    }
}
