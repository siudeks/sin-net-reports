package sinnet.dbo;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sinnet.domain.model.ValProjectId;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
class DboRemoveImpl implements DboRemove {

  private final ProjectRepository repository;

  @Override
  public void remove(ValProjectId idHolder) {

    var eid = idHolder.value();
    repository.deleteById(eid);
  }
  
}
