package sinnet.gql.api;

import lombok.Value;

/** Fixme. */
@Value(staticConstructor = "of")
public class ProjectsQuery {
  private String requestorEmail;
}
