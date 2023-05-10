package sinnet.domain.access;

import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import io.grpc.Status;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import sinnet.grpc.projects.generated.UserToken;
import sinnet.domain.model.ValProjectId;

/** Entry to check if the user has access to requested action. */
@Component
@RequiredArgsConstructor
class UserAccess implements AccessFacade {

  private final AccessAs<?>[] accessAs;

  @Override
  @SneakyThrows
  public void guardAccess(UserToken requestor, ValProjectId eid, Function<RoleContext, Predicate<ValProjectId>> methodExtractor) {
    var roleContext = this.from(requestor);
    var isAllowed = methodExtractor.apply(roleContext).test(eid);
    if (isAllowed) {
      return;
    }
    throw Status.PERMISSION_DENIED.asRuntimeException();
  }

  /**
   * Converts user token to list of all contexts where the user have access to.
   */
  private RoleContext from(UserToken userToken) {
    var sets = Stream
        .of(accessAs)
        .map(it -> (RoleContextSet) it.calculate(userToken));
    return new RoleContext(sets);
  }

  @SneakyThrows
  private ValProjectId eitherMap(Either<Exception, ValProjectId> it) {
    if (it.isLeft()) {
      throw it.getLeft();
    }

    return it.get();
  }

}
