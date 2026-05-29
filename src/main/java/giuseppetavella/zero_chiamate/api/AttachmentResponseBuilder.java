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
        return download(bytes, filename, mimeType);
    }

    public static ResponseEntity<byte[]> csv(Csv csv, String filenameWithoutExt) {
        return download(csv.toBytes(), filenameWithoutExt + ".csv", "text/csv");
    }

    public static ResponseEntity<byte[]> pdf(Pdf pdf, String filenameWithoutExt) {
        return download(pdf.toBytes(), filenameWithoutExt + ".pdf", "application/pdf");
    }

    // --- Private ---

    private static ResponseEntity<byte[]> download(byte[] bytes, String filename, String mimeType) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(mimeType))
                .body(bytes);
    }
}
