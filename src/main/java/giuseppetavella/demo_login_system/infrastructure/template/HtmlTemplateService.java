package giuseppetavella.demo_login_system.infrastructure.template;

import giuseppetavella.demo_login_system.exceptions.HtmlTemplateException;
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
public class HtmlTemplateService {
    
    @Autowired
    private TemplateEngine templateEngine;

    
    /**
     * Fill a HTML template located in directory "resources".
     * 
     * @throws HtmlTemplateException if input template is not valid / does not exist
     */
    public String fillTemplate(String template, Map<String, ? extends Object> vars) 
    {

        Context context = new Context();

        // populate the template with the given vars
        for(String key : vars.keySet()) {
            Object value = vars.get(key);
            context.setVariable(key, value);
        }

        try {

            return this.templateEngine.process(template, context);

        } catch(TemplateInputException ex) {

            throw new HtmlTemplateException("Input template was '"+template+"'. DETAILS: " + ex.getMessage());

        } catch(TemplateEngineException ex) {

            throw new HtmlTemplateException("While filling a html template, a generic error "
                                    +"specific to the template engine occurred. DETAILS: " + ex.getMessage());
            
        } 

    }
    
}
