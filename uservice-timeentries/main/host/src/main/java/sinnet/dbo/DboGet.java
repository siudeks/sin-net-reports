package sinnet.dbo;

import io.vavr.collection.Seq;
import sinnet.grpc.projects.generated.Project;
import sinnet.domain.model.ValEmail;
import sinnet.domain.model.ValProjectId;

/**
 * IDs of all projects owned by a given user.
 *
 * @param ownerEmail email ow the give user
 */
interface DboGet {

  /** List of all projects where given email is marked as owner. */
  Seq<Project> ownedAsProject(ValEmail ownerEmail);
  
  /** List of all projects where given email is marked as owner. */
  Seq<ValProjectId> ownedAsId(ValEmail ownerEmail);

  
  StatsResult getStats(ValEmail ownerEmail);

  Project get(ValProjectId projectId);

  record StatsResult(int numberOdProjects) {
  }
}
