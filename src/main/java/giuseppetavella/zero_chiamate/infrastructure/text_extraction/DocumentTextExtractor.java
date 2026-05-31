package giuseppetavella.zero_chiamate.infrastructure.text_extraction;

import giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions.DocumentTextExtractionException;
import giuseppetavella.zero_chiamate.exceptions.FileException;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions.TikaAPIException;
import org.apache.tika.Tika;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class DocumentTextExtractor {

    @Autowired
    private TikaExtractor tikaExtractor;
    
    
    /**
     * Extract plain text from a document (PDF, DOCX, XLSX, TXT, CSV).
     *
     * @param docBytes the document bytes
     * @param maxChars max number of chars to read from document
     * 
     * @return the extracted plain text
     * 
     * @throws InvalidDataException if document is not a text or pdf file
     * @throws DocumentTextExtractionException if document had no text
     *  or was not truly atext/pdf file (for example a scanned pdf or image converted to pdf)
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
        var extracted = tikaExtractor.bytesToText(docBytes, maxChars);

        // if there's no result
        if(extracted.isEmpty())  {
            throw new DocumentTextExtractionException(
                "Document to extract was a text or pdf file, but text extracted was empty. Likely causes: "
                        +"1) document was an image converted to pdf "
                        +"2) document contained text but had no text. "
                        +"Mime type is '" + FileHelper.getMimeType(docBytes) + "'. "
                        +"Provide a valid document that contains text."
            );
        }
        
        return extracted;

    }


    /*
    * Read the whole file.
    * */
    public @NonNull String extract(byte[] docBytes) {
        return extract(docBytes, -1);
    }
    

}