package giuseppetavella.zero_chiamate.api;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.csv.Csv;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Pre-made response entities that help 
 * with sending attachments (pdf, csv).
 * 
 * The browser will trigger a download of the attachment.
 */
public class AttachmentResponseBuilder {

    public static ResponseEntity<byte[]> anyFile(byte[] bytes, String filename) {
        
        var mimeType = FileHelper.getMimeType(bytes, filename);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(mimeType))
                .body(bytes);
    }
    
    public static ResponseEntity<byte[]> csv(Csv csv, String filenameWithoutExt) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filenameWithoutExt + ".csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.toBytes());
    }

    public static ResponseEntity<byte[]> pdf(Pdf pdf, String filenameWithoutExt) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filenameWithoutExt + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.toBytes());
    }
}
