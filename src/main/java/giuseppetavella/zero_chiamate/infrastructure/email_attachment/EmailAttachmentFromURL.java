package giuseppetavella.zero_chiamate.infrastructure.email_attachment;

import giuseppetavella.zero_chiamate.exceptions.FileDownloadException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;

public class EmailAttachmentFromURL extends EmailAttachment {
    public EmailAttachmentFromURL(String url, String attachmentFilename) throws FileDownloadException 
    {
        super(FileHelper.urlToBase64(url), attachmentFilename);
    }
}
