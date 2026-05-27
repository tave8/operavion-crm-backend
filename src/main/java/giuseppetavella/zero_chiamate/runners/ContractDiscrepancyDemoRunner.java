package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.ContractDiscrepancyReportGenerator;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobManager;
import giuseppetavella.zero_chiamate.infrastructure.jobs.jobs.JobName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ContractDiscrepancyDemoRunner implements CommandLineRunner {
    
    @Autowired
    private ContractDiscrepancyReportGenerator reportGenerator;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private JobManager jobManager;
    
    @Override
    public void run(String... args) throws Exception {
            
        // var pdf = reportGenerator.generate(new ContractDiscrepancyReportParams(
        //         List.of(),
        //         LocalDate.now(),
        //         LocalDate.now().plusDays(1)
        // ));
        //
        // emailService.sendEmail(
        //         "giuseppetavella8@gmail.com",
        //         "Your discrepancy report",
        //         "Your report",
        //         new EmailAttachment(pdf, "discrepancy_report")
        // );
        
        // jobManager.executeJob(JobName.SEND_ADMIN_DISCREPANCIES);
        
    }
    
    
}
