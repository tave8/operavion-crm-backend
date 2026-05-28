package giuseppetavella.zero_chiamate.config;

import giuseppetavella.zero_chiamate.helpers.HttpUrlHelper;
import giuseppetavella.zero_chiamate.helpers.UrlHelper;
import giuseppetavella.zero_chiamate.integrations.geoapify.exceptions.GeoapifyAPIException;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for API: Geoapify.
 */
@Configuration
public class GeoapifyAPIConfig {

    /**
     * Geoapify API geocoding url.
     * 
     * @return
     */
    @Bean(name = "geoapifyAPIGeocodingUrl")
    public String getGeoapifyAPIGeocodingUrl() {
        return "https://api.geoapify.com/v1/geocode/search";
    }
    
    
    /**
     * Geoapify API key.
     * 
     * @param apiKey
     * @return
     */
    @Bean(name = "geoapifyAPIkey")
    public String getGeoapifyAPIKey(
            @Value("${geoapify-apikey}") String apiKey) 
    {
        return apiKey;
    }
    
    
    
}
