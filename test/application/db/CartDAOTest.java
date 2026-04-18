package application.db;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CartDAOTest {

    @Before
    public void setUp() throws Exception {
        TestSetup.setupTestDatabase();
    }

    @After
    public void tearDown() throws Exception {
        TestSetup.teardown();
    }

    // --- get cart items ---

    @Test
    public void testGetCartItems() {
        // Frankie (userId=2) has 2 seeded cart items (product 1 and product 4)
        List<String[]> items = CartDAO.getAllCartItem(2);
        assertEquals(2, items.size());
    }

    @Test
    public void testGetCartItemsEmpty() {
        // Jamal (userId=4) has nothing in cart
        List<String[]> items = CartDAO.getAllCartItem(4);
        assertTrue(items.isEmpty());
    }

    // --- add to cart ---

    @Test
    public void testInsertCartItem() {
        // Add product 3 to Jamal's cart
        boolean result = CartDAO.insertCartItem(4, 3, 2);
        assertTrue(result);

        List<String[]> items = CartDAO.getAllCartItem(4);
        assertEquals(1, items.size());
        assertEquals("White Chocolate Almonds", items.get(0)[1]);
        assertEquals("2", items.get(0)[3]);
    }

    // --- update quantity ---

    @Test
    public void testUpdateQuantity() {
        // Update Frankie's first cart item (cartId=1) to qty 10
        boolean result = CartDAO.updateCartItem(1, 10);
        assertTrue(result);

        List<String[]> items = CartDAO.getAllCartItem(2);
        assertEquals("10", items.get(0)[3]);
    }

    // --- remove item ---

    @Test
    public void testRemoveItem() {
        boolean result = CartDAO.removeCartItem(1);
        assertTrue(result);

        // Frankie now has 1 item left
        List<String[]> items = CartDAO.getAllCartItem(2);
        assertEquals(1, items.size());
    }

    // --- clear entire cart ---

    @Test
    public void testClearCart() {
        CartDAO.removeEntireCart(2);

        List<String[]> items = CartDAO.getAllCartItem(2);
        assertTrue(items.isEmpty());
    }
}