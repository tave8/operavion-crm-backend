package giuseppetavella.demo_login_system.infrastructure.template.template_models;

import giuseppetavella.demo_login_system.domain.entities.users.User;

import java.util.Map;

public interface AdminWeeklyReportTemplateInterface {
    Map<User, Integer> getShiftsCountByOperator();
 }
