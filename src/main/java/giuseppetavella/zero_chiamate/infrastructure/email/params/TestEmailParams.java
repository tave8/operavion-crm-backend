package giuseppetavella.zero_chiamate.infrastructure.email.params;

import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;

import java.util.List;

public record TestEmailParams(
        String recipient,
        String subject,
        String htmlBody,
        List<EmailAttachment> attachments
) {

    public TestEmailParams(EmailAttachment attachment)
    {
        this("giuseppetavella8@gmail.com", "Test email", "Test email", List.of(attachment));
    }

    public TestEmailParams()
    {
        this("giuseppetavella8@gmail.com", "Test email", "Test email", List.of());
        
    }


}