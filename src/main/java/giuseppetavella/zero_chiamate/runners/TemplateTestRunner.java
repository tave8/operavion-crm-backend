package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.infrastructure.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TemplateTestRunner implements CommandLineRunner {
    
    @Autowired
    private TemplateService templateService;
    
    @Override
    public void run(String... args) throws Exception {
        
        
        // var html = templateService.fillTemplate(Template.REPORT_SHIFTS_COUNT_BY_OPERATOR, Map.of());

        // System.out.println(html);
        
    }
}
