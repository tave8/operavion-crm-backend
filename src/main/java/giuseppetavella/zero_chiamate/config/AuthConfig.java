package giuseppetavella.zero_chiamate.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration for Authentication.
 * 
 */
@Configuration
@PropertySource("application.properties")
public class AuthConfig {


    @Bean(name = "forgotPasswordSetPasswordTTL")
    public long getForgotPasswordSetPasswordTTL(
            @Qualifier("forgotPasswordTimeConstraintEnabled") boolean isTimeConstraintEnabled)
    {
        // how many minutes the user will have to set the new password,
        // once the authorization code has been generated
        if(isTimeConstraintEnabled) {
            return 10;
        }
        // notice that this must be bounded
        // this means: we have 1000 minutes to set a new password,
        // from when the code was generated
        return 1000;
    }


    @Bean(name = "forgotPasswordNextCodeWaitTime")
    public long getForgotPasswordNextCodeWaitTime(
            @Qualifier("forgotPasswordTimeConstraintEnabled") boolean isTimeConstraintEnabled)
    {
        // how many minutes the user will have to wait to 
        // request authorization to set a new password
        if(isTimeConstraintEnabled) {
            return 30;
        }
        // this means: we have rate limit on how many codes we can generate,
        // we don't have to wait at all
        return 0;
    }

    /**
     * Whether the time constraint is enabled, 
     * for the forgot password security mechanism.
     */
    @Bean(name = "forgotPasswordTimeConstraintEnabled")
    public boolean getForgotPasswordTimeConstraintEnabled(
            @Value("${FORGOT_PASSWORD_TIME_CONSTRAINT_ENABLED}") String timeConstraintEnabled)
    {
        if(timeConstraintEnabled.equals("true")) {
            return true;
        }
        if(timeConstraintEnabled.equals("false")) {
            return false;
        }
        throw new RuntimeException("While initializing a Spring bean, the value '"+timeConstraintEnabled+"' for env var known as "
                +"'forgot password time constraint enabled' is not recognized.");
    }
    
}
