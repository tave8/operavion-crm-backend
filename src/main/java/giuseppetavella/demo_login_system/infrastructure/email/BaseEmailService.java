package giuseppetavella.demo_login_system.infrastructure.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import giuseppetavella.demo_login_system.exceptions.HtmlTemplateException;
import giuseppetavella.demo_login_system.helpers.LanguageHelper;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.demo_login_system.exceptions.EmailSendingException;
import giuseppetavella.demo_login_system.infrastructure.template.HtmlTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Send emails.
 * Hides the email API library-specific implementation details.
 */
@Service
public class BaseEmailService {
    
    // Email API
    @Autowired
    private Resend resend;
    
    // Email API-specific options/params builder 
    @Autowired
    private CreateEmailOptions.Builder defaultParams;
    
    @Autowired
    private HtmlTemplateService htmlTemplateService;
    

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
        
        // check that the email is a valid email
        StringHelper.requireValidEmailElseThrowWith(
                recipient, 
                "Before sending an email, recipient email is not valid. Email was '" + recipient+ "'. "
        );
        
        // these are the API-specific attachments
        // we translate from API-independent to API-specific
        List<Attachment> attachmentsForAPI = this.toAPIAttachments(attachments);
        
        CreateEmailOptions params = this.buildEmailParams(
                recipient, 
                subject, 
                html,
                attachmentsForAPI
        );
        
        try {
            
            CreateEmailResponse data = resend.emails().send(params);
            return data.getId();
            
        } catch (ResendException e) {
            throw new EmailSendingException(e.getMessage());
        }
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
     * @throws HtmlTemplateException if input template is not found
     */
    public String sendEmailFromTemplate(String template, 
                                        Map<String, Object> vars,
                                        String recipient,
                                        String subject,
                                        List<EmailAttachment> attachments) 
    {
        
        String html = this.htmlTemplateService.fillTemplate(template, vars);
        
        return this.sendEmail(recipient, subject, html, attachments);
        
    }

    /**
     * Send email from a HTML template.
     * One attachment.
     * 
     * @throws HtmlTemplateException if input template is not found
     */
    public String sendEmailFromTemplate(String template,
                                        Map<String, Object> vars,
                                        String recipient,
                                        String subject,
                                        EmailAttachment attachment) throws HtmlTemplateException
    {

        return this.sendEmailFromTemplate(template, vars, recipient, subject, List.of(attachment));

    }


    /**
     * Send email from a HTML template.
     * No attachments.
     * 
     * @throws HtmlTemplateException if input template is not found
     */
    public String sendEmailFromTemplate(String template,
                                        Map<String, Object> vars,
                                        String recipient,
                                        String subject) throws HtmlTemplateException
    {

        return this.sendEmailFromTemplate(template, vars, recipient, subject, List.of());

    }

    public String sendEmailFromTemplateWithDefaultLanguage(String template,
                                                    Map<String, Object> vars,
                                                    String recipient,
                                                    String subject) throws HtmlTemplateException
    {

        return this.sendEmailFromTemplate(template, vars, recipient, subject, List.of());

    }

    public String sendEmailFromTemplateWithLanguage(String templateAfterLanguage,
                                                   Map<String, Object> vars,
                                                   String recipient,
                                                   String subject) throws HtmlTemplateException
    {

        String lang = LanguageHelper.getLanguage().getValue();
        
        String templatePathWithLanguage = lang + "/" + templateAfterLanguage;
        
        return this.sendEmailFromTemplate(templatePathWithLanguage, vars, recipient, subject, List.of());

    }
    
    /**
     * Build the email params.
     * Can add attachments.
     * API-specific.
     */
    private CreateEmailOptions buildEmailParams(String recipient, 
                                               String subject, 
                                               String html,
                                               List<Attachment> attachments) throws HtmlTemplateException
    {
        return this.defaultParams
                .to(recipient)
                .subject(subject)
                .html(html)
                .attachments(attachments)
                // for now i get the response
                .replyTo("giuseppetavella8@gmail.com")
                .build();
    }

    
    /**
     * Build the email params.
     * No attachments.
     * API-specific.
     */
    private CreateEmailOptions buildEmailParams(String recipient,
                                               String subject,
                                               String html)
    {
        return this.buildEmailParams(recipient, subject, html, List.of());
    }


    /**
     * Turn a list of app attachments, to API-specific attachments.
     * (adapter/translation layer)
     */
    private List<Attachment> toAPIAttachments(List<EmailAttachment> emailAttachments) 
    {
        List<Attachment> attachments = new ArrayList<>();

        for(EmailAttachment emailAttachment : emailAttachments) {
            // library-specific object
            Attachment attachment = Attachment.builder()
                                        .fileName(emailAttachment.getFilename())
                                        .content(emailAttachment.getBase64Content())
                                        .build();

            attachments.add(attachment);
        }
        
        return attachments;
    }
    

}
