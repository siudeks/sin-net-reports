package sinnet.models;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;
import sinnet.domain.model.ValEmail;

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
  private String otpSecret;
  private String otpRecoveryKeys;
}
