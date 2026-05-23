package giuseppetavella.zero_chiamate.config;

import com.cloudinary.Cloudinary;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("application.properties")
public class Config {

    /**
     * HTTP client. 
     */
    // @Bean(name = "httpClient")
    // public OkHttpClient getHttpClient() {
    //     return new OkHttpClient();
    // }
    //
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
        throw new RuntimeException("While loading env vars in a "
                                    +"Spring bean, the value '" + whereami+ "' for 'whereami' was not "
                                    +"found in the env vars related to frontend URL." );
    }


    /**
     * Cloudinary is the media uploader.
     */
    @Bean
    public Cloudinary getFileUploader(
            @Value("${cloudinary.name}") String cloudName,
            @Value("${cloudinary.apikey}") String apiKey,
            @Value("${cloudinary.secret}") String apiSecret)
    {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }

    /**
     * Cloudflare R2 is the file uploader.
     */
    @Bean
    public S3Client getS3Client(
                @Value("${cloudflare.r2.access-key}") String cloudflareR2AccessKey,
                @Value("${cloudflare.r2.secret-key}") String cloudflareR2SecretKey,
                @Value("${cloudflare.r2.endpoint}") String cloudflareR2Endpoint) 
    {
        return S3Client.builder()
                .endpointOverride(URI.create(cloudflareR2Endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(cloudflareR2AccessKey, cloudflareR2SecretKey)))
                .region(Region.of("auto"))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
    }
    
    /**
     * The instance will be using to send emails.
     */
    @Bean 
    public Resend getEmailSender(
                @Value("${resend.apikey}") String resendApiKey) 
    {
        return new Resend(resendApiKey);    
    }

    /**
     * Default params for sending an email.
     * For example, a default domain, user and name.
     */
    @Bean
    public CreateEmailOptions.Builder getEmailSenderOptions(
            @Value("${email.sender-domain}") String domain,
            @Value("${email.sender-user}") String user,
            @Value("${email.sender-name}") String name)
    {
        // something like "info@mydomain.com"
        String senderAddress = user + "@" + domain;
        // something like "Giuseppe Tavella <info@mydomain.com>"
        String nameAndSenderAddress = name + " <" + senderAddress + ">";
        
        return CreateEmailOptions.builder().from(nameAndSenderAddress);
    }


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