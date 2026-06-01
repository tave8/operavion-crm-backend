package giuseppetavella.zero_chiamate.config;

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


@Configuration
@PropertySource("classpath:application.properties")
public class CloudflareR2APIConfig {


    /**
     * Cloudflare R2 is the file upload API.
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
    
}
