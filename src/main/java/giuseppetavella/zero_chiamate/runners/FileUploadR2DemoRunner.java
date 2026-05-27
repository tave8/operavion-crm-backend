package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.business.reports.operators_by_company.OperatorsByCompanyReportGenerator;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FileUploadR2DemoRunner implements CommandLineRunner {
    
    @Autowired
    private FileUploadService fileUploadService;
    
    @Autowired
    private OperatorsByCompanyReportGenerator appCsvGenerationService;
    

    
    @Autowired
    private EmailService appEmailService;

    @Override
    public void run(String... args) throws Exception {

        //  byte[] csvBytes = this.appCsvGenerationService.generateArticlesReport();
        //
        //  byte[] pdfBytes = this.appPdfGenerationService.generateInvoice(Map.of());
        //
        //
        // String pdfUrl = this.fileUploadService.upload(pdfBytes, "pdf");
        //
        // System.out.println(pdfUrl);
        //
        // this.appEmailService.sendPdf("giuseppetavella8@gmail.com", pdfUrl);
        //
    }
}




