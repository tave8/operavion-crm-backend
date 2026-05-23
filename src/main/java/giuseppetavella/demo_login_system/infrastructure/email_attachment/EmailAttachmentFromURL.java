package giuseppetavella.demo_login_system.infrastructure.email_attachment;

import giuseppetavella.demo_login_system.exceptions.FileDownloadException;
import giuseppetavella.demo_login_system.helpers.FileHelper;

public class EmailAttachmentFromURL extends EmailAttachment {
    public EmailAttachmentFromURL(String url, String attachmentFilename) throws FileDownloadException 
    {
        super(FileHelper.urlToBase64(url), attachmentFilename);
    }
}
