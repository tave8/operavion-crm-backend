package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.business.reports.operators_by_company.OperatorsByCompanyReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PdfRunner implements CommandLineRunner {
    
    @Autowired
    private OperatorsByCompanyReportGenerator operatorsByCompanyReportGenerator;
    
    @Override
    public void run(String... args) throws Exception {
        
        // operatorsByCompanyReportGenerator.generate();
        
        // this.pdfGenerationService.generateInvoice();
        
    }
}
