package sinnet.grpc.users;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sinnet.grpc.mapping.RpcQueryHandler;
import sinnet.models.ValEmail;
import sinnet.read.UsersRepositoryEx;

/**
 * TBD.
 */
@Component
@RequiredArgsConstructor
public class UsersRpcSearch implements RpcQueryHandler<SearchRequest, SearchReply>, MapperDto {

  private final UsersRepositoryEx usersProvider;

  @Override
  public SearchReply apply(SearchRequest request) {
    var emailAsString = request.getUserToken().getRequestorEmail();
    var projectIdAsString = request.getUserToken().getProjectId();
    var projectId = UUID.fromString(projectIdAsString);

    var result = usersProvider.search(projectId);

    // does the requestor is also use about the requested set of user form the same
    // project?
    var email = ValEmail.of(emailAsString);
    var authorized = !result.map(it -> Objects.equals(it, email)).isEmpty();
    var asDto = authorized ? toDto(result) : List.<UsersSearchModel>of();

    return SearchReply.newBuilder()
        .addAllItems(asDto)
        .build();
  }

}
