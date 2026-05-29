package giuseppetavella.zero_chiamate.infrastructure.email.params;

import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;

import java.util.List;

public record EmailParams(
        String recipient,
        String subject,
        String htmlBody,
        List<EmailAttachment> attachments
) {
    
    public EmailParams(String recipient,
                       String subject,
                       String htmlBody,
                       EmailAttachment attachment) 
    {
        this(recipient, subject, htmlBody, List.of(attachment));    
    }

    public EmailParams(String recipient,
                       String subject,
                       String htmlBody)
    {
        this(recipient, subject, htmlBody, List.of());
    }


}
