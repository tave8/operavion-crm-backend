package giuseppetavella.demo_login_system.infrastructure.jobs.jobs.send_admin_weekly_report;

import giuseppetavella.demo_login_system.domain.entities.companies.CompaniesService;
import giuseppetavella.demo_login_system.domain.entities.notifications.NotificationsService;
import giuseppetavella.demo_login_system.domain.entities.shifts.ShiftsService;
import giuseppetavella.demo_login_system.domain.entities.users.UsersService;
import giuseppetavella.demo_login_system.domain.entities.companies.Company;
import giuseppetavella.demo_login_system.domain.entities.users.User;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.domain.business.admin_weekly_report.AdminWeeklyReportEmailService;
import giuseppetavella.demo_login_system.infrastructure.jobs.job_library.JobExecutionItem;
import giuseppetavella.demo_login_system.infrastructure.jobs.job_library.JobExecutionMetadata;
import giuseppetavella.demo_login_system.infrastructure.jobs.job_library.JobExecutor;
import giuseppetavella.demo_login_system.infrastructure.jobs.jobs.JobName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class SendAdminWeeklyReport_JobExecutor extends JobExecutor<User> {
    
    @Autowired
    private SendAdminWeeklyReport_Repository thisRepository;
    
    @Autowired
    private AdminWeeklyReportEmailService adminWeeklyReportEmailService;
    
    @Autowired
    private NotificationsService notificationsService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private CompaniesService companiesService;
    
    
    public SendAdminWeeklyReport_JobExecutor() {
        super(JobName.SEND_ADMIN_WEEKLY_REPORT);
    }
    
    
    @Override
    public void processItem(JobExecutionItem<?> itemToProcess, JobExecutionMetadata jobExecutionMetadata) {
        
        if (itemToProcess == null) {
            return;
        }
        
        User admin = (User) itemToProcess.getItem();
        
        AuthorizationHelper.requireUserAdmin(admin);
        
        Company company = admin.getCompany();

        // TODO: fix this +2 logical bug
        LocalDate referenceDate = LocalDate.now().plusDays(2);

        LocalDate lastWeekTarget = referenceDate.minusWeeks(1);

        LocalDate lastMonday = lastWeekTarget.with(DayOfWeek.MONDAY); 
        LocalDate lastFriday = lastWeekTarget.with(DayOfWeek.FRIDAY); 
        
        // find shifts by operator
        Map<User, Integer> shiftsCountByOperator  = this.shiftsService.countShiftsByOperator(company, lastMonday, lastFriday);

        // send email with weekly report as pdf attachment
        adminWeeklyReportEmailService.sendAdminWeeklyReport(
                admin,
                shiftsCountByOperator,
                lastMonday,
                lastFriday
        );
        
        
    }

    @Override
    public JobExecutionItem<User> getNextItem() {
        
        Optional<User> maybeNextUser = this.thisRepository.getNextItem(this.getJobName().name());
        
        if(maybeNextUser.isEmpty()) {
            return null; 
        }
        
        User user = maybeNextUser.get();
        
        return new JobExecutionItem<>(user, user.getId());
        
    }

    @Override
    public JobExecutionItem<User> getItemByIdOnIncompleteExecution(UUID itemId) {
        
        Optional<User> maybeNextUser = this.thisRepository.getItemByIdOnIncompleteExecution(itemId);

        if(maybeNextUser.isEmpty()) {
            return null;
        }

        User user = maybeNextUser.get();

        return new JobExecutionItem<>(user, user.getId());
        
    }


}
