package sinnet.domain.access;

import java.util.UUID;

import io.vavr.collection.Seq;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sinnet.models.UserToken;

/**
 * TBD.
 */
public interface DataAccess {

  /**
   * TBD.
   */
  interface Provider {

    /**
     * TBD.
     *
     * @param invoker tbd
     * @param <T> TBD
     * @return TBD.
     */
    <T> AccessOptions<T> with(UserToken invoker);
  }

  /**
   * Allows to strictly define and  handle all known roles supported by the application.
   *
   * @param <T> TBD
   */
  interface AccessOptions<T> {

    AccessOptions<T> unlimited(AccessUnlimited<T> handler);

    AccessOptions<T> limitedByProject(AccessLimitedByProject<T> handler);

    AccessOptions<T> limitedByGuardian(AccessLimitedByGuardian<T> handler);

    T apply(T defaultValue);
  }

  /**
   * TBD.
   */
  @FunctionalInterface
  interface AccessUnlimited<T> {

    T handle();
  }

  /**
   * TBD.
   */
  @FunctionalInterface
  interface AccessLimitedByProject<T> {

    T handle(Seq<UUID> projectIds);
  }

  /**
   * TBD.
   */
  @FunctionalInterface
  interface AccessLimitedByGuardian<T> {

    T handle(Seq<UUID> projectIds, UUID customerId);
  }

  /**
   * TBD.
   */
  @NoArgsConstructor
  final class EffectiveAccess<T> implements AccessOptions<T> {

    @Setter
    private Seq<UUID> limitedToProjects;

    @Setter
    private UUID limitedToGuardian;

    private AccessUnlimited<T> accessUnlimitedHandler;
    private AccessLimitedByProject<T> accessLimitedToProjectsHandler;
    private AccessLimitedByGuardian<T> accessLimitedToGuardianHandler;

    @Override
    public AccessOptions<T> unlimited(AccessUnlimited<T> handler) {
      this.accessUnlimitedHandler = handler;
      return this;
    }

    @Override
    public AccessOptions<T> limitedByProject(AccessLimitedByProject<T> handler) {
      this.accessLimitedToProjectsHandler = handler;
      return this;
    }

    @Override
    public AccessOptions<T> limitedByGuardian(AccessLimitedByGuardian<T> handler) {
      this.accessLimitedToGuardianHandler = handler;
      return this;
    }

    @Override
    public T apply(T defaultValue) {

      if (limitedToGuardian != null && limitedToProjects != null && accessLimitedToGuardianHandler != null) {
        return accessLimitedToGuardianHandler.handle(limitedToProjects, limitedToGuardian);
      }

      if (limitedToProjects != null && accessLimitedToProjectsHandler != null) {
        return accessLimitedToProjectsHandler.handle(limitedToProjects);
      }

      if (accessUnlimitedHandler != null) {
        return accessUnlimitedHandler.handle();
      }

      return defaultValue;
    }

    @Builder
    private static <T> EffectiveAccess<T> of(Seq<UUID> limitedToProjects, UUID limitedToCustomer) {
      var effectiveAccess = new EffectiveAccess<T>();
      effectiveAccess.setLimitedToGuardian(limitedToCustomer);
      effectiveAccess.setLimitedToProjects(limitedToProjects);
      return effectiveAccess;
    }
  }
}
