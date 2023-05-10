package sinnet.grpc;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import io.grpc.Metadata;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import sinnet.grpc.timeentries.TimeEntries;

@ApplicationScoped
@RequiredArgsConstructor
public class GrpcTimeEntries{

  private final @GrpcClient("uservice-activities-config") TimeEntries service;

  @Delegate
  private TimeEntries interceptedService;

  @PostConstruct
  void init() {
    var extraHeaders = new Metadata();
    var key = Metadata.Key.of("dapr-app-id", Metadata.ASCII_STRING_MARSHALLER);
    extraHeaders.put(key, "activities-app-id");
    
    interceptedService = GrpcClientUtils.attachHeaders(service, extraHeaders);
  }
}