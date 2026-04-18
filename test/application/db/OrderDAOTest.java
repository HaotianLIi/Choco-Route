package application.db;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class OrderDAOTest {

    @Before
    public void setUp() throws Exception {
        TestSetup.setupTestDatabase();
    }

    @After
    public void tearDown() throws Exception {
        TestSetup.teardown();
    }

    // --- create order ---

    @Test
    public void testCreateOrder() {
        int orderId = OrderDAO.createOrder(2, "USA", 35.0, 70.99);
        assertTrue(orderId > 0);

        // Frankie had 2 seeded orders, now has 3
        List<String[]> orders = OrderDAO.getOrderByUser(2);
        assertEquals(3, orders.size());
    }

    @Test
    public void testCreateOrderData() {
        OrderDAO.createOrder(4, "UK", 50.0, 100.50);

        // Jamal had 1 seeded order, now has 2
        List<String[]> orders = OrderDAO.getOrderByUser(4);
        assertEquals(2, orders.size());
        // Newest first (ORDER BY id DESC)
        assertEquals("UK", orders.get(0)[1]);
        assertEquals("Pending", orders.get(0)[4]);
    }

    @Test
    public void testAddOrderItem() {
        boolean result = OrderDAO.addOrderItem(1, 1, 3, 5.99);
        assertTrue(result);
    }

    // --- update order status (admin) ---

    @Test
    public void testUpdateStatus() {
        boolean result = OrderDAO.updateOrderStatusByAdmin(1, "Shipped");
        assertTrue(result);

        // Verify: Frankie's order 1 should now be "Shipped"
        List<String[]> orders = OrderDAO.getOrderByUser(2);
        // Orders come back DESC, so order 3 is first, order 1 is last
        String lastOrderStatus = orders.get(orders.size() - 1)[4];
        assertEquals("Shipped", lastOrderStatus);
    }

    @Test
    public void testUpdateStatusNonExistentOrder() {
        boolean result = OrderDAO.updateOrderStatusByAdmin(999, "Shipped");
        assertFalse(result);
    }
}