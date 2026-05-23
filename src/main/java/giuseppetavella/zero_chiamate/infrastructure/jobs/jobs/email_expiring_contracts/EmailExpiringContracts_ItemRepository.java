package giuseppetavella.zero_chiamate.infrastructure.jobs.jobs.email_expiring_contracts;

import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionItemRepository;
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
