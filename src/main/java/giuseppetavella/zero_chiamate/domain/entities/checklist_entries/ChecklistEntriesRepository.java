package giuseppetavella.zero_chiamate.domain.entities.checklist_entries;

import giuseppetavella.zero_chiamate.domain.entities.checklists.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChecklistEntriesRepository extends JpaRepository<ChecklistEntry, UUID> {

    /**
     * Get entries by checklist.
     * 
     * @return
     */
    @Query("""
    
        SELECT ce
        FROM ChecklistEntry ce
        WHERE ce.checklist = :checklist
            
    """)
    List<ChecklistEntry> getEntriesByChecklist(
            Checklist checklist
    );
    
}
