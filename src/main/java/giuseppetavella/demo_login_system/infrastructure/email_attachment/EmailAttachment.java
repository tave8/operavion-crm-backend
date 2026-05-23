package giuseppetavella.demo_login_system.infrastructure.email_attachment;

import giuseppetavella.demo_login_system.exceptions.FileException;
import giuseppetavella.demo_login_system.helpers.FileHelper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class EmailAttachment {
    
    // base64-encoded file
    private final String base64Content;
    
    // this is what is shown in the email
    private final String filename;
    
    public EmailAttachment(String base64Content, String attachmentFilename) {
        this.base64Content = base64Content;
        this.filename = attachmentFilename;
    }

    public EmailAttachment(byte[] bytes, String attachmentFilename) {
        this(FileHelper.toBase64(bytes), attachmentFilename);
    }

    public EmailAttachment(MultipartFile file, String attachmentFilename) throws FileException
    {
        this(EmailAttachment.getBytes(file), attachmentFilename);
    }
    
    public String getBase64Content() {
        return base64Content;
    }

    public String getFilename() {
        return filename;
    }
    
    private static byte[] getBytes(MultipartFile file) throws FileException
    {
        try {
            return file.getBytes();
        } catch (IOException ex) {
            throw new FileException(ex.getMessage());
        }
    }
}
