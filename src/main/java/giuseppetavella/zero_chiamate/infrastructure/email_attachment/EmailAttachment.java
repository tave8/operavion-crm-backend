package giuseppetavella.zero_chiamate.infrastructure.email_attachment;

import giuseppetavella.zero_chiamate.exceptions.FileException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.csv.Csv;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import org.jspecify.annotations.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class EmailAttachment {
    
    // base64-encoded file
    private final String base64Content;
    // this is what is shown in the email
    private final String filename;
    
    public EmailAttachment(String base64Content, String filename) {
        this.base64Content = base64Content;
        this.filename = filename;
    }

    public EmailAttachment(byte[] bytes, String filename) {
        this(FileHelper.toBase64(bytes), filename);
    }
    
    public EmailAttachment(@NonNull MultipartFile file, String filename) throws FileException
    {
        this(FileHelper.getBytes(file), filename);
    }
    
    public EmailAttachment(@NonNull Csv csv, String filenameWithoutExt) {
        this(csv.toAttachment(), filenameWithoutExt + ".csv");
    }

    public EmailAttachment(@NonNull Pdf pdf, String filenameWithoutExt) {
        this(pdf.toAttachment(), filenameWithoutExt + ".pdf");
    }

    
    public String getBase64Content() {
        return base64Content;
    }

    public String getFilename() {
        return filename;
    }
    
}
