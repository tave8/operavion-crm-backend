package giuseppetavella.zero_chiamate.infrastructure.template;

import giuseppetavella.zero_chiamate.infrastructure.template.exceptions.TemplateException;
import giuseppetavella.zero_chiamate.infrastructure.template.exceptions.ThymeleafAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateEngineException;
import org.thymeleaf.exceptions.TemplateInputException;

import java.util.Map;

/**
 * API-specific template service.
 * (Fill html templates, used for report and email creation)
 * 
 * This service is "lower level" and should not be used by the app.
 * Use TemplateService instead.
 */
@Service
public class ThymeleafAPIService {

    @Autowired
    private TemplateEngine templateEngine;

    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ThymeleafAPIService.class);
    
    
    /**
     * API-specific template filler.
     * Do not call this directly from the app.
     * Use TemplateService.fillTemplate instead.
     *
     * @throws TemplateException if input template is not valid / does not exist
     */
    public String fillTemplate(String templatePath, Map<String, ? extends Object> vars)
    {

        // *****************
        // CREATE CONTEXT FOR TEMPLATE
        // *****************
        
        var context = new Context();

        // populate the template with the given vars
        for(String key : vars.keySet()) {

            var value = vars.get(key);

            context.setVariable(key, value);
        }

        // *****************
        // FILL TEMPLATE
        // *****************
        
        try {

            // fill the template
            return templateEngine.process(templatePath, context);

        } catch (TemplateInputException ex) {

            throw new ThymeleafAPIException(
                    "Template input error. " +
                            "Template: '" + templatePath + "'. " +
                            "Keys passed to method: " + vars.keySet() + ". " +
                            "Keys registered in template context (actual): " + context.getVariableNames() + ". " +
                            "DETAILS: " + ex.getMessage()
            );

        } catch (TemplateEngineException ex) {

            throw new ThymeleafAPIException(
                    "Template engine error. " +
                            "Template: '" + templatePath + "'. " +
                            "Keys passed to method: " + vars.keySet() + ". " +
                            "Keys registered in template context (actual): " + context.getVariableNames() + ". " +
                            "DETAILS: " + ex.getMessage()
            );

        } catch (Exception ex) {

            throw new ThymeleafAPIException(
                    "Unknown error while processing template. " +
                            "Template: '" + templatePath + "'. " +
                            "Keys passed to method: " + vars.keySet() + ". " +
                            "Keys registered in template context (actual): " + context.getVariableNames() + ". " +
                            "DETAILS: " + ex.getMessage()
            );

        }

    }
    
}
