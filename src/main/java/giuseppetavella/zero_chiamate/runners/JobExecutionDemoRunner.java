package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionService;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobExecutionDemoRunner implements CommandLineRunner {
    
    @Autowired
    private JobExecutionService jobExecutionService;
    
    @Autowired
    private JobManager jobManager;

    @Override
    public void run(String... args) throws Exception {

        // JobExecution jobExecution1 = new JobExecution(
        //         JobName.SEND_ME_INVOICE_REPORT,
        //         UUID.randomUUID()
        // );
        // // //
        // this.jobExecutionService.save(jobExecution1);
        // //
        // ********** JOB EXECUTIONS FROM DB
        // JobExecution jobExecution1FromDB = this.jobExecutionService.findById(1L);
        // JobExecution jobExecution2FromDB = this.jobExecutionService.findById(2L);
        // find last executions of job name
        // Optional<JobExecution> lastJobExecutionFromDB = this.jobExecutionService.findLastExecutionOfJob(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE);
        //
        // System.out.println(lastJobExecutionFromDB);
        
        // jobExecution2FromDB.finish();
        // jobExecution2FromDB.setState(JobExecutionState.INCOMPLETE);
        //
        
        // this.jobExecutionService.save(jobExecution1FromDB);
        // this.jobExecutionService.save(jobExecution2FromDB);
        
        
        // System.out.println(jobExecution1FromDB);
        
        
         // this.jobManager.executeJob(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE);
         
        
    }
}
