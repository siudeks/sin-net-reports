package onlexnet.sinnet.actests.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Project {

  /** In gherkin we use just an alias to name projects, but - fot sake of database constraints - used name should be unique so that we may avoid duplication of project names. */
  @Getter
  private final String alias;

}
