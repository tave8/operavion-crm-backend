package giuseppetavella.zero_chiamate.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
// this annotation allows us to customize Spring Security
@EnableWebSecurity
// i need this annotation to enable authorization check on all endpoints, 
// through the annotation @PreAuthorization
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * In this bean we can customize and override default
     * Spring Security behavior.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        // System.out.println("security filter chain called!");
        
        // disable session management, because with JWT we use a stateless approach
        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.formLogin(formLogin -> formLogin.disable());

        // disable protection from CSRF
        httpSecurity.csrf(csrf -> csrf.disable());

        // disable authentication on all endpoints, because we are going
        // to define that ourselves
        httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());

        // we must activate the cors configuration
        httpSecurity.cors(Customizer.withDefaults());

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Value("${frontend.production.url}") String frontendProductionUrl,
                                                           @Value("${frontend.preview.url-pattern}") String frontendPreviewUrlPattern,
                                                           @Value("${frontend.local.url}") String frontendLocalUrl) 
    {

        CorsConfiguration configuration = new CorsConfiguration();

        // Here we define a whitelist  of allowed origins
        configuration.setAllowedOriginPatterns(List.of(
                // frontend: production
                frontendProductionUrl,
                // frontend: preview
                frontendPreviewUrlPattern,
                // frontend: local
                frontendLocalUrl
        ));
        

        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }


}