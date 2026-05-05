package giuseppetavella.demo_login_system.jobs;

import java.util.Optional;

public interface JobExecutionItemRepository<T, ID> {
    Optional<T> getNextItem(String jobName);
    Optional<T> getItemById(ID id);
}
