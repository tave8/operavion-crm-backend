package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Checklist;
import giuseppetavella.demo_login_system.entities.ChecklistEntry;
import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class SeedDataService {
    
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
    
        String checklistName = "Pulizia Giornaliera Uffici";
        List<String> taskNames = List.of(
            "Svuotare i cestini",
            "Passare l'aspirapolvere",
            "Lavare i pavimenti",
            "Pulire i bagni",
            "Rifornire carta igienica e sapone"
        );
        
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
