package giuseppetavella.zero_chiamate.domain.entities.tasks_completion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TasksCompletionRepository extends JpaRepository<TaskCompletion, UUID> {
}
