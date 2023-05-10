package sinnet.grpc;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import sinnet.grpc.customers.CustomersGrpc.CustomersBlockingStub;
import sinnet.grpc.customers.GetReply;
import sinnet.grpc.customers.GetRequest;
import sinnet.grpc.customers.ListReply;
import sinnet.grpc.customers.ListRequest;
import sinnet.grpc.customers.RemoveReply;
import sinnet.grpc.customers.RemoveRequest;
import sinnet.grpc.customers.ReserveReply;
import sinnet.grpc.customers.ReserveRequest;
import sinnet.grpc.customers.UpdateCommand;
import sinnet.grpc.customers.UpdateResult;

/** Mockable equivalent of {@link ProjectsGrpcStub}. */
@Component
@RequiredArgsConstructor
public class CustomersGrpcService {

  private interface CustomersService {

    ListReply list(ListRequest request);
    
    GetReply get(GetRequest request);

    ReserveReply reserve(ReserveRequest request);

    RemoveReply remove(RemoveRequest request);

    UpdateResult update(UpdateCommand request);
  }

  @Delegate(types = CustomersService.class)
  private final CustomersBlockingStub stub;
}
