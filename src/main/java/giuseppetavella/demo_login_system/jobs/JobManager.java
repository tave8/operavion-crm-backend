package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.jobs.enums.JobName;
import giuseppetavella.demo_login_system.jobs.exceptions.JobException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionException;
import giuseppetavella.demo_login_system.jobs.functional_interfaces.JobRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Cron Job Manager defines the HOW to run a specific job,
 * and WHO is responsible for that specific execution.
 */
@Service
public class JobManager {

    @Autowired
    private JobExecutor jobExecutor;
    
    // logger
    private static final Logger LOGGER = Logger.getLogger(JobManager.class.getName());

    
    /**
     * Entry point for executing a job.
     * Provides centralized error handling. 
     * 
     * @param jobName which job to execute.
     *             
     * @throws JobExecutionException if any error during the execution of 
     *  the given cron job task
     * @throws JobException if a generic error occurred
     */
    public void executeJob(JobName jobName) 
    {
        
        // give me the job executor for this job
        JobRunner executor = this.getJobExecutorElseThrow(jobName);
        
        LOGGER.info("JOB '"+jobName+"': this job was called to be executed, executing it...");
        
        System.out.println(executor.processItem(new JobExecutionItem(UUID.randomUUID())));
        
        

        // the executor keeps on executing items until there's no more
        // the executor should continue processing items even if there's an error, 
        // so the try/catch should be inside the while loop
        // while() {

            // try {
            //
            //
            //
            //
            //
            //     // executor 
            //
            //         LOGGER.info("JOB '"+jobName+"': finished executing job with no errors.");
            //
            // } catch(RuntimeException ex) {
            //
            //     throw new JobExecutionException(jobName.name(), ex.getMessage());
            //
            // }

        // }
        //
        

    }


    /**
     * Get the job executor for the given job.
     * If a job executor for this job was not mapped,
     * an exception will be thrown.
     * 
     * When a new job is added, you must only update the
     * mapping between the job name (in this method)
     * and the actual job executor.
     * 
     */
    
    private JobRunner getJobExecutorElseThrow(JobName jobName) throws JobException
    {

        if(jobName.equals(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE)) {
            
            return this.jobExecutor.emailEmployeesWithContractAboutToExpire();

        }
        
        
        // add more jobs here...     


        // the given job was not mapped to an executor

        LOGGER.severe("JOB '"+jobName+"': this job was not found, is not mapped or is not recognized internally.");

        throw new JobException("Job '"+jobName +"' was not found, is not mapped or is not recognized internally.");
        

    }
    

    
}
