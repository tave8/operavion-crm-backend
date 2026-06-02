package giuseppetavella.zero_chiamate.infrastructure.pdf;

import giuseppetavella.zero_chiamate.config.Template;
import giuseppetavella.zero_chiamate.infrastructure.pdf.exceptions.PdfGenerationException;
import giuseppetavella.zero_chiamate.infrastructure.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Higher level PDF service.
 * It deals with PDF entities.
 * Only interface towards business logic.
 * 
 */
@Service
public class PdfService {
    
    @Autowired
    private FlyingSaucerAPIPdfService flyingSaucerAPIPdfService;
    
    @Autowired
    private TemplateService templateService;
    
    
    /**
     * Build a PDF. 
     * Provide a template and the variables to feed into that template.
     */
    public Pdf templateToPdf(Template template, Map<String, ? extends Object> vars)
    {
        
        var html = templateService.fillTemplate(template, vars);
        
        var bytes = flyingSaucerAPIPdfService.htmlToPdf(html);
        
        return new Pdf(bytes);
    } 
    
    
}
