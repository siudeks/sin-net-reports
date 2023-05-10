package sinnet;

import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.TestContext;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.Disposables;
import sinnet.db.PostgresDbRunner;
import testcontainers.dapr.DaprContainer;

/**
 * For Cucumber we would like to run 3 things in the same time:
 * Cucumber, SpringBoot, Database
 * The main technical issue is, Cucumber+Spring integration does not honor @ExtendWith, especially when we use
 * @ExtendWith(PostgresDbExtension.class) to run database before tests.
 * As w can't find any way how to run them together, th bootstrapper runs Db and
 * Spring in one piece here.
 * the issue we trying solve: https://stackoverflow.com/questions/74431287/junit-5-how-to-use-extensions-with-a-test-suite
 */
@Slf4j
public final class HostTestContextBootstrapper extends SpringBootTestContextBootstrapper {

  public HostTestContextBootstrapper() {
    super();
    log.error("SPARTA1");
  }
  
  @Override
  public TestContext buildTestContext() {
    
    var dbRunner = new PostgresDbRunner();
    var dbDisposer = dbRunner.start();

    
    var dapr = new DaprContainer();
    dapr.start();
    Disposable daprDisposer = () -> dapr.close();


    var disposer = Disposables.composite(dbDisposer::close, daprDisposer);
    
    var testContext = super.buildTestContext();
    var appContext = (AbstractApplicationContext) testContext.getApplicationContext();
    appContext.addApplicationListener((ContextClosedEvent e) -> disposer.dispose());
    log.error("SPARTA2");
    return testContext;
  }



}
