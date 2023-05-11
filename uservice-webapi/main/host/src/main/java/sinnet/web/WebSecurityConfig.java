package sinnet.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

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
}
