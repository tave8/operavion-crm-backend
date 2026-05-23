package giuseppetavella.demo_login_system.domain.entities.checklist_entries;

import giuseppetavella.demo_login_system.domain.entities.checklists.Checklist;
import giuseppetavella.demo_login_system.domain.entities.checklist_entries.dto.to_send.ChecklistEntryToSendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * Get raw entries for this checklist
     */
    public List<ChecklistEntry> getEntriesByChecklist(Checklist checklist) {
        return this.checklistEntriesRepository.getEntriesByChecklist(checklist);
    }

    /**
     * Get entries for this checklist as DTOs
     */
    public List<ChecklistEntryToSendDTO> getEntriesByChecklistAsDTO(Checklist checklist) {
        return this.checklistEntriesRepository
                .getEntriesByChecklist(checklist)
                .stream()
                .map(ChecklistEntryToSendDTO::new)
                .toList();
    }
    
    
}
