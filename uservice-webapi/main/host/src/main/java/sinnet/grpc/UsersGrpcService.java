package sinnet.grpc;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import sinnet.grpc.users.UsersGrpc.UsersBlockingStub;

/** Mockable equivalent of {@link UsersBlockingStub}. */
@Component
@RequiredArgsConstructor
public class UsersGrpcService {

  private interface UsersService {

    sinnet.grpc.users.SearchReply search(sinnet.grpc.users.SearchRequest request);

  }

  @Delegate(types = UsersService.class)
  private final UsersBlockingStub stub;

}
