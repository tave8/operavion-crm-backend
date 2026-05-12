package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChecklistsRepository extends JpaRepository<Checklist, UUID> {
    
    
    
}
