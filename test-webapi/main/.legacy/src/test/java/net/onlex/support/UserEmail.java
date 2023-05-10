package net.onlex.support;

import lombok.EqualsAndHashCode;
import lombok.Value;

// Intentionally just name is used for equality as we assume in tests users with the same name represents in tests the same login
@Value
public class UserEmail {
  private String name;
  @EqualsAndHashCode.Exclude
  private String email;
}
