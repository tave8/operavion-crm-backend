package giuseppetavella.zero_chiamate.infrastructure.jobs.job_library;

import java.util.Optional;

public interface JobExecutionItemRepository<T, ID> {
    Optional<T> getNextItem(String jobName);
    Optional<T> getItemByIdOnIncompleteExecution(ID id);
}
