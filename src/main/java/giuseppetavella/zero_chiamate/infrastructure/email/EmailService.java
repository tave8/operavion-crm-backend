package giuseppetavella.zero_chiamate.infrastructure.email;

import giuseppetavella.zero_chiamate.domain.business.Template;
import giuseppetavella.zero_chiamate.exceptions.TemplateException;
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
    public String sendEmail(String recipient, 
                            String subject, 
                            String html,
                            List<EmailAttachment> attachments) throws EmailSendingException 
    {
        
        // TODO: rate limit emails. max 5 emails per second based on Resend API limit
        
        // check that the email is a valid email
        ValidationHelper.requireValidEmailElseThrow(
                recipient,
                () -> new EmailSendingException("Before sending an email, "
                                                +"recipient email is not valid. "
                                                +"Email was '" + recipient+ "'. ")
        );
        
        // recipient and subject cannot be empty
        ValidationHelper.requireStringNotBlankElseThrow(
                subject,
                () -> new EmailSendingException("Email subject cannot be empty")
        );
        
        // html cannot be empty
        ValidationHelper.requireStringNotBlankElseThrow(
                html, 
                () -> new EmailSendingException("Html body cannot be empty")
        );
        
       return resendAPIService.sendEmail(
               recipient,
               subject,
               html,
               attachments
       ); 
       
    }

    /**
     * Send an email.
     * One attachment.
     */
    public String sendEmail(String recipient,
                               String subject,
                               String html,
                               EmailAttachment attachment) throws EmailSendingException
    {

        return this.sendEmail(recipient, subject, html, List.of(attachment));
    }

    
    /**
     * Send an email.
     * No attachments.
     */
    public String sendEmail(String recipient,
                            String subject,
                            String html) throws EmailSendingException
    {
        return this.sendEmail(recipient, subject, html, List.of());
    }

     
    /**
     * Send email from a HTML template.
     * Many attachments.
     * 
     * @throws TemplateException if input template is not found
     */
    public String sendEmailFromTemplate(Template template,
                                        Map<String, Object> vars,
                                        String recipient,
                                        String subject,
                                        List<EmailAttachment> attachments) 
    {
        
        String html = this.templateService.fillTemplate(template, vars);
        
        return this.sendEmail(recipient, subject, html, attachments);
        
    }

    /**
     * Send email from a HTML template.
     * One attachment.
     * 
     * @throws TemplateException if input template is not found
     */
    public String sendEmailFromTemplate(Template template,
                                        Map<String, Object> vars,
                                        String recipient,
                                        String subject,
                                        EmailAttachment attachment) throws TemplateException
    {

        return this.sendEmailFromTemplate(template, vars, recipient, subject, List.of(attachment));

    }



    /**
     * Send email from a HTML template.
     * No attachments.
     * 
     * @throws TemplateException if input template is not found
     */
    public String sendEmailFromTemplate(Template template,
                                        Map<String, Object> vars,
                                        String recipient,
                                        String subject) throws TemplateException
    {

        return this.sendEmailFromTemplate(template, vars, recipient, subject, List.of());

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
