package sinnet;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@Disabled
public class NativeGreetingResourceIT extends GreetingResourceTest {

    // Execute the same tests but in native mode.
}