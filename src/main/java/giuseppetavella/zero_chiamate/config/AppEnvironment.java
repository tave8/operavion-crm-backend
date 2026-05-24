package giuseppetavella.zero_chiamate.config;

import giuseppetavella.zero_chiamate.exceptions.AppConfigurationException;
import giuseppetavella.zero_chiamate.exceptions.AppStartupException;
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
            @Value("${frontend.local.url}") String frontendLocalUrl) 
    {
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
    

    public String get() {
        return whereami;
    }


    /**
     * Runs after bean is created.
     * Verifies that the "whereami" property has a legit value,
     * i.e. that we are in a legit environment. 
     * 
     */
    @PostConstruct
    public void validate() {
        if (!VALID_ENVIRONMENTS.contains(whereami)) {
            throw new AppStartupException(
                    "Invalid environment: '" + whereami + "'. " +
                            "Valid values are: " + VALID_ENVIRONMENTS + ". " +
                            "Check the 'whereami' property."
            );
        }
    }
    
}