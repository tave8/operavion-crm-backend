package giuseppetavella.zero_chiamate.infrastructure.pdf;

import giuseppetavella.zero_chiamate.config.Template;
import giuseppetavella.zero_chiamate.infrastructure.pdf.exceptions.FlyingSaucerAPIException;
import giuseppetavella.zero_chiamate.infrastructure.pdf.exceptions.PdfGenerationException;
import giuseppetavella.zero_chiamate.infrastructure.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Base class for working with PDFs.
 * It deals with bytes and lower level details.
 */
@Service
public class FlyingSaucerAPIPdfService {
    
    /**
     * HTML -> PDF
     */
    public byte[] htmlToPdf(String html) throws FlyingSaucerAPIException
    {
        // Generate PDF into memory
        var baos = new ByteArrayOutputStream();
        var renderer = new ITextRenderer();

        try {

            renderer.setDocumentFromString(html, null);
            renderer.layout();
            renderer.createPDF(baos);
            
            return baos.toByteArray();

        } catch (Exception ex) {

            throw new FlyingSaucerAPIException("Failed to generate PDF from HTML. DETAILS: " + ex.getMessage());

        }

    }
    
    
}
