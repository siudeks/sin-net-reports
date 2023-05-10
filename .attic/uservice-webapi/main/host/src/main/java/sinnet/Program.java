package sinnet;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * This main class will bootstrap Quarkus and run it until it stops. This is no different to
 *  the automatically generated main class, but has the advantage that you can just launch it
 * directly from the IDE without needing to run a Maven or Gradle command.
 * More: https://quarkus.io/guides/lifecycle#the-main-method
 */
@QuarkusMain  
public class Program {

  public static void main(String ... args) {
    Quarkus.run(args); 
  }
}
