package giuseppetavella.zero_chiamate.infrastructure.pdf;

import giuseppetavella.zero_chiamate.domain.business.Template;
import giuseppetavella.zero_chiamate.exceptions.PdfGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
