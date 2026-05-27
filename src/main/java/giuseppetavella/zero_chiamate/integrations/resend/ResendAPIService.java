package giuseppetavella.zero_chiamate.integrations.resend;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import giuseppetavella.zero_chiamate.exceptions.EmailSendingException;
import giuseppetavella.zero_chiamate.infrastructure.template.exceptions.TemplateException;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResendAPIService {

    // Email API
    @Autowired
    private Resend resend;

    // Email API-specific options/params builder 
    @Autowired
    private CreateEmailOptions.Builder defaultParams;



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

            // this is where we send the email with the API,
            // and where the "journey" of email sending ends for our system
            CreateEmailResponse data = resend.emails().send(params);
            
            return data.getId();

        } catch (ResendException e) {
        
            throw new EmailSendingException(e.getMessage());
        
        }
    }




    /**
     * Build the email params.
     * Can add attachments.
     * API-specific.
     */
    private CreateEmailOptions buildEmailParams(String recipient,
                                                String subject,
                                                String html,
                                                List<Attachment> attachments) throws TemplateException
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
