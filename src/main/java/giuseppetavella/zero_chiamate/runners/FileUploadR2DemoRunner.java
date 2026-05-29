package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.business.reports.operators_by_company.OperatorsByCompanyReportGenerator;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email.params.EmailParams;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileStorageService;
import giuseppetavella.zero_chiamate.integrations.cloudflare_r2.CloudflareR2APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FileUploadR2DemoRunner implements CommandLineRunner {
    
    @Autowired
    private CloudflareR2APIService cloudflareR2APIService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private EmailService emailService;

    @Override
    public void run(String... args) throws Exception {
        
        var bytes = cloudflareR2APIService.download("d5d29ab6-e558-4c93-b879-85a264b79ecf.png");

        emailService.send(new EmailParams(
                "giuseppetavella8@gmail.com",
                "my linkedin",
                "something",
                new EmailAttachment(bytes)
        ));
        
        // System.out.println(bytes);
        
        // var bytes = FileHelper.readFile("extra/linkedin.png");
        //
        // var fileUrl = fileStorageService.upload(bytes);
        //
        // System.out.println(fileUrl);

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




