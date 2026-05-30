package giuseppetavella.zero_chiamate.domain.business.jobs.email_expiring_contracts;

import giuseppetavella.zero_chiamate.domain.entities.notifications.Notification;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationType;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecution;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionItem;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionMetadata;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutor;
import giuseppetavella.zero_chiamate.domain.business.jobs.JobName;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailExpiringContracts_JobExecutor extends JobExecutor<User> {
    
    @Autowired
    private EmailExpiringContracts_ItemRepository thisRepository;
    
    @Autowired
    private EmailService appEmailService;
    
    @Autowired
    private NotificationsService notificationsService;
    
    
    public EmailExpiringContracts_JobExecutor() {
        super(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE, 2);
    }
    
    
    @Override
    public void processItem(JobExecutionItem<?> itemToProcess, JobExecution jobExecution) {
        
        if (itemToProcess == null) {
            return;
        }
        
        // send email, do business-specific logic
        User user = (User) itemToProcess.getItem();
        
        // add notification in DB

        Notification newNotification = new Notification(
                user,
                NotificationType.EXPIRING_EMPLOYEE_CONTRACT,
                "this employee's contract is expiring",
                "<added by background job>"
        );
        
        this.notificationsService.save(
                newNotification
        );
        
        // send an email to the employee
        // this.appEmailService.sendEmail(
        //         user.getEmail(),
        //         "expiring employee",
        //         "The employee " + user.getFirstname() + " is expiring. Here's their profile picture: " + user.getAvatarUrl()
        // );
        
        
        // try {
        //     // Thread.sleep(2000);
        //
        // } catch (InterruptedException e) {
        //     // throw new RuntimeException(e);
        // }
        
         
        
        // this.appEmailService.sendMeInvoiceReport();
        
        // throw new RuntimeException("error during processing");
        
        // User user = (User) itemToProcess.getItem();
        //
        // System.out.println("processing item " + itemToProcess);
        // //
        // return new JobExecutionResult<User>(itemToProcess);
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
