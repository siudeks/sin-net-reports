package net.onlex;

import java.time.Duration;

import io.smallrye.mutiny.Uni;

class UniExtensions {
  public static <T> T sync(Uni<T> async) {
    return async.await().atMost(Duration.ofMinutes(1));
  }
}