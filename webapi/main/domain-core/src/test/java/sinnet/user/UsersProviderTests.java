package sinnet.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import sinnet.AppTestContext;
import sinnet.BaseDbTests;
import sinnet.Email;
import sinnet.UsersProvider;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(classes = AppTestContext.class)
@TestPropertySource(value = {
    "/domain-core.properties"
})
@Testcontainers
public final class UsersProviderTests extends BaseDbTests {

    @Autowired
    private UsersProvider sut;

    @Test
    public void myTest() {
        var actual = sut.search(Email.of("user1@project"))
            .block()
            .map(it -> it.getEmail().getValue());

        Assertions.assertThat(actual).containsExactlyInAnyOrder(
            "user1@project",
            "user1@project1",
            "user2@project1");
    }
}
