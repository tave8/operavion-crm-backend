package giuseppetavella.zero_chiamate.infrastructure.text_extraction;

import giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions.TikaAPIException;
import org.apache.tika.Tika;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class TikaExtractor {

    private final Tika tika = new Tika();

    /**
     *
     * Extract plain text from a document (PDF, DOCX, XLSX, TXT, CSV).
     * Reads the document's text layer via Apache Tika.
     *
     * Note: scanned/image-only PDFs have no text layer, so this returns 
     * an empty string for them. 
     *
     * @param docBytes the document bytes
     * @param maxChars max number of chars to read from document
     *                 
     * @return the extracted text. If no text was found, an empty string is returned. 
     *     This way, caller can simply check .isEmpty() and decide how to interpret it.
     *     
     * @throws TikaAPIException if any error during extraction, 
     *  we assume it's the Tika library's error
     */
    public @NonNull String bytesToText(byte[] docBytes, int maxChars)
    {

        try {

            // max chars to read from document
            tika.setMaxStringLength(maxChars);

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(docBytes)) {

                var extracted = tika.parseToString(inputStream);

                var isEmpty = extracted == null || extracted.isBlank();

                // this way caller doesn't have to distinguish between null or blank,
                // caller can just check .isEmpty() and decide
                if(isEmpty) {
                    return "";
                }

                return extracted;

            }

        } 
        // API exception
        catch (Exception ex) {

            throw new TikaAPIException(ex.getMessage());

        }

    }
    

}
