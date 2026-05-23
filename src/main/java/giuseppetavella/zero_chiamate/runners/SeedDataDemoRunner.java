package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.business.auth.SeedDataOnSignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedDataDemoRunner implements CommandLineRunner {
    
    @Autowired
    private SeedDataOnSignupService seedDataOnSignupService;
    
    @Autowired
    private CompaniesService companiesService;
    
    @Override
    public void run(String... args) throws Exception {

        // Company company = this.companiesService.findById(UUID.fromString("922fb7dd-95cd-4266-aad9-c6f734f8386c"));
        //
        // this.seedDataService.seedStandardChecklists(company);
        
    }
    
}
