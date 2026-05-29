package giuseppetavella.zero_chiamate.infrastructure.email;

import giuseppetavella.zero_chiamate.config.EmailTemplate;
import giuseppetavella.zero_chiamate.infrastructure.email.params.EmailParams;
import giuseppetavella.zero_chiamate.infrastructure.email.params.EmailTemplateParams;
import giuseppetavella.zero_chiamate.infrastructure.email.params.TestEmailParams;
import giuseppetavella.zero_chiamate.infrastructure.template.exceptions.TemplateException;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.exceptions.EmailSendingException;
import giuseppetavella.zero_chiamate.infrastructure.template.TemplateService;
import giuseppetavella.zero_chiamate.integrations.resend.ResendAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Send emails.
 * Hides the email API library-specific implementation details.
 */
@Service
public class EmailService {
    
    @Autowired
    private ResendAPIService resendAPIService;
    
    @Autowired
    private TemplateService templateService;
    

    /**
     * Send an email.
     * Many attachments.
     * 
     * @throws EmailSendingException if any problem occurred during email sending
     */
    public String send(EmailParams params) throws EmailSendingException 
    {
        
        // TODO: rate limit emails. max 5 emails per second based on Resend API limit
        
        // check that the email is a valid email
        ValidationHelper.requireValidEmailElseThrow(
                params.recipient(),
                () -> new EmailSendingException("Before sending an email, "
                                                +"recipient email is not valid. "
                                                +"Email was '" + params.recipient()+ "'. ")
        );
        
        // recipient and subject cannot be empty
        ValidationHelper.requireStringNotBlankElseThrow(
                params.subject(),
                () -> new EmailSendingException("Email subject cannot be empty")
        );
        
        // html cannot be empty
        ValidationHelper.requireStringNotBlankElseThrow(
                params.htmlBody(), 
                () -> new EmailSendingException("Html body cannot be empty")
        );
        
       return resendAPIService.sendEmail(params); 
       
    }

    /**
     * Send test email to developer.
     * Useful for protopying.
     * 
     * @param params
     * @return
     */
    public String send(TestEmailParams params) {
        return send(new EmailParams(
                params.recipient(),
                params.subject(),
                params.htmlBody(),
                params.attachments()
        ));
    }
     
    /**
     * Send email from a HTML template.
     *
     */
    public String sendTemplate(EmailTemplateParams tParams) 
    {
        
        var htmlBody = templateService.fillTemplate(
                tParams.template(),
                tParams.templateVars()
        );
        
        var params = new EmailParams(
                tParams.recipient(),
                tParams.subject(),
                htmlBody,
                tParams.attachments()
        );
        
        return send(params);
        
    }

    
    /**
     * Send email to developer.
     * Useful for experiments.
     * @return
     */
    public String sendTestEmail(EmailParams params) {
        return send(params);
    }
    
    
    // public String sendEmailFromTemplateWithDefaultLanguage(String template,
    //                                                 Map<String, Object> vars,
    //                                                 String recipient,
    //                                                 String subject) throws HtmlTemplateException
    // {
    //
    //     return this.sendEmailFromTemplate(template, vars, recipient, subject, List.of());
    //
    // }

    // public String sendEmailFromTemplateWithLanguage(String templateAfterLanguage,
    //                                                Map<String, Object> vars,
    //                                                String recipient,
    //                                                String subject) throws HtmlTemplateException
    // {
    //
    //     String lang = LanguageHelper.getLanguage().getValue();
    //    
    //     String templatePathWithLanguage = lang + "/" + templateAfterLanguage;
    //    
    //     return this.sendEmailFromTemplate(templatePathWithLanguage, vars, recipient, subject, List.of());
    //
    // }
    
 

}
