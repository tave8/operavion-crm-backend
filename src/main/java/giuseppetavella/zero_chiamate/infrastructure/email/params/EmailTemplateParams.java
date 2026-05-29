package giuseppetavella.zero_chiamate.infrastructure.email.params;

import giuseppetavella.zero_chiamate.config.EmailTemplate;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;

import java.util.List;
import java.util.Map;

public record EmailTemplateParams(
        EmailTemplate template,
        Map<String, Object> templateVars,
        String recipient,
        String subject,
        List<EmailAttachment> attachments
) {
    
    public EmailTemplateParams(EmailTemplate template,
                               Map<String, Object> templateVars,
                               String recipient,
                               String subject,
                               EmailAttachment attachment) 
    {
        this(template, templateVars, recipient, subject, List.of(attachment));
    }

    public EmailTemplateParams(EmailTemplate template,
                               Map<String, Object> templateVars,
                               String recipient,
                               String subject)
    {
        this(template, templateVars, recipient, subject, List.of());
    }
    
}
