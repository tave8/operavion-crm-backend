package giuseppetavella.zero_chiamate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for API: Geoapify.
 */
@Configuration
public class GeoapifyAPIConfig {
    
    @Bean(name = "geoapifyAPIkey")
    public String getGeoapifyAPIKey(@Value("${geoapify-apikey}") String apiKey) {
        return apiKey;
    }
    
}
