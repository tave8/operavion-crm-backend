package giuseppetavella.demo_login_system.jobs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecution, Long> {
    
    
    
}
