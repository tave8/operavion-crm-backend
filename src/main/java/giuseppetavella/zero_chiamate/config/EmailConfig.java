package giuseppetavella.zero_chiamate.config;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfig {

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


}
