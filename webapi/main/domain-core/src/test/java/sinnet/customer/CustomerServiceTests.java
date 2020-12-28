package sinnet.customer;

import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.Value;
import sinnet.Dates;
import sinnet.bus.commands.ChangeCustomer;
import sinnet.models.CustomerAuthorization;
import sinnet.models.Email;

@ExtendWith(VertxExtension.class)
public class CustomerServiceTests {


    static Context newContext() {
        var repo = Mockito.mock(CustomerRepository.class);
        var sut = new CustomerService(repo);
        return new Context(sut, repo);
    }

    @Test
    void someTest(Vertx vertx, VertxTestContext testContext) {
        var ctx = newContext();
        var sut = ctx.sut;
        vertx.deployVerticle(sut);

        testContext.completeNow();
    }

    @Nested
    public class MergeAuthorisationShould {

        @Test
        public void replaceEmptyAuthorisations() {
            var requested = new ChangeCustomer.Authorization("location1", "username1", "password1");
            var requestor = Email.of("someone@somewhere");
            var when = Dates.gen().head();
            var empty = new CustomerAuthorization[0];
            var actual = CustomerService.merge(requestor, when, ArrayUtils.toArray(requested), empty);
            var expected = ArrayUtils.toArray(new CustomerAuthorization("location1", "username1", "password1", requestor, when));
            Assertions.assertThat(actual).isEqualTo(expected);
        }

        @Test
        public void removeExistingAuthorisations() {
            var requestedEmpty = new ChangeCustomer.Authorization[0];
            var requestor = Email.of("someone@somewhere");
            var when = Dates.gen().head();
            var someExistinhAuths = ArrayUtils.toArray(new CustomerAuthorization("a", "b", "c", requestor, when));
            var actual = CustomerService.merge(requestor, when, requestedEmpty, someExistinhAuths);
            Assertions.assertThat(actual).isEmpty();
        }

        @Test
        public void updateChangedAuthorisationsCase1() {
            var newRequestor = Email.of("new@requestor");
            var newDate = Dates.gen().head();
            var requested1 = new ChangeCustomer.Authorization("location", "username1", "password1");

            var oldRequestor = Email.of("old@requestor");
            var oldDate = Dates.gen().head();
            var existing1 = new CustomerAuthorization("location", "username1", "c", oldRequestor, oldDate);

            var actual = CustomerService.merge(newRequestor, newDate,
                                               ArrayUtils.toArray(requested1),
                                               ArrayUtils.toArray(existing1));
            Assertions
                .assertThat(actual)
                .containsExactly(new CustomerAuthorization("location", "username1", "password1", newRequestor, newDate));
        }

        @Test
        public void updateChangedAuthorisationsCase2() {
            var newRequestor = Email.of("new@requestor");
            var newDate = Dates.gen().head();
            var requested1 = new ChangeCustomer.Authorization("location", "username1", "password1");

            var oldRequestor = Email.of("old@requestor");
            var oldDate = Dates.gen().head();
            var existing1 = new CustomerAuthorization("location", "username1", "password1", oldRequestor, oldDate);

            var actual = CustomerService.merge(newRequestor, newDate,
                                               ArrayUtils.toArray(requested1),
                                               ArrayUtils.toArray(existing1));
            Assertions
                .assertThat(actual)
                .containsExactly(new CustomerAuthorization("location", "username1", "password1", oldRequestor, oldDate));
        }
    }

    @Value
    static class Context {
        private CustomerService sut;
        private CustomerRepository repo;
    }
}
