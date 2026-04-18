package application.db;

import java.io.File;

import org.flywaydb.core.Flyway;

public class TestSetup {

    private static final String TEST_DB_FILE = "test-chocoroute.db";
    private static final String TEST_URL = "jdbc:sqlite:" + TEST_DB_FILE;

    public static void setupTestDatabase() throws Exception {
        // Delete old test DB if it exists, so every test run starts fresh
        File dbFile = new File(TEST_DB_FILE);
        if (dbFile.exists()) {
//            dbFile.delete();
        }

        // Point the app to the test database
        DatabaseConnection.setUrl(TEST_URL);

        // Run the real Flyway migrations (V1 creates tables, V2 seeds data)
        DatabaseInitializer.initialize();
    }

    public static void teardown() throws Exception {
        // Reset URL back to production
        DatabaseConnection.setUrl("jdbc:sqlite:chocoroute.db");

        // Delete the test database file
        File dbFile = new File(TEST_DB_FILE);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }
}