package sinnet.dbo;

import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sinnet.models.ValEmail;
import sinnet.read.RolesProjector;
import sinnet.read.RolesProjector.Role;

/** Projections implementation using VertX async db client. */
@Service
@RequiredArgsConstructor
public class RolesProjectorImpl implements RolesProjector.Provider {

  private final ServicemanRepo repo;

  @Override
  public Role find(ValEmail testedPerson, UUID projectId) {
    var probe = new ServicemanDbo().setProjectId(projectId).setEmail(testedPerson.getValue());
    var example = Example.of(probe);
    var candidates = repo.findAll(example, Sort.unsorted());
    if (candidates.isEmpty()) {
      return Role.NONE;
    }
    return Role.USER;
  }
}
