package giuseppetavella.zero_chiamate.infrastructure.email_attachment;

import giuseppetavella.zero_chiamate.exceptions.FileException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.csv.Csv;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import org.jspecify.annotations.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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

    /**
     * The file extension will be automatically determined.
     * @param bytes
     */
    public EmailAttachment(byte[] bytes) {
        this(bytes, UUID.randomUUID() + "." + FileHelper.getFileType(bytes));
    }
    
    
    public EmailAttachment(@NonNull MultipartFile file, String filename)
    {
        this(FileHelper.getBytes(file), filename);
    }


    public EmailAttachment(@NonNull MultipartFile file)
    {
        this(file, UUID.randomUUID() + "." + FileHelper.getFileType(file));
    }
    
    public EmailAttachment(@NonNull Csv csv, String filenameNoExt) {
        this(csv.toAttachment(), filenameNoExt + ".csv");
    }
    
    public EmailAttachment(@NonNull Csv csv) {
        this(csv, UUID.randomUUID() + ".csv");
    }
    
    public EmailAttachment(@NonNull Pdf pdf, String filenameNoExt) {
        this(pdf.toAttachment(), filenameNoExt + ".pdf");
    }

    public EmailAttachment(@NonNull Pdf pdf) {
        this(pdf, UUID.randomUUID() + ".pdf");
    }


    public String getBase64Content() {
        return base64Content;
    }

    public String getFilename() {
        return filename;
    }
    
}
