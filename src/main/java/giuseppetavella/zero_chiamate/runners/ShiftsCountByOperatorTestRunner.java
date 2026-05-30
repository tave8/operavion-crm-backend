package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ShiftsCountByOperatorTestRunner implements CommandLineRunner {
    
    @Autowired
    private JobManager jobManager;

    @Override
    public void run(String... args) throws Exception {
        
        // jobManager.executeJob(JobName.SEND_ADMIN_WEEKLY_REPORT);
        
    }
}
