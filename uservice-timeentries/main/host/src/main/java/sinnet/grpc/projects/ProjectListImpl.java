package sinnet.grpc.projects;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sinnet.dbo.DboFacade;
import sinnet.grpc.projects.generated.ListReply;
import sinnet.grpc.projects.generated.ListRequest;
import sinnet.domain.model.ValEmail;

@Component
@RequiredArgsConstructor
class ProjectListImpl implements RpcQueryHandler<ListRequest, ListReply> {

  private final DboFacade dbo;

  @Override
  public ListReply apply(ListRequest request) {
    var emailOfRequestor = ValEmail.of(request.getEmailOfRequestor());
    var result = dbo.ownedAsProject(emailOfRequestor);
    return ListReply.newBuilder().addAllProjects(result).build();
  }

}
