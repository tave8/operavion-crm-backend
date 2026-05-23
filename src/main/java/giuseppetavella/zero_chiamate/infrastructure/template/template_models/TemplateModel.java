package giuseppetavella.zero_chiamate.infrastructure.template.template_models;

import java.util.Map;

abstract public class TemplateModel {

    /**
     * Get all vars that will be passed to the final template.
     * 
     * @return
     */
    public abstract Map<String, Object> getVars();
    
}
