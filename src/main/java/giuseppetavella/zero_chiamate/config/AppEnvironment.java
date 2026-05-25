package giuseppetavella.zero_chiamate.config;

import giuseppetavella.zero_chiamate.exceptions.AppConfigurationException;
import giuseppetavella.zero_chiamate.exceptions.AppStartupException;
import giuseppetavella.zero_chiamate.exceptions.InvalidUrlException;
import giuseppetavella.zero_chiamate.helpers.DataValidationHelper;
import giuseppetavella.zero_chiamate.helpers.UrlHelper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppEnvironment {

    // all valid environments.
    // if "whereami" property is not set exactly to one of these values,
    // app will fail on startup
    private static final List<String> VALID_ENVIRONMENTS = List.of("LOCAL", "PREVIEW", "PRODUCTION");


    // all these attributes are injected via constructor
    private final String whereami;
    private final String serverUrl;
    private final String frontendProductionUrl;
    private final String frontendPreviewUrl;
    private final String frontendLocalUrl;

    public AppEnvironment(
            @Value("${whereami}") String whereami,
            @Value("${server.url}") String serverUrl,
            @Value("${frontend.production.url}") String frontendProductionUrl,
            @Value("${frontend.preview.url}") String frontendPreviewUrl,
            @Value("${frontend.local.url}") String frontendLocalUrl) throws InvalidUrlException
    {
        
        // validate the the URLs are valid
        DataValidationHelper.requireValidUrl(serverUrl);
        DataValidationHelper.requireValidUrl(frontendProductionUrl);
        DataValidationHelper.requireValidUrl(frontendPreviewUrl);
        DataValidationHelper.requireValidUrl(frontendLocalUrl);
        
        this.whereami = whereami;
        
        this.serverUrl = serverUrl;
        this.frontendProductionUrl = frontendProductionUrl;
        this.frontendPreviewUrl = frontendPreviewUrl;
        this.frontendLocalUrl = frontendLocalUrl;
    }
    

    public boolean isLocal() {
        return "LOCAL".equals(whereami);
    }

    public boolean isProduction() {
        return "PRODUCTION".equals(whereami);
    }

    public boolean isPreview() {
        return "PREVIEW".equals(whereami);
    }

    
    /**
     * Get the server URL based on current environment.
     * 
     * @return
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * Get the frontend URL based on current environment.
     * 
     * @return
     */
    public String getFrontendUrl() {
        if(isLocal()) {
            return frontendLocalUrl;
        }
        if(isPreview()) {
            return frontendPreviewUrl;
        }
        if(isProduction()){
            return frontendProductionUrl;
        }
        throw new AppConfigurationException("While getting frontend URL, "
                                            +"no matching environment was found.");
    }


    /**
     * Build the URL made of server URL (based on current env) + path. 
     * 
     * @param path
     * @return
     */
    public String buildServerUrl(String path) throws InvalidUrlException
    {
        return UrlHelper.buildUrl(
                getServerUrl(),
                path
        ); 
    }

    /**
     * Build the URL made of frontend URL (based on current env) + path. 
     * 
     * @param path
     * @return
     */
    public String buildFrontendUrl(String path) throws InvalidUrlException
    {
        return UrlHelper.buildUrl(
            getFrontendUrl(),
            path    
        );
        
    }
    
    
    /**
     * Get the current environment.
     * TODO: could be an enum, something like AppEnv.LOCAL, AppEnv.PRODUCTION etc.
     * 
     * @return
     */
    public String getEnv() {
        return whereami;
    }


    /**
     * Runs after bean is created.
     * Verifies that the "whereami" property has a legit value,
     * i.e. that we are in a legit environment. 
     * 
     */
    @PostConstruct
    public void validate() throws AppStartupException
    {
        if (!VALID_ENVIRONMENTS.contains(whereami)) {
            throw new AppStartupException(
                    "Invalid environment: '" + whereami + "'. " +
                            "Valid values are: " + VALID_ENVIRONMENTS + ". " +
                            "Check the 'whereami' property."
            );
        }
    }
    
}