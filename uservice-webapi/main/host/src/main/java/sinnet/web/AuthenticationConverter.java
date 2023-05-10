package sinnet.web;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import io.vavr.control.Try;

/** Fixme. */
public class AuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  /** {@inheritDoc} */
  @Override
  public AbstractAuthenticationToken convert(final Jwt source) {
    // both claims below are not availabe by default in B2C.
    // you have to configure user flows -> your sign in policy -> application claims
    // and select 'User's Object ID' and 'Email Addresses' and 'User is new'
    // more aboput B2C token:
    // https://docs.microsoft.com/en-us/azure/active-directory-b2c/tokens-overview#claims
    var accountId = source.getClaimAsString("oid");
    // claim 'newUser' is optional available only when iser is logging first time to the app
    var newUser = Optional
        .ofNullable(source.getClaimAsString("newUser"))
        .map(ignored -> source.getClaimAsBoolean("newUser"))
        .orElse(false);

    var name = source.getClaimAsString("name");
    return Try
        .of(() -> source.getClaimAsStringList("emails").get(0))
        .map(email -> new B2CauthenticationToken(accountId, email, name, newUser))
        .getOrElseThrow(() -> new IllegalArgumentException("JWT should contain 'emails' claim, required by the application."));
  }

}
