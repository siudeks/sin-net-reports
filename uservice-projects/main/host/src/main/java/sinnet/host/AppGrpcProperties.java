package sinnet.host;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** Configuration properties for the application related to grpc functionality. */
@ConfigurationProperties(prefix = "grpc")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppGrpcProperties {
  int serverPort;
}
