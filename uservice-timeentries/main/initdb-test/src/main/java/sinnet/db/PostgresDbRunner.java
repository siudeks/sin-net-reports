package sinnet.db;

import java.util.List;

import org.testcontainers.containers.PostgreSQLContainer;

/** Will be described. */
public final class PostgresDbRunner {

  private static final String TESTCONTAINERS = "testcontainers";
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
      .withUsername(TESTCONTAINERS)
      .withPassword(TESTCONTAINERS)
      .withDatabaseName(TESTCONTAINERS);

  /** Will be described. */
  public SafeAutoCloseable start() {
    postgres.start();
    var items = List.of(
        setProperty("DATABASE_HOST", postgres.getHost()),
        setProperty("DATABASE_NAME", postgres.getDatabaseName()),
        setProperty("DATABASE_PORT", String.valueOf(postgres.getFirstMappedPort())),
        setProperty("DATABASE_USERNAME", postgres.getUsername()),
        setProperty("DATABASE_PASSWORD", postgres.getPassword()),
        setProperty("DATABASE_SCHEMA", "public"));
    return () -> items.forEach(SafeAutoCloseable::close);
  }

  private SafeAutoCloseable setProperty(String propertyName, String propertyValue) {
    var actual = System.getProperty(propertyName);
    System.setProperty(propertyName, propertyValue);
    return actual == null
        ? () -> System.clearProperty(propertyName)
        : () -> System.setProperty(propertyName, actual);
  }

  /** Will be described. */
  @FunctionalInterface
  public interface SafeAutoCloseable extends AutoCloseable {
    void close();
  }

}
