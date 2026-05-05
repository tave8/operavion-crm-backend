package giuseppetavella.demo_login_system.jobs.concrete_jobs;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.jobs.JobExecution;
import giuseppetavella.demo_login_system.jobs.JobExecutionItem;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import giuseppetavella.demo_login_system.services.AppEmailService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailEmployeesWhoseContractAboutToExpire_JobExecutor extends JobExecutor<User> {
    
    @Autowired
    private EmailEmployeesWhoseContractAboutToExpire_Repository thisRepository;
    
    @Autowired
    private AppEmailService appEmailService;
    
    
    public EmailEmployeesWhoseContractAboutToExpire_JobExecutor() {
        super(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE);
    }

    @Override
    public void processItem(JobExecutionItem<?> itemToProcess, JobExecution currentJobExecution) {
        
        // send email, do business-specific logic

        if (itemToProcess == null) {
            return;
        }
        
        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            // throw new RuntimeException(e);
        }
        
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
        
        return new JobExecutionItem<>(user, user.getUserId());
        
    }

    @Override
    public JobExecutionItem<User> getItemById(UUID itemId) {
        
        Optional<User> maybeNextUser = this.thisRepository.getItemById(itemId);

        if(maybeNextUser.isEmpty()) {
            return null;
        }

        User user = maybeNextUser.get();

        return new JobExecutionItem<>(user, user.getUserId());
        
    }


}
