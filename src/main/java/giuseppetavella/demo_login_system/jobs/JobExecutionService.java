package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobExecutionService {
    
    @Autowired
    private JobManagerRepository jobManagerRepository;

    /**
     * Save a job execution.
     */
    public JobExecution save(JobExecution jobExecution) {
        return this.jobManagerRepository.save(jobExecution);
    }

    /**
     * Get a job execution by ID.
     */
    public JobExecution findById(Long jobExecutionId) throws NotFoundException
    {
        return this.jobManagerRepository
                .findById(jobExecutionId)
                .orElseThrow(() -> new NotFoundException(jobExecutionId, "JOB EXECUTION"));
    }

    /**
     * Find the last job execution of the given job.
     * The given job might have never been executed, 
     * so it's possible to not have any execution returned.
     */
    public Optional<JobExecution> findLastExecutionOfJob(JobName jobName) 
    {
        return this.jobManagerRepository.findLastExecutionOfJob(jobName.name());
    }

}
