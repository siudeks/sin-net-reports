package sinnet;

import com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** Fixme. */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /** Fixme. */
    @Autowired
    private AADAuthenticationFilter aadAuthFilter;

    /** Fixme. */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http
            .cors()
            .and()
                .csrf().disable()
            .authorizeRequests()
            .antMatchers("/graphql/**")
            .authenticated()
            .anyRequest().permitAll()
            .and()
                .headers().frameOptions().disable();

            http.addFilterBefore(aadAuthFilter,
                                 UsernamePasswordAuthenticationFilter.class);
    }
}
