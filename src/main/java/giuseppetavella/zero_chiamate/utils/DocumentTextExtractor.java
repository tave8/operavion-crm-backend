package giuseppetavella.zero_chiamate.utils;

import giuseppetavella.zero_chiamate.exceptions.FileException;
import org.apache.tika.Tika;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class DocumentTextExtractor {

    private final Tika tika = new Tika();

    /**
     * 
     * Extract plain text from a document (PDF, DOCX, XLSX, TXT...).
     * Deterministic, no AI. Reads the document's text layer via Apache Tika,
     * which selects the right parser based on the file content.
     * 
     * Simply call <code>extractedText.isEmpty()</code> at call site,
     *
     * Note: scanned/image-only PDFs have no text layer, so this returns an
     * empty (or near-empty) string for them. Use the AI vision path for those.
     *
     * @param docBytes the document bytes
     * @return the extracted plain text
     * @throws FileException if the text cannot be extracted
     */
    public @NonNull String bytesToText(byte[] docBytes)
    {
        try {

            // -1 = no length limit; parseToString otherwise truncates at 100k chars
            tika.setMaxStringLength(-1);

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(docBytes)) {

                var result = tika.parseToString(inputStream);

                // avoids having to check for null at call site
                if(result == null) {
                    return "";
                }
                
                // avoids having to remember difference 
                // if string is empty or blank at call site
                if(result.isBlank()) {
                    return "";
                }
                
                return result;
                
            }

        } catch (Exception ex) {

            throw new FileException("Error while extracting text from a document. DETAILS: " + ex.getMessage());

        }

    }

}