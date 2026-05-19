package giuseppetavella.demo_login_system.interfaces.template_models;

import giuseppetavella.demo_login_system.entities.User;

import java.util.Map;

public interface AdminWeeklyReportTemplateInterface {
    Map<User, Integer> getShiftsCountByOperator();
 }
