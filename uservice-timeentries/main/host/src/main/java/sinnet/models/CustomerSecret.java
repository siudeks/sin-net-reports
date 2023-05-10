package sinnet.models;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TBD.
 */
@Data
@Accessors(chain = true)
public final class CustomerSecret {
  private String location;
  private String username;
  private String password;
  private ValEmail changedWho = ValEmail.empty();
  private LocalDateTime changedWhen;
}
