package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.business.reports.operators_by_company.OperatorsByCompanyReportGenerator;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email.params.EmailParams;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileStorageService;
import giuseppetavella.zero_chiamate.integrations.cloudflare_r2.CloudflareR2APIService;
import org.apache.xmlgraphics.ps.PSImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

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
        
        
        // var invoicePath = "extra/invoice.pdf";
        // var linkedinBannerPath = "extra/linkedin.png";
        // var reportTurniPath = "extra/report_turni.csv";
        // var reportTurniExcelPath = "extra/report.xlsx";
        // var jwtPath = "extra/report_turni.csv";
        //
        // var invoiceBytes = FileHelper.readFile(invoicePath);
        // var linkedinBannerBytes = FileHelper.readFile(linkedinBannerPath);
        // var reportTurniBytes = FileHelper.readFile(reportTurniPath);
        // var reportTurniExcelBytes = FileHelper.readFile(reportTurniExcelPath);
        // var jwtBytes = FileHelper.readFile(jwtPath);
        //
        // var fileTypeInvoice = FileHelper.getFileType(invoiceBytes, "invoice.pdf");
        // var linkedinBannerFileType = FileHelper.getFileType(linkedinBannerBytes, "linkedin.png");
        // var reportTurniFileType = FileHelper.getFileType(reportTurniBytes, "report_turni.csv");
        // var reportTurniExcelFileType = FileHelper.getFileType(reportTurniExcelBytes, "report.xlsx");
        // var jwtFileType = FileHelper.getFileType(jwtBytes, "jwt.txt");
        // //
        // System.out.println(fileTypeInvoice);
        // System.out.println(linkedinBannerFileType);
        // System.out.println(reportTurniFileType);
        // System.out.println(reportTurniExcelFileType);
        // System.out.println(jwtFileType);

        
        
        
        // FileHelper.getFileType(in)
        
        
        // var reportTurniBytes = FileHelper.readFile("extra/report_turni.csv");
        //
        // var reportTurniFilenames = fileStorageService.upload(reportTurniBytes, "csv");
        // //
        // var reportTurniBytesDownloaded = fileStorageService.download(reportTurniFilenames.filename());
        // //
        // System.out.println(reportTurniFilenames.filename());
        // System.out.println(reportTurniFilenames.url());
        //
        //
        // emailService.send(new EmailParams(
        //         "giuseppetavella8@gmail.com",
        //         "my linkedin",
        //         "...",
        //         new EmailAttachment(reportTurniBytesDownloaded, "report.csv")
        // ));
        
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




