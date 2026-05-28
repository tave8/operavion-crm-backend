package giuseppetavella.zero_chiamate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    private AppEnvironment appEnvironment;
    
    /**
     * CORS
     * 
     * Add/edit the values of the returned list,
     * to automatically configure CORS for these origins.
     * 
      * @return
     */ 
    @Bean(name = "allowedOrigins")
    public List<String> getAllowedOrigins(@Value("${frontend.production.url}") String frontendProductionUrl,
                                          @Value("${frontend.preview.url-pattern}") String frontendPreviewUrlPattern,
                                          @Value("${frontend.local.url}") String frontendLocalUrl)
    {
        
        
        return List.of(
                // frontend: production
                frontendProductionUrl,
                // frontend: preview
                frontendPreviewUrlPattern,
                // frontend: local
                frontendLocalUrl
                
                // add more origins here...
                
        );
        
    }
    

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


    /**
     * BCrypt.
     *  
     * @return
     */
    @Bean
    public PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(12);
    }

    
    /**
     * Configure CORS.
     * 
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Qualifier("allowedOrigins") List<String> allowedOrigins) 
    {
        
        CorsConfiguration configuration = new CorsConfiguration();

        // Here we define a whitelist  of allowed origins
        configuration.setAllowedOriginPatterns(allowedOrigins);

        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        // Cache preflight (OPTIONS) responses for 6 hours.
        // Without this, the browser sends a preflight before every request.
        configuration.setMaxAge(21600L); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }


}