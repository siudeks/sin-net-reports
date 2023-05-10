package sinnet.db;

import java.util.List;

import org.testcontainers.containers.PostgreSQLContainer;

import lombok.extern.slf4j.Slf4j;

/** Designed to be used to run single instance for all tests in single module. */
@Slf4j
public final class PostgresDbRunner {

  private static final String TESTCONTAINERS = "testcontainers";
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
      .withUsername(TESTCONTAINERS)
      .withPassword(TESTCONTAINERS)
      .withDatabaseName(TESTCONTAINERS);

  /**
   * tarts database and sets all exopected environments variables, used to construct propoer jdbc connection.
   *
   * @return AutoCloseable that will stop the server and restore all environment variables to their original values.
   */
  public SafeAutoCloseable start() {
    postgres.start();
    var items = List.of(
        setProperty("DATABASE_HOST", postgres.getHost()),
        setProperty("DATABASE_PORT", postgres.getFirstMappedPort().toString()),
        setProperty("DATABASE_NAME", postgres.getDatabaseName()),
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

  /** AutoCloseable which doesn't be to close with handling exception from close method. */
  @FunctionalInterface
  public interface SafeAutoCloseable extends AutoCloseable {
    void close();
  }

}
