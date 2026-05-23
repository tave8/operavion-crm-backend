package giuseppetavella.zero_chiamate.domain.entities.checklists;

import giuseppetavella.zero_chiamate.domain.entities.checklist_entries.ChecklistEntriesService;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddressesService;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.tasks.TasksService;
import giuseppetavella.zero_chiamate.domain.entities.checklist_entries.ChecklistEntry;
import giuseppetavella.zero_chiamate.domain.entities.tasks.Task;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddress;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.exceptions.InvalidUUIDStringException;
import giuseppetavella.zero_chiamate.exceptions.NotFoundException;
import giuseppetavella.zero_chiamate.helpers.AuthorizationHelper;
import giuseppetavella.zero_chiamate.helpers.StringHelper;
import giuseppetavella.zero_chiamate.domain.entities.checklists.dto.sent.ChecklistSimpleEntrySentDTO;
import giuseppetavella.zero_chiamate.domain.entities.checklists.dto.sent.NewChecklistWithSimpleEntriesSentDTO;
import giuseppetavella.zero_chiamate.domain.entities.checklist_entries.dto.to_send.ChecklistEntryToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.checklists.dto.to_send.ChecklistToSendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChecklistsService {
    
    @Autowired
    private ChecklistsRepository checklistsRepository;
    
    @Autowired
    private ChecklistEntriesService checklistEntriesService;
    
    @Autowired
    private ClientAddressesService clientAddressesService;
    
    @Autowired
    private TasksService tasksService;


    /**
     * Checklist -> Checklist DTO 
     * 
     * @param checklist
     * @return
     */
    public ChecklistToSendDTO toChecklistDTO(Checklist checklist)
    {
        List<ChecklistEntryToSendDTO> entries = this.checklistEntriesService.getEntriesByChecklistAsDTO(checklist);
    
        return new ChecklistToSendDTO(checklist, entries);
    }
    
    
    /**
     * Save a checklist. 
     * 
     * @param checklist
     * @return
     */
    public Checklist save(Checklist checklist) {
        return this.checklistsRepository.save(checklist);
    }

    /**
     * Find checklist by ID.
     */
    public Checklist findById(UUID checklistId) throws NotFoundException {
        return this.checklistsRepository.findById(checklistId).orElseThrow(() -> new NotFoundException(checklistId, "checklist"));
    }    

    
    /**
     * Add a checklist with simple entries.
     * See the relevant payload for info on what "simple entries" means.
     * In short, a simple checklist entry is a checklist entry
     * that has not been associated to a checklist yet,
     * which we are about to do right now.
     */
    @Transactional
    public Checklist addChecklistWithSimpleEntries(NewChecklistWithSimpleEntriesSentDTO body,
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
        
        return checklistFromDB;
        
    }


    /**
     * Find checklists by client address.
     * 
     * @return
     */
    public List<Checklist> findChecklistsByClientAddress(Company company,
                                                         ClientAddress clientAddress)
    {
        AuthorizationHelper.requireSameCompany(company, clientAddress.getClient().getCompany());
        
        return this.checklistsRepository.findChecklistsByClientAddress(clientAddress);
    }

    
    public List<ChecklistToSendDTO> findChecklistsByClientAddressDTO(Company company,
                                                                     ClientAddress clientAddress)
    {
        return this.findChecklistsByClientAddress(company, clientAddress)
                    .stream()
                    .map(this::toChecklistDTO)
                    .toList();
    }

    public List<ChecklistToSendDTO> findChecklistsByClientAddressDTO(Company company,
                                                                     UUID clientAddressId)
    {
        ClientAddress clientAddress = this.clientAddressesService.findById(clientAddressId);
        
        return this.findChecklistsByClientAddressDTO(company, clientAddress);
    }

    /**
     * Get checklists.
     */
    public Page<Checklist> findChecklists(Company company,
                                          String searchQuery, 
                                          int page,
                                          int pageSize,
                                          String sortBy,
                                          String sortOrder) throws InvalidDataException
    {

        // we can sort by these values
        StringHelper.requireInValues(
                sortBy,
                List.of("name"),
                "sortBy"
        );

        // we can sort in these "directions"
        StringHelper.requireInValues(
                sortOrder,
                List.of("asc", "desc"),
                "sortOrder"
        );

        // number of elements in page
        int finalSize = Math.clamp(pageSize, 1, 100);

        // which pagination page was requested
        int finalPage = Math.max(0, page);

        Sort sort = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(finalPage, finalSize, sort);
        
        String searchQueryPattern = StringHelper.buildSearchQueryPattern(searchQuery);
        
        return this.checklistsRepository.findChecklists(
                company,
                searchQueryPattern,
                pageable
        );

    }
    
}
