package giuseppetavella.zero_chiamate.infrastructure.template;

import giuseppetavella.zero_chiamate.config.EmailTemplate;
import giuseppetavella.zero_chiamate.config.Template;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.template.exceptions.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Fill HTML templates.
 * API-independent.
 * This is the service the the app should use.
 * 
 */
@Component
public class TemplateService {
    
    @Autowired
    private ThymeleafService thymeleafService;

    
    /**
     * Fill a HTML template located in directory "resources".
     * 
     * @throws TemplateException if input template is not valid / does not exist
     */
    public String fillTemplate(Template template,
                               Map<String, ? extends Object> vars)
    {

        if (vars == null) {
            throw new TemplateException(
                    "Template vars cannot be null. Template: '" + template + "'."
            );
        }
        
        if (template == null) {
            throw new TemplateException(
                    "Input template cannot be null."
            );
        }

        // this template exists?
        ValidationHelper.requireTemplateExistsElseThrow(
                template.getValue(),
                () -> new TemplateException(
                        "Template '" + template + "' with value '" + template.getValue() + "' does not exist in filesystem. " +
                                "Make sure to exclude the file extension and " +
                                "to start from the templates directory. " +
                                "Keys passed to template: " + vars.keySet() + "."
                )
        );

        return thymeleafService.fillTemplate(
                template.getValue(),
                vars
        );
    }
    
}
