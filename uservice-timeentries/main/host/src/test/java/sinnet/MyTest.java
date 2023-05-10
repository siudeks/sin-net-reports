package sinnet;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.module.Configuration;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.env.Environment;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.WebApplicationContext;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(HostTestContextBootstrapper.class)
public @interface MyTest {

	/**
	 * Alias for {@link #properties()}.
	 * @return the properties to apply
	 */
	@AliasFor("properties")
	String[] value() default {};

	/**
	 * Properties in form {@literal key=value} that should be added to the Spring
	 * {@link Environment} before the test runs.
	 * @return the properties to add
	 */
	@AliasFor("value")
	String[] properties() default {};

	/**
	 * Application arguments that should be passed to the application under test.
	 * @return the application arguments to pass to the application under test.
	 * @see ApplicationArguments
	 * @see SpringApplication#run(String...)
	 * @since 2.2.0
	 */
	String[] args() default {};

	/**
	 * The <em>component classes</em> to use for loading an
	 * {@link org.springframework.context.ApplicationContext ApplicationContext}. Can also
	 * be specified using
	 * {@link ContextConfiguration#classes() @ContextConfiguration(classes=...)}. If no
	 * explicit classes are defined the test will look for nested
	 * {@link Configuration @Configuration} classes, before falling back to a
	 * {@link SpringBootConfiguration @SpringBootConfiguration} search.
	 * @see ContextConfiguration#classes()
	 * @return the component classes used to load the application context
	 */
	Class<?>[] classes() default {};

	/**
	 * The type of web environment to create when applicable. Defaults to
	 * {@link WebEnvironment#MOCK}.
	 * @return the type of web environment
	 */
	WebEnvironment webEnvironment() default WebEnvironment.MOCK;

	/**
	 * The type of main method usage to employ when creating the {@link SpringApplication}
	 * under test.
	 * @return the type of main method usage
	 * @since 3.0.0
	 */
	UseMainMethod useMainMethod() default UseMainMethod.NEVER;

	/**
	 * An enumeration web environment modes.
	 */
	enum WebEnvironment {

		/**
		 * Creates a {@link WebApplicationContext} with a mock servlet environment if
		 * servlet APIs are on the classpath, a {@link ReactiveWebApplicationContext} if
		 * Spring WebFlux is on the classpath or a regular {@link ApplicationContext}
		 * otherwise.
		 */
		MOCK(false),

		/**
		 * Creates a web application context (reactive or servlet based) and sets a
		 * {@code server.port=0} {@link Environment} property (which usually triggers
		 * listening on a random port). Often used in conjunction with a
		 * {@link LocalServerPort @LocalServerPort} injected field on the test.
		 */
		RANDOM_PORT(true),

		/**
		 * Creates a (reactive) web application context without defining any
		 * {@code server.port=0} {@link Environment} property.
		 */
		DEFINED_PORT(true),

		/**
		 * Creates an {@link ApplicationContext} and sets
		 * {@link SpringApplication#setWebApplicationType(WebApplicationType)} to
		 * {@link WebApplicationType#NONE}.
		 */
		NONE(false);

		private final boolean embedded;

		WebEnvironment(boolean embedded) {
			this.embedded = embedded;
		}

		/**
		 * Return if the environment uses an {@link ServletWebServerApplicationContext}.
		 * @return if an {@link ServletWebServerApplicationContext} is used.
		 */
		public boolean isEmbedded() {
			return this.embedded;
		}

	}

	/**
	 * Enumeration of how the main method of the
	 * {@link SpringBootConfiguration @SpringBootConfiguration}-annotated class is used
	 * when creating and running the {@link SpringApplication} under test.
	 * @since 3.0.0
	 */
	enum UseMainMethod {

		/**
		 * Always use the {@code main} method. A failure will occur if there is no
		 * {@link SpringBootConfiguration @SpringBootConfiguration}-annotated class or
		 * that class does not have a main method.
		 */
		ALWAYS,

		/**
		 * Never use the {@code main} method, creating a test-specific
		 * {@link SpringApplication} instead.
		 */
		NEVER,

		/**
		 * Use the {@code main} method when it is available. If there is no
		 * {@link SpringBootConfiguration @SpringBootConfiguration}-annotated class or
		 * that class does not have a main method, a test-specific
		 * {@link SpringApplication} will be used.
		 */
		WHEN_AVAILABLE;

	}

}
