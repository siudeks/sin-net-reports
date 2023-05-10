package sinnet.reports;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import sinnet.reports.shared.Minutes;

class MinutesTests {

    @Test
    void shouldFormatString() {

      Assertions.assertThat(Minutes.of(15).asString()).isEqualTo("0:15");

      Assertions.assertThat(Minutes.of(7381).asString()).isEqualTo("123:01");

    }
}
