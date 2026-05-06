package giuseppetavella.demo_login_system.jobs.concrete_jobs.email_expiring_contracts;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.jobs.JobExecutionItem;
import giuseppetavella.demo_login_system.jobs.JobExecutionMetadata;
import giuseppetavella.demo_login_system.jobs.JobExecutionService;
import giuseppetavella.demo_login_system.jobs.JobExecutor;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import giuseppetavella.demo_login_system.services.AppEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailExpiringContracts_JobExecutor extends JobExecutor<User> {
    
    @Autowired
    private EmailExpiringContracts_ItemRepository thisRepository;
    
    @Autowired
    private AppEmailService appEmailService;
    
    @Autowired
    private JobExecutionService jobExecutionService;
    
    
    public EmailExpiringContracts_JobExecutor() {
        super(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE, 2);
    }
    
    
    @Override
    public void processItem(JobExecutionItem<?> itemToProcess, JobExecutionMetadata jobExecutionMetadata) {
        
        if (itemToProcess == null) {
            return;
        }
        
        // send email, do business-specific logic
        User user = (User) itemToProcess.getItem();
        
        jobExecutionMetadata.getExtra().put("firstname", user.getFirstname());
        
        // metadata.getProcessedItemIds().removeIf((x) -> x.equals("id1"));
        
        
        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            // throw new RuntimeException(e);
        }
        
        
        
        // this.appEmailService.sendMeInvoiceReport();
        
        throw new RuntimeException("error during processing");
        
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
    public JobExecutionItem<User> getItemByIdOnIncompleteExecution(UUID itemId) {
        
        Optional<User> maybeNextUser = this.thisRepository.getItemByIdOnIncompleteExecution(itemId);

        if(maybeNextUser.isEmpty()) {
            return null;
        }

        User user = maybeNextUser.get();

        return new JobExecutionItem<>(user, user.getUserId());
        
    }


}
