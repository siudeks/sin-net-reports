package sinnet;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Non standard CORS configuration to allow connection between GraphQL client and out GraphQL client.
 */
@Configuration
public class AppCorsConfigurer {

    /**
     * Fixme.
     * @return fixme
    */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var conf = new CorsConfiguration();
        conf.setAllowedOrigins(Arrays.asList("http://localhost:3000",
                                             "https://raport.sin.net.pl"));
        conf.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
        conf.addAllowedHeader("*");
        conf.setAllowCredentials(true);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf);
        return source;
    }

}
