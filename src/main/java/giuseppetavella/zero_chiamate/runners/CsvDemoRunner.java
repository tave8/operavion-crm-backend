package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.business.reports.operators_by_company.OperatorsByCompanyReportGenerator;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CsvDemoRunner implements CommandLineRunner {

    @Autowired
    private OperatorsByCompanyReportGenerator operatorsByCompanyReportGenerator;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private CompaniesService companiesService;
    

    @Override
    public void run(String... args) throws Exception {

        var company = companiesService.getById("922fb7dd-95cd-4266-aad9-c6f734f8386c");
        
        var csv = operatorsByCompanyReportGenerator.generate(company);
        
        
        emailService.sendEmail(
                "giuseppetavella8@gmail.com",
                "Your report",
                "Your report",
                new EmailAttachment(csv, "report")
        );
        
        // CSV GENERATION

        // User user = new User(
        //         "giuseppetavella8+@gmail.com",
        //         "1234",
        //         "Giuseppe",
        //         "Tavella"
        // );
        //
        // this.appEmailService.sendArticlesReport(
        //         user,
        //         this.appCsvGenerationService.generateArticlesReport()
        // );


        // CSV UPLOAD
        
        // User user = new User(
        //         "giuseppetavella8+@gmail.com",
        //         "1234",
        //         "Giuseppe",
        //         "Tavella"
        // );
        //
        // String csvUrl = this.mediaUploadService.uploadFile(
        //         this.appCsvGenerationService.generateArticlesReport() 
        // );
        //
        // System.out.println(csvUrl);
        
    }
}
