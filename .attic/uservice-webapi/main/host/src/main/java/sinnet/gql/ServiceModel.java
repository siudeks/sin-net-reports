package sinnet.gql;

import java.time.LocalDate;

import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Ignore;
import org.eclipse.microprofile.graphql.NonNull;

import lombok.Data;
import sinnet.grpc.common.UserToken;

@Data
public class ServiceModel {
  private @NonNull @Id String projectId;
  private @NonNull @Id String entityId;
  private @NonNull long entityVersion;
  private String servicemanEmail;
  private String servicemanName;
  private @NonNull LocalDate whenProvided;
  private String description;
  private Integer distance;
  private Integer duration;

  @Ignore
  private String customerId;
  @Ignore
  private UserToken userToken;
}
