package sinnet.events;

import java.util.UUID;

import lombok.Value;

/**
 * TBD.
 */
@Value(staticConstructor = "of")
public class ProjectCreated implements AppEvent {
  private UUID projectId;
  private Long projectVersion;
  private String name;
}
