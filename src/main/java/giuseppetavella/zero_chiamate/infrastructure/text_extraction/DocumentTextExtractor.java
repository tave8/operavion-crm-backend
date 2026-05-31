package giuseppetavella.zero_chiamate.infrastructure.text_extraction;

import giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions.DocumentEmptyTextExtractionException;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions.TikaAPIException;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentTextExtractor {

    @Autowired
    private TikaExtractor tikaExtractor;
    
    
    /**
     * Extract plain text from a document (PDF, DOCX, XLSX, TXT, CSV).
     * Use this if you want to customize handling when extraction has no text.
     *
     * @param docBytes the document bytes
     * @param maxChars max number of chars to read from document
     * 
     * @return the extracted plain text
     * 
     * @throws InvalidDataException if document is not a text or pdf file
     * @throws TikaAPIException if error during text extraction
     */
    public @NonNull String extract(byte[] docBytes, int maxChars) 
    {
        // check that this is a text or pdf file 
        ValidationHelper.requireFileTextOrPdfElseThrow(
                docBytes,
                () -> new InvalidDataException(
                        "Document to extract is not text nor pdf. "
                        +"Mime type is '" + FileHelper.getMimeType(docBytes) + "' instead."
                )
        );
        
        // extract text using API
        return tikaExtractor.bytesToText(docBytes, maxChars);

    }

    /**
     * Extract the text from document AND require that
     * the document actually contains some text (non-empty).
     * Use this if you want error thrown when document has no text.
     * 
     * @throws InvalidDataException if document is not a text or pdf file
     * @throws DocumentEmptyTextExtractionException if document had no text
     *   or was not truly a text/pdf file (for example a scanned pdf or image converted to pdf)
     * @throws TikaAPIException if error during text extraction
     */
    public @NonNull String extractAndRequireNonEmpty(byte[] docBytes, int maxChars) {
        
        var extracted = extract(docBytes, maxChars);
        
        // if there's no result
        if(extracted.isEmpty())  {
            throw new DocumentEmptyTextExtractionException(
                    "Document to extract was a text or pdf file, but text extracted was empty. Likely causes: "
                            +"1) document was an image converted to pdf "
                            +"2) document contained text but had no text. "
                            +"Mime type is '" + FileHelper.getMimeType(docBytes) + "'. "
                            +"Provide a valid document that contains text."
            );
        }
        
        return extracted;
    }
    
    public @NonNull String extractAndRequireNonEmpty(byte[] docBytes) {
        return extractAndRequireNonEmpty(docBytes, -1);
    }


    /*
    * Read the whole file.
    * */
    public @NonNull String extract(byte[] docBytes) {
        return extract(docBytes, -1);
    }
    

}