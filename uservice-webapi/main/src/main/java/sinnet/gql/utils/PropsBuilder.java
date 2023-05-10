package sinnet.gql.utils;

import java.util.Optional;
import java.util.function.Consumer;

import io.vavr.Function1;
import lombok.experimental.UtilityClass;

/** Helper class to map data gt proto as proto is not working with null values in traditional mapping paproach. */
@UtilityClass
public class PropsBuilder {
  public static <T> PropSet<T> build(T builder) {
    return new PropSet<>(builder);
  }

  /**
   * Helper class for setting properties for proto3.
   * Proto3 allows not to set values, but id does not allow to set null values.
   * So special semantic is required to simplify operations.
   */
  public static class PropSet<T> {
    private final T builder;

    PropSet(T builder) {
      this.builder = builder;
    }

    /** Try set. Run setter only if value is not null. */
    public <U> PropSet<T> set(Function1<T, Consumer<U>> setter, U maybeValue) {
      return tset(Optional.ofNullable(maybeValue), setter);
    }

    /** Try set. Run setter only if value is not null. */
    public <U1, U2> PropSet<T> set(U1 maybeValue, Function1<U1, U2> map1, Function1<T, Consumer<U2>> setter) {
      return tset(Optional.ofNullable(maybeValue).map(map1), setter);
    }

    public <U1, U2, U3> PropSet<T> set(U1 maybeValue, Function1<U1, U2> map1, Function1<U2, U3> map2, Function1<T, Consumer<U3>> setter) {
      return tset(Optional.ofNullable(maybeValue).map(map1).map(map2), setter);
    }

    /** Try set. Run setter only if value is not null. */
    public <U> PropSet<T> tset(Optional<U> maybeValue, Function1<T, Consumer<U>> setter) {
      if (maybeValue.isEmpty()) {
        return this;
      }
      var value = maybeValue.get();
      setter.apply(builder).accept(value);
      return this;
    }

    public T done() {
      return builder;
    }
  }

  public static <S, U1, U2> Optional<U2> ofNullable(S maybe, Function1<S, U1> map1, Function1<U1, U2> map2) {
    return Optional.ofNullable(maybe).map(map1).map(map2);
  }

  public static <S, U1> Optional<U1> ofNullable(S maybe, Function1<S, U1> map1) {
    return Optional.ofNullable(maybe).map(map1);
  }

  public static <S> Optional<S> ofNullable(S maybe) {
    return Optional.ofNullable(maybe);
  }

}
  