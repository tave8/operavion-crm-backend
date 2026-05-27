package giuseppetavella.zero_chiamate.infrastructure.pdf;

import giuseppetavella.zero_chiamate.domain.business.Template;
import giuseppetavella.zero_chiamate.infrastructure.BrowserContentDispositionHeader;
import giuseppetavella.zero_chiamate.exceptions.PdfGenerationException;
import giuseppetavella.zero_chiamate.infrastructure.template.HtmlTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Map;

/**
 * Higher level PDF service.
 * It deals with PDF entities.
 * Wrapper for BasePdfService.
 * Only interface towards business logic.
 * 
 */
@Service
public class PdfService {
    
    
    @Autowired
    private BasePdfService basePdfService;
    
    
    
    /**
     * template + vars -> PDF
     */
    public Pdf templateToPdf(Template template,
                             Map<String, ? extends Object> vars) throws PdfGenerationException
    {
        var bytes = basePdfService.templateToPdf(template, vars);
        
        return new Pdf(bytes);
    }

    
    
    
}
