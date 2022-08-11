package sinnet.access;

import java.util.function.Function;
import java.util.function.Predicate;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;

import io.grpc.Status;
import io.smallrye.mutiny.Uni;
import io.vavr.Function1;
import io.vavr.collection.Array;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import sinnet.grpc.projects.UserToken;
import sinnet.model.ProjectIdHolder;

/** Entry to check if the user has access to requested action. */
@ApplicationScoped
@RequiredArgsConstructor
class UserAccess implements AccessFacade {

  private final Instance<AccessAs<?>> accessAs;
  
  /** Converts user token to list of all contexts where the user have access to. */
  private Uni<RoleContext> from(UserToken userToken) {
    var sets = Stream.ofAll(accessAs).map(it -> it.calculate(userToken));
    return Uni.combine().all().unis(sets)
        .combinedWith(RoleContextSet.class, Array::ofAll)
        .map(it -> it.filter(x -> x != null))
        .map(RoleContext::new);
  }

  @Override
  public Uni<ProjectIdHolder> guardAccess(UserToken requestor, ProjectIdHolder eid, Function<RoleContext, Predicate<ProjectIdHolder>> methodExtractor) {
    return this.from(requestor)
      .map(methodExtractor::apply)
      .map(this::guardRole)
      .map(it -> it.apply(eid))
      .chain(this::eitherMap);
  }

  private Function1<ProjectIdHolder, Either<Exception, ProjectIdHolder>> guardRole(Predicate<ProjectIdHolder> canContinue) {
    return id -> canContinue.test(id) 
      ? Either.right(id)
      : Either.left(Status.FAILED_PRECONDITION.withDescription("Illegal owner").asException());
  }

  private Uni<ProjectIdHolder> eitherMap(Either<Exception, ProjectIdHolder> it) {
    return it.isRight()
      ? Uni.createFrom().item(it.get())
      : Uni.createFrom().failure(it.getLeft());
  }


}
