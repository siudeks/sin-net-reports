package sinnet;

import java.time.LocalDate;

import io.vavr.collection.Stream;

// Helper class to generate unique dates for testing
public final class Dates {

    private Dates() {
    }

    private static LocalDate initial = LocalDate.of(2001, 2, 3);
    public static Stream<LocalDate> gen() {
        return Stream.continually(() -> {
            var current = initial;
            initial = initial.plusDays(1);
            return current;
        });
    }
}
