package sinnet.web;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import sinnet.Profiles;

/** Fixme. */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  /**
   * Configures security for 'prod' profile'.
   *
   * @return the configurer
   */
  @Bean
  public SecurityFilterChain webSecurityForProdProfile(HttpSecurity http) throws Exception {
    http
        .cors()
        .and().csrf().disable()
        .httpBasic().disable()
        .authorizeHttpRequests(conf -> conf
            .requestMatchers("/graphiql/**").permitAll()
            .requestMatchers("/actuator").permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new AuthenticationConverter())))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
            .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

    return http.build();
  }

  /**
   * For test purpose we would like to produce jwt token expected by application logic, and dictated by http request
   * created only to satisfy test scenarios.
   * Inspired by https://www.baeldung.com/spring-security-oauth-jwt
   */
  @Bean
  @Profile(Profiles.TEST)
  public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {

    // // we expect to have in http a header named 'ONLEX_TEST_USER' in form of json
    // // { email: 'some@email' }
    // var expectedHeader = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    // var declaredUserEmail = expectedHeader.getAttribute("ONLEX_TEST_USER", 0);

    // var validFrom = Instant.now();
    // var validTo = validFrom.plusSeconds(42);
    // var genericJwt = new Jwt("ignored", validFrom, validTo, Map.of(), Map.of(
    //     "name", declaredUserEmail,
    //     "emails", List.of(declaredUserEmail)
    // ));
    return token -> {
      var secret = "my super secret key to sign my dev JWT token";
      var originalKey = new SecretKeySpec(secret.getBytes(), "HS256");
      var decoder = NimbusJwtDecoder
          .withSecretKey(originalKey)
          .build();
      var jwt = decoder.decode(token);
      return jwt;
    };
  }
}
