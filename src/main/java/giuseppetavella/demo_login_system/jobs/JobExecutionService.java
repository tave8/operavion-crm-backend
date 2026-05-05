package giuseppetavella.demo_login_system.jobs;

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
    
}
