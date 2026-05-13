package giuseppetavella.demo_login_system.runners;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.services.CompaniesService;
import giuseppetavella.demo_login_system.services.SeedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SeedDataDemoRunner implements CommandLineRunner {
    
    @Autowired
    private SeedDataService seedDataService;
    
    @Autowired
    private CompaniesService companiesService;
    
    @Override
    public void run(String... args) throws Exception {

        Company company = this.companiesService.findById(UUID.fromString("922fb7dd-95cd-4266-aad9-c6f734f8386c"));

        this.seedDataService.seedStandardChecklists(company);
        
    }
    
}
