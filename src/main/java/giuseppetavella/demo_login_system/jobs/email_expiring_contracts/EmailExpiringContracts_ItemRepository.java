package giuseppetavella.demo_login_system.jobs.email_expiring_contracts;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.job_library.JobExecutionItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class EmailExpiringContracts_ItemRepository implements JobExecutionItemRepository<User, UUID> {

    @Autowired
    private EmailExpiringContracts_JpaRepository jpaRepository;

    @Override
    public Optional<User> getNextItem(String jobName) {
        return jpaRepository.getNextItem(jobName);
    }

    @Override
    public Optional<User> getItemByIdOnIncompleteExecution(UUID id) {
        return jpaRepository.getItemByIdOnIncompleteExecution(id);
    }
}
