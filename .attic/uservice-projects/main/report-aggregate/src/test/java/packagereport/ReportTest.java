package packagereport;

import org.junit.jupiter.api.Test;

/**
 * Simple using tests to run tests in the module so that jacoco plugin could start it's work.
 * Without the test jacococ will be not invoked in the module and finally aggregated code coverage will be empty.
 */
public class ReportTest {

	@Test
	public void test() {
	}
}
