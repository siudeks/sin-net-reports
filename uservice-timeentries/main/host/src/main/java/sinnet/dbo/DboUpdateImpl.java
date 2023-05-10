package sinnet.dbo;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.grpc.Status;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import sinnet.domain.model.ProjectVid;
import sinnet.domain.model.ValEmail;

@Component
@AllArgsConstructor
class DboUpdateImpl implements DboUpdate {

  private final ProjectRepository repository;
  
  @Transactional
  @Override
  public Either<Status, ProjectVid> updateCommand(ProjectVid vid, UpdateCommandContent content) {
    var eid = vid.id();
    var etag = vid.tag();

    try {
      // load to memory a managed instance so that update may be done with preloaded instance with given locking type
      return Option.ofOptional(repository.findById(eid))
        // set expected version so that optimistic locking will reject operation, if version would not be the same
        .peek(it -> it.setVersion(etag))
        .peek(it -> applyCommand(it, content))
        .map(it -> repository.saveAndFlush(it))
        .map(this::getVersion)
        .toEither(Status.NOT_FOUND);
    } catch (ObjectOptimisticLockingFailureException e) {
      return Either.left(Status.FAILED_PRECONDITION);
    }
  }

  @SneakyThrows
  private ProjectDbo guardVersion(Either<Exception, ProjectDbo> it) {
    if (it.isLeft()) {
      throw it.getLeft();
    }
    return it.get();
  }

  private ProjectVid getVersion(ProjectDbo it) {
    return ProjectVid.of(it.getEntityId(), it.getVersion());
  }

  private void applyCommand(ProjectDbo dbEntity, UpdateCommandContent cmd) {
    var operators = Stream.of(cmd.operators()).map(ValEmail::value).collect(Collectors.toSet());
    dbEntity.setName(cmd.name());
    dbEntity.setEmailOfOwner(cmd.newOwner().value());
    dbEntity.setOperators(operators);
  }

}
