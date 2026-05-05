package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobExecutionService {
    
    @Autowired
    private JobExecutionRepository jobExecutionRepository;

    /**
     * Save a job execution.
     */
    public JobExecution save(JobExecution jobExecution) {
        return this.jobExecutionRepository.save(jobExecution);
    }

    /**
     * Get a job execution by ID.
     */
    public JobExecution findById(Long jobExecutionId) throws NotFoundException
    {
        return this.jobExecutionRepository
                .findById(jobExecutionId)
                .orElseThrow(() -> new NotFoundException(jobExecutionId, "JOB EXECUTION"));
    }

}
