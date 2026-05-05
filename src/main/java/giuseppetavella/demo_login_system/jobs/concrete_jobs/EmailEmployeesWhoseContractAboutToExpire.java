package giuseppetavella.demo_login_system.jobs.concrete_jobs;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.jobs.JobExecutionItem;
import giuseppetavella.demo_login_system.jobs.JobExecutionResult;
import giuseppetavella.demo_login_system.jobs.JobExecutorRepository;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailEmployeesWhoseContractAboutToExpire extends JobExecutor<User> {
    
    
    public EmailEmployeesWhoseContractAboutToExpire(JobExecutorRepository jobExecutorRepository) {
        super(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE, jobExecutorRepository);
    }

    @Override
    public void processItem(@Nullable JobExecutionItem<?> itemToProcess) {
        
        // send email, do business-specific logic

        if (itemToProcess == null) {
            return;
        }
        
        throw new RuntimeException("error during processing");
        
        // User user = (User) itemToProcess.getItem();
        //
        // System.out.println("processing item " + itemToProcess);
        // //
        // return new JobExecutionResult<User>(itemToProcess);
    }

    @Override
    public JobExecutionItem<User> getNextItem() {
        
        Optional<User> maybeNextUser = this.jobExecutorRepository.getNextEmployeeWhoseContractAboutToExpire(this.getJobName().name());
        
        if(maybeNextUser.isEmpty()) {
            return null;
        }
        
        User user = maybeNextUser.get();
        
        return new JobExecutionItem<>(user, user.getUserId());
        
    }
    
    
    
}
