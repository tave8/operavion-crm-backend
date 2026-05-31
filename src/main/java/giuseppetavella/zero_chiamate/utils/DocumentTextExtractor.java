package giuseppetavella.zero_chiamate.utils;

import giuseppetavella.zero_chiamate.exceptions.DocumentTextExtractionException;
import giuseppetavella.zero_chiamate.exceptions.FileException;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import org.apache.tika.Tika;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class DocumentTextExtractor {

    private final Tika tika = new Tika();

    /**
     * 
     * Extract plain text from a document (PDF, DOCX, XLSX, TXT, CSV).
     * Deterministic, no AI. Reads the document's text layer via Apache Tika,
     * which selects the right parser based on the file content.
     * 
     * This method will either give you a non-empty, non-null extracted text, or throw.
     *
     * Note: scanned/image-only PDFs have no text layer, so this returns an
     * empty (or near-empty) string for them. Use the AI vision path for those.
     *
     * @param docBytes the document bytes
     * @return the extracted plain text
     * @throws FileException if the text cannot be extracted
     */
    public @NonNull String bytesToText(byte[] docBytes, 
                                       int maxChars) throws FileException, 
                                                            InvalidDataException, 
                                                            DocumentTextExtractionException
    {
        
        // check that this is a text or pdf file 
        ValidationHelper.requireFileTextOrPdfElseThrow(
                docBytes,
                () -> new DocumentTextExtractionException(
                        "Document to extract is not text nor pdf. "
                        +"Mime type is '" + FileHelper.getMimeType(docBytes) + "' instead."
                )
        );
        
        
        try {

            tika.setMaxStringLength(maxChars);

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(docBytes)) {

                var result = tika.parseToString(inputStream);

                var isEmpty = result == null || result.isBlank();
                
                // if there's no result
                if(isEmpty) {
                    throw new DocumentTextExtractionException(
                            "Document to extract was a text or pdf file, but text extracted was empty. Likely causes: "
                            +"1) document was an image converted to pdf "
                            +"2) document contained text but had no text. "
                            +"Mime type is '" + FileHelper.getMimeType(docBytes) + "'. " 
                            +"Provide a valid document that contains text."
                    );
                }
                
                return result;
                
            }

        } catch (DocumentTextExtractionException e) {
            
            throw e;
            
        } catch (Exception ex) {

            throw new FileException("Error while extracting text from a document. DETAILS: " + ex.getMessage());

        }

    }


    /*
    * Read the whole file.
    * */
    public @NonNull String bytesToText(byte[] docBytes) {
        return bytesToText(docBytes, -1);
    }
    

}