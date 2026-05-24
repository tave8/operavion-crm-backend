package giuseppetavella.zero_chiamate.config;

import giuseppetavella.zero_chiamate.exceptions.AppStartupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * Generic project configuration, such as:
 * - URLs per environment 
 * - ..
 */
@Configuration
@PropertySource("application.properties")
public class AppConfig {


    /**
     * The URL of this server.
     */
    @Bean(name = "serverUrl")
    public String getServerUrl(@Value("${server.url}") String serverUrl) {
        return serverUrl;
    }

    /**
     * The URL of that this server is "connected" to.
     * You can see this as the frontend URL that belongs 
     * to the same environment scope as the server:
     * 
     * local server -> local frontend
     * preview server -> preview frontend
     * production server -> production frontend
     */
    @Bean(name = "frontendUrl")
    public String getFrontendUrl(@Value("${whereami}") String whereami, 
                                 @Value("${frontend.production.url}") String frontendProductionUrl,
                                 @Value("${frontend.preview.url}") String frontendPreviewUrl,
                                 @Value("${frontend.local.url}") String frontendLocalUrl) 
    {
        if(whereami.equals("LOCAL")) {
            return frontendLocalUrl;
        }
        if(whereami.equals("PREVIEW")) {
            return frontendPreviewUrl;
        }
        if(whereami.equals("PRODUCTION")) {
            return frontendProductionUrl;
        }
        throw new AppStartupException("While loading env vars in a "
                                    +"Spring bean, the value '" + whereami+ "' for 'whereami' was not "
                                    +"found in the env vars related to frontend URL." );
    }
    

}