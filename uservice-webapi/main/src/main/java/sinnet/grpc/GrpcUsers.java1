package sinnet.grpc;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import io.grpc.Metadata;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import sinnet.grpc.users.Users;

@ApplicationScoped
@RequiredArgsConstructor
public class GrpcUsers{

 
  private final @GrpcClient("uservice-activities-config") Users service;

  @Delegate
  private Users interceptedService;

  @PostConstruct
  void init() {
    var extraHeaders = new Metadata();
    var key = Metadata.Key.of("dapr-app-id", Metadata.ASCII_STRING_MARSHALLER);
    extraHeaders.put(key, "activities-app-id");
    
    interceptedService = GrpcClientUtils.attachHeaders(service, extraHeaders);
  }
}