package giuseppetavella.zero_chiamate.infrastructure.template.template_models;

import giuseppetavella.zero_chiamate.domain.entities.users.User;

import java.util.Map;

public interface AdminWeeklyReportTemplateInterface {
    Map<User, Integer> getShiftsCountByOperator();
 }
