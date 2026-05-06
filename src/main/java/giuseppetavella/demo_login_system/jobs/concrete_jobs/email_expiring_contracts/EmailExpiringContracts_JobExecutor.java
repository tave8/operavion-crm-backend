package giuseppetavella.demo_login_system.jobs.concrete_jobs.email_expiring_contracts;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.jobs.JobExecution;
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

    // TODO: the second paramater of this method should not be 
    //  the current job execution, because that means that
    //  i could corrupt the current job execution
    //  i should only pass something like the metadata?
    @Override
    public void processItem(JobExecutionItem<?> itemToProcess, JobExecution currentJobExecution) {
        
        if (itemToProcess == null) {
            return;
        }
        
        // if(currentJobExecution.getState().equals(JobExecutionState.INCOMPLETE)) {
        //     System.out.println("this job execution was incomplete");
        // }
        
        // send email, do business-specific logic
        User user = (User) itemToProcess.getItem();
        
        JobExecutionMetadata metadata = currentJobExecution.getMetadata();
        
        metadata.getExtra().put("firstname", user.getFirstname());
        
        // metadata.getProcessedItemIds().removeIf((x) -> x.equals("id1"));
        //
        jobExecutionService.save(currentJobExecution);
        
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
    public JobExecutionItem<User> getItemById(UUID itemId) {
        
        Optional<User> maybeNextUser = this.thisRepository.getItemById(itemId);

        if(maybeNextUser.isEmpty()) {
            return null;
        }

        User user = maybeNextUser.get();

        return new JobExecutionItem<>(user, user.getUserId());
        
    }


}
