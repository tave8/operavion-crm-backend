package giuseppetavella.demo_login_system.runners;

import giuseppetavella.demo_login_system.jobs.JobExecution;
import giuseppetavella.demo_login_system.jobs.JobExecutionService;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class JobExecutionDemoRunner implements CommandLineRunner {
    
    @Autowired
    private JobExecutionService jobExecutionService;

    @Override
    public void run(String... args) throws Exception {

        // JobExecution jobExecution1 = new JobExecution(
        //         JobName.SEND_EMAIL_TO_USERS_WHO_SIGNEDUP_TODAY,
        //         UUID.randomUUID(),
        //         OffsetDateTime.now().minusMonths(1)
        // );
        //
        // this.jobExecutionService.save(jobExecution1);
        
        // ********** JOB EXECUTIONS FROM DB
        JobExecution jobExecution1FromDB = this.jobExecutionService.findById(1L);

        System.out.println(jobExecution1FromDB);
        
        
    }
}
