package application.db;

//https://documentation.red-gate.com/flyway/getting-started-with-flyway
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

public class DatabaseInitializer {
	public static void initialize() throws FlywayException {
		
		Flyway flyway = Flyway.configure().dataSource(DatabaseConnection.getUrl(), null, null)
				.locations("classpath:application/db/migration")
				.baselineOnMigrate(true)
				.load();
		
		flyway.migrate();
		
		System.out.println("Database migration done, it's now up to date");
	}
}
