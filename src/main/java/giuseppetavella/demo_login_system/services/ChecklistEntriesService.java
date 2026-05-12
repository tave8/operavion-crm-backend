package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.ChecklistEntry;
import giuseppetavella.demo_login_system.repositories.ChecklistEntriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChecklistEntriesService {
    
    @Autowired
    private ChecklistEntriesRepository checklistEntriesRepository;

    /**
     * Save a checklist entry.
     * @return
     */
    public ChecklistEntry save(ChecklistEntry checklistEntry) {
        return this.checklistEntriesRepository.save(checklistEntry);
    }
    
}
