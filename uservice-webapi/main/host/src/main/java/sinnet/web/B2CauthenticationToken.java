package sinnet.web;

import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import lombok.Getter;

/** Fixme. */
public final class B2CauthenticationToken extends AbstractAuthenticationToken {

  @Getter
  private String email;

  /**
   * Fixme.
   *
   * @param accountId fixme
   * @param email fixme
   */
  B2CauthenticationToken(String accountId, String email, String name, boolean newUser) {
    super(Collections.emptyList());
    this.email = email;
    this.setAuthenticated(true);
  }

  private static final long serialVersionUID = 3717651672219649325L;

  @Override
  public Object getCredentials() {
    return null;
  }

  /** Contains primary email of the user. */
  @Override
  public String getPrincipal() {
    return email;
  }
}
