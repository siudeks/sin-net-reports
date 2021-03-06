package sinnet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/** Fixme. */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    /**
     * Configures security for 'prod' profile'.
     * @return the configurer
     */
    @Bean
    @Profile({ "prod", "dev" })
    public WebSecurityConfigurerAdapter webSecurityForProdProfile() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(final HttpSecurity http) throws Exception {
                http
                    .cors()
                    .and()
                        .csrf().disable()
                    .authorizeRequests(conf -> {
                            conf
                                .antMatchers("/graphql/**").hasAuthority("Actions.Read")
                                .antMatchers("/api/to-delete").permitAll()
                                // .antMatchers("/graphql/**").hasAuthority("SCOPE_Actions.Read")
                                .antMatchers("/actuator/**").permitAll(); })
                    .oauth2ResourceServer()
                        .jwt()
                        //.jwtAuthenticationConverter(new JwtAuthenticationConverter());
                        .jwtAuthenticationConverter(new AuthenticationConverter());
            }
        };
    }

    // /**
    //  * Configures security for 'dev' profile'.
    //  * @return the configurer
    //  */
    // @Bean
    // @Profile("dev")
    // public WebSecurityConfigurerAdapter webSecurityForDevProfile(@Autowired DevAuthenticationFilter authFilter) {

    //     return new WebSecurityConfigurerAdapter() {

    //         @Override
    //         protected void configure(final HttpSecurity http) throws Exception {
    //             http
    //                 .cors()
    //                 .and().csrf().disable()
    //                 .authorizeRequests().anyRequest().permitAll();
    //         }
    //     };
    // }
}
