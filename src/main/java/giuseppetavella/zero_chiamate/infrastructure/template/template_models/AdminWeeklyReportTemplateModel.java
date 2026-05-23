package giuseppetavella.zero_chiamate.infrastructure.template.template_models;

import giuseppetavella.zero_chiamate.domain.entities.users.User;

import java.util.LinkedHashMap;
import java.util.Map;

public class AdminWeeklyReportTemplateModel extends TemplateModel implements AdminWeeklyReportTemplateInterface {

    private final Map<User, Integer> shiftsCountByOperator;

    public AdminWeeklyReportTemplateModel(Map<User, Integer> shiftsCountByOperator) {
        this.shiftsCountByOperator = shiftsCountByOperator;
    }
    
    @Override
    public Map<User, Integer> getShiftsCountByOperator() {
        return this.shiftsCountByOperator;
    }

    @Override
    public Map<String, Object> getVars() {
        // Using LinkedHashMap to keep the variables organized in insertion order
        Map<String, Object> vars = new LinkedHashMap<>();

        // Add your complex structures
        vars.put("shiftsCountByOperator", this.shiftsCountByOperator);

        // Add your flat scalar variables as they get introduced
        // some examples:
        // vars.put("companyName", this.companyName);
        // vars.put("reportWeekRange", this.reportWeekRange);
        
        return vars;
    }
}
