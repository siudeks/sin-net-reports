package sinnet.grpc;


import java.util.UUID;

import lombok.Data;

/**
 * TBD.
 */
@Data
public class MyEntity {
  private UUID projectId;
  private UUID entityId;
  private int entityVersion;
}
