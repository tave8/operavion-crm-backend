package giuseppetavella.zero_chiamate.integrations.resend;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import giuseppetavella.zero_chiamate.exceptions.EmailSendingException;
import giuseppetavella.zero_chiamate.infrastructure.email.params.EmailParams;
import giuseppetavella.zero_chiamate.infrastructure.template.exceptions.TemplateException;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.integrations.resend.exceptions.ResendAPIException;
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
    
    private static final String REPLY_TO_EMAIL = "giuseppetavella8@gmail.com";



    /**
     * Send an email.
     *
     */
    public String sendEmail(EmailParams params)
    {
        
        // these are the API-specific attachments
        // we translate from API-independent to API-specific
        var apiAttachments = toAPIAttachments(params.attachments());
        
        var apiParams = buildEmailParams(
                params.recipient(),
                params.subject(),
                params.htmlBody(),
                apiAttachments
        );

        try {

            // this is where we send the email with the API,
            // and where the "journey" of email sending ends for our system
            var data = resend.emails().send(apiParams);
            
            return data.getId();

        } catch (ResendException e) {
        
            throw new ResendAPIException(e.getMessage());
        
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
                                                List<Attachment> attachments) 
    {
        return defaultParams
                    .to(recipient)
                    .subject(subject)
                    .html(html)
                    .attachments(attachments)
                    // for now i get the response
                    .replyTo(REPLY_TO_EMAIL)
                    .build();
    }

    


    /**
     * Turn a list of app attachments, to API-specific attachments.
     * (adapter/translation layer)
     */
    private List<Attachment> toAPIAttachments(List<EmailAttachment> emailAttachments)
    {
        List<Attachment> attachments = new ArrayList<>();

        for(var emailAttachment : emailAttachments) {
            // library-specific object
            var attachment = Attachment.builder()
                                    .fileName(emailAttachment.getFilename())
                                    .content(emailAttachment.getBase64Content())
                                    .build();

            attachments.add(attachment);
        }

        return attachments;
    }


}
