package giuseppetavella.zero_chiamate.utils;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

@Component
public class DocumentTextExtractor {

    private final Tika tika = new Tika();

    /**
     * Extract bytes from document.
     * 
     * @return
     */
    // public String bytesToText(byte[] docBytes)
    // {
    //    
    //     //  check that this document is a valid document?
    //     // pdf, docx, etc.
    //    
    //     try (ByteArrayInputStream inputStream = new ByteArrayInputStream(docBytes)) {
    //
    //         return tika.parseToString(inputStream);
    //        
    //     } catch (Exception ex) {
    //        
    //         throw new FileException("Error while extracting text from a document. DETAILS: " + ex.getMessage());
    //        
    //     }
    //    
    //
    // }
    
}
