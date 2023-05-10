package sinnet.domain.model;

import java.util.UUID;

import lombok.Value;
import lombok.experimental.Accessors;

/** Versioned Project id. */
@Value(staticConstructor = "of")
@Accessors(fluent = true)
public class ProjectVid {
  private final UUID id;
  private final long tag;

  public static ProjectVid of(ValProjectId eid, long etag) {
    return ProjectVid.of(eid.value(), etag);
  }
}
