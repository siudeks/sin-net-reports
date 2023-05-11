package sinnet.gql.api;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import sinnet.gql.models.ProjectEntityGql;
import sinnet.gql.models.SomeEntityGql;
import sinnet.grpc.ProjectsGrpcFacade;

@Controller
@RequiredArgsConstructor
class ProjectsMutationSave {
  
  private final ProjectsGrpcFacade projectsGrpc;

  @SchemaMapping 
  ProjectEntityGql save(ProjectsMutation self, @Argument String name) {
    var requestorEmail = self.requestorEmail();

    // no security check as each person can create its own projects

    var createId = projectsGrpc.create(requestorEmail);

    var updatedId = projectsGrpc.update(requestorEmail, createId, name, requestorEmail, List.of());

    return new ProjectEntityGql()
        .setName(name)
        .setEntity(new SomeEntityGql()
          .setEntityId(updatedId.id())
          .setEntityVersion(updatedId.tag())
          .setProjectId(updatedId.id()));

  }
}
