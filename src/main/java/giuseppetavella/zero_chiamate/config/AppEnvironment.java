package giuseppetavella.zero_chiamate.config;

import giuseppetavella.zero_chiamate.exceptions.AppConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppEnvironment {

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
}