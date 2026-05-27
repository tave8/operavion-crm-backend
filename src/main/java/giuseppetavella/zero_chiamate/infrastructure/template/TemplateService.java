package giuseppetavella.zero_chiamate.infrastructure.template;

import giuseppetavella.zero_chiamate.config.Template;
import giuseppetavella.zero_chiamate.exceptions.TemplateException;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateEngineException;
import org.thymeleaf.exceptions.TemplateInputException;

import java.util.Map;

/**
 * Fill HTML templates.
 */
@Component
public class TemplateService {
    
    @Autowired
    private TemplateEngine templateEngine;

    
    /**
     * Fill a HTML template located in directory "resources".
     * 
     * @throws TemplateException if input template is not valid / does not exist
     */
    public String fillTemplate(Template template, Map<String, ? extends Object> vars) 
    {

        ValidationHelper.requireTemplateExistsElseThrow(
                template.getValue(),
                () -> new TemplateException(
                        "Template '" + template + "' with value '"+template.getValue()+"' does not exist in filesystem. "
                        +"Make sure to exclude the file extension and "
                        +"to start from the templates directory."
                )
        );
        
        
        var context = new Context();

        // populate the template with the given vars
        for(String key : vars.keySet()) {
            
            var value = vars.get(key);
            
            context.setVariable(key, value);
        }

        try {

            return this.templateEngine.process(template.getValue(), context);

        } catch(TemplateInputException ex) {

            throw new TemplateException("Input template was '"+template+"'. DETAILS: " + ex.getMessage());

        } catch(TemplateEngineException ex) {

            throw new TemplateException("While filling a html template, a generic error "
                                    +"specific to the template engine occurred. DETAILS: " + ex.getMessage());
            
        } 

    }
    
}
