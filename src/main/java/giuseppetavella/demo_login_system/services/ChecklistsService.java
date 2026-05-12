package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Checklist;
import giuseppetavella.demo_login_system.entities.ChecklistEntry;
import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.Task;
import giuseppetavella.demo_login_system.exceptions.InvalidDataException;
import giuseppetavella.demo_login_system.exceptions.InvalidUUIDStringException;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.payloads.in_request.ChecklistSimpleEntrySentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.NewChecklistWithSimpleEntriesSentDTO;
import giuseppetavella.demo_login_system.repositories.ChecklistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ChecklistsService {
    
    @Autowired
    private ChecklistsRepository checklistsRepository;
    
    @Autowired
    private ChecklistEntriesService checklistEntriesService;
    
    @Autowired
    private TasksService tasksService;

    /**
     * Add a checklist with simple entries.
     * See the relevant payload for info on what "simple entries" means.
     * In short, a simple checklist entry is a checklist entry
     * that has not been associated to a checklist yet,
     * which we are about to do right now.
     */
    @Transactional
    public void addChecklistWithSimpleEntries(NewChecklistWithSimpleEntriesSentDTO body,
                                              Company company) 
    {
    
        Checklist newChecklist = new Checklist(company, body.name());
        
        // add the checklist, which is simply a named container for entries
        Checklist checklistFromDB = this.checklistsRepository.save(newChecklist);
        
        // now that we have the checklist saved in DB, we can now
        // associate the entries with this checklist
        
        // iterate through the simple entries in the payload,
        // and associate the checklist to each entry

        for (ChecklistSimpleEntrySentDTO entry : body.entries()) {

            try {

                // cast task it into uuid. it may fail.
                UUID taskId = StringHelper.parseUUID(entry.taskId());
            
                // get task from DB. it may not exist.
                Task taskFromDB = this.tasksService.findById(taskId);
                
                // require that the it's the task of the same company
                AuthorizationHelper.requireSameCompany(company, taskFromDB.getCompany());

                ChecklistEntry newChecklistEntry = new ChecklistEntry(
                        checklistFromDB,
                        taskFromDB,
                        entry.position()
                );

                this.checklistEntriesService.save(newChecklistEntry);

            }
            // if we catch this error, a task ID does not have a valid ID,
            // despite validation at the controller
            catch (InvalidUUIDStringException e) {
                throw new InvalidDataException("This error occurred while iterating on checklist entries: " +
                        "task ID '" + entry.taskId() + "' is not a valid UUID. DETAILS: " + e.getMessage());
            }
            // if we catch this error, a task ID was not found, which means
            // the client has sent us a task ID that we imagine was deleted
            catch (NotFoundException e) {
                throw new InvalidDataException("This error occurred while iterating on checklist entries: " +
                        "task with ID '" + entry.taskId() + "' was not found — it may have been deleted. DETAILS: " + e.getMessage());
            }

        }
            
        
    }
    
}
