package sinnet.db;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.common.util.concurrent.Runnables;

/**
 * Reusable extension to start local PostgreSQL instance for µservices based on
 * its databases.
 */
public final class PostgresDbExtension implements BeforeAllCallback, AfterAllCallback {

  private AutoCloseable disposer = Runnables::doNothing;

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    var dbRunner = new PostgresDbRunner();
    disposer = dbRunner.start();
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    disposer.close();
  }

}
