package sinnet.gql.security;

import io.smallrye.mutiny.Uni;
import lombok.Value;
import sinnet.grpc.common.UserToken;
import sinnet.grpc.roles.GetReply.Role;

/** Single point to return verifed model of authorization of the current invoked based on JWT token.  */
public interface AccessProvider {

  /** Continuse */
  Uni<WithResult> with(String projectId);
  
  @Value
  class WithResult {
      private UserToken userToken;
      private Role projectRole;
  }

}


// package sinnet.domain.access;

// import java.util.UUID;

// import io.smallrye.mutiny.Uni;
// import io.vavr.collection.Seq;
// import lombok.Builder;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import sinnet.models.UserToken;

// public interface DataAccess {

//     interface Provider {

//         /**
//          * TBD.
//          * @param invoker
//          * @param <T> TBD
//          * @return TBD.
//          */
//         <T> AccessOptions<T> with(UserToken invoker);
//     }

//     /**
//      * Allows to strictly define and  handle all known roles supported by the application.
//      *
//      * @param <T> TBD
//      */
//     interface AccessOptions<T> {

//         AccessOptions<T> unlimited(AccessUnlimited<T> handler);

//         AccessOptions<T> limitedByProject(AccessLimitedByProject<T> handler);

//         AccessOptions<T> limitedByGuardian(AccessLimitedByGuardian<T> handler);

//         Uni<T> apply(T defaultValue);
//     }

//     @FunctionalInterface
//     interface AccessUnlimited<T> {
//         Uni<T> handle();
//     }

//     @FunctionalInterface
//     interface AccessLimitedByProject<T> {
//         Uni<T> handle(Seq<UUID> projectIds);
//     }

//     @FunctionalInterface
//     interface AccessLimitedByGuardian<T> {
//         Uni<T> handle(Seq<UUID> projectIds, UUID customerId);
//     }

//     @NoArgsConstructor
//     final class EffectiveAccess<T> implements AccessOptions<T> {

//         @Setter
//         private Seq<UUID> limitedToProjects;

//         @Setter
//         private UUID limitedToGuardian;

//         private AccessUnlimited<T> accessUnlimitedHandler;
//         private AccessLimitedByProject<T> accessLimitedToProjectsHandler;
//         private AccessLimitedByGuardian<T> accessLimitedToGuardianHandler;

//         @Override
//         public AccessOptions<T> unlimited(AccessUnlimited<T> handler) {
//             this.accessUnlimitedHandler = handler;
//             return this;
//         }

//         @Override
//         public AccessOptions<T> limitedByProject(AccessLimitedByProject<T> handler) {
//             this.accessLimitedToProjectsHandler = handler;
//             return this;
//         }

//         @Override
//         public AccessOptions<T> limitedByGuardian(AccessLimitedByGuardian<T> handler) {
//             this.accessLimitedToGuardianHandler = handler;
//             return this;
//         }

//         @Override
//         public Uni<T> apply(T defaultValue) {

//             if (limitedToGuardian != null && limitedToProjects != null && accessLimitedToGuardianHandler != null) {
//                 return accessLimitedToGuardianHandler.handle(limitedToProjects, limitedToGuardian);
//             }

//             if (limitedToProjects != null && accessLimitedToProjectsHandler != null) {
//                 return accessLimitedToProjectsHandler.handle(limitedToProjects);
//             }

//             if (accessUnlimitedHandler != null) {
//                 return accessUnlimitedHandler.handle();
//             }

//             return Uni.createFrom().item(defaultValue);
//         }

//         @Builder
//         private static <T> EffectiveAccess<T> of(Seq<UUID> limitedToProjects, UUID limitedToCustomer) {
//             var effectiveAccess = new EffectiveAccess<T>();
//             effectiveAccess.setLimitedToGuardian(limitedToCustomer);
//             effectiveAccess.setLimitedToProjects(limitedToProjects);
//             return effectiveAccess;
//         }
//     }
// }
