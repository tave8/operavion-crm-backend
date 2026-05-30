package giuseppetavella.zero_chiamate.domain.business.jobs.send_admin_weekly_report;

import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationsService;
import giuseppetavella.zero_chiamate.domain.entities.shifts.ShiftsService;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.helpers.AuthorizationHelper;
import giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator.ShiftsCountByOperatorMailer;
import giuseppetavella.zero_chiamate.helpers.TimeHelper;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionItem;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionMetadata;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutor;
import giuseppetavella.zero_chiamate.domain.business.jobs.JobName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class SendAdminWeeklyReport_JobExecutor extends JobExecutor<User> {
    
    @Autowired
    private SendAdminWeeklyReport_Repository thisRepository;
    
    @Autowired
    private ShiftsCountByOperatorMailer shiftsCountByOperatorMailer;
    
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
        //  range should be this week
        LocalDate lastMonday = TimeHelper.lastMonday();
        LocalDate lastFriday = TimeHelper.lastFriday();
        
        // find shifts by operator
        Map<User, Integer> shiftsCountByOperator  = this.shiftsService.countShiftsByOperator(company, lastMonday, lastFriday);

        // if no entry is present, skip this admin
        if(shiftsCountByOperator.isEmpty()) {
            return;
        }
        
        // send email with weekly report as pdf attachment
        shiftsCountByOperatorMailer.send(
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
