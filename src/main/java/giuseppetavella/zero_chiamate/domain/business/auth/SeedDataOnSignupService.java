package giuseppetavella.zero_chiamate.domain.business.auth;

import giuseppetavella.zero_chiamate.domain.entities.checklist_entries.ChecklistEntriesService;
import giuseppetavella.zero_chiamate.domain.entities.checklists.ChecklistsService;
import giuseppetavella.zero_chiamate.domain.entities.tasks.TasksService;
import giuseppetavella.zero_chiamate.domain.entities.checklists.Checklist;
import giuseppetavella.zero_chiamate.domain.entities.checklist_entries.ChecklistEntry;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.tasks.Task;
import giuseppetavella.zero_chiamate.exceptions.SeedDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Seed data for users.
 * 
 * Seeding data means creating default or standard data
 * for users, so they don't have to do it. 
 * 
 * It could also be called populating or initializing data.
 * 
 */
@Service
public class SeedDataOnSignupService {
    
    @Autowired
    private ChecklistsService checklistsService;
    
    @Autowired
    private TasksService tasksService;
    
    @Autowired
    private ChecklistEntriesService checklistEntriesService;


    /**
     * Seed standard checklists for a company.
     *
     * @param company
     */
    @Transactional
    public void seedStandardChecklists(Company company) 
    {
        
        Map<String, List<String>> taskMap = Map.of(
                "Pulizia Giornaliera Uffici", List.of(
                        "Svuotare i cestini",
                        "Passare l'aspirapolvere",
                        "Lavare i pavimenti",
                        "Pulire i bagni",
                        "Rifornire carta igienica e sapone"
                ),
                "Pulizia Scale Condominio", List.of(
                        "Spazzare le scale", 
                        "Lavare le scale",
                        "Pulire l'ascensore", 
                        "Pulire l'ingresso",
                        "Svuotare i cestini condominiali"
                ),
                "Sanificazione Bagni", List.of(
                        "Pulire i sanitari",
                        "Disinfettare le superfici",
                        "Lavare i pavimenti",
                        "Rifornire carta igienica e sapone",
                        "Pulire gli specchi"
                )
                // add more checklist name : task names here...         
        );
        
        
        // for each checklist name, get its task names 
        for (String checklistName : taskMap.keySet()) 
        {
            
            try {
                
                List<String> taskNames = taskMap.get(checklistName);
                
                this.addTasksToChecklist(
                        company,
                        checklistName,
                        taskNames
                );
                
            } catch (RuntimeException e) {
            
                throw new SeedDataException("For checklist: " + checklistName + " DETAILS: " + e.getMessage());
            
            }
            
            
        }
        

    }
    

    @Transactional
    private void addTasksToChecklist(Company company, 
                                    String checklistName, 
                                    List<String> taskNames) 
    {

        Checklist checklistFromDB = this.checklistsService.save(new Checklist(
                company, checklistName
        ));
        
        List<Task> tasks = taskNames.stream().map(taskName -> new Task(company, taskName)).toList();
        
        List<Task> tasksFromDB = this.tasksService.saveAll(tasks);

        for (int i = 0; i < tasksFromDB.size(); i++) {
            
            int position = i+1;
            
            Task taskFromDB = tasksFromDB.get(i);
            
            this.checklistEntriesService.save(new ChecklistEntry(
                    checklistFromDB,
                    taskFromDB,
                    position
            ));
            
        }
        
        
    }
    
    
    
    
}
