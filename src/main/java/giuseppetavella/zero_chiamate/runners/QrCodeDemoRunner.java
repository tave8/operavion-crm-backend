package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.business.AppQrCodeGenerator;
import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.UploadedFilesService;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email.params.EmailParams;
import giuseppetavella.zero_chiamate.infrastructure.email.params.TestEmailParams;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.qr_code.QrCodeService;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileStorageService;
import giuseppetavella.zero_chiamate.security.TokenTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class QrCodeDemoRunner implements CommandLineRunner {

    @Autowired
    private QrCodeService qrCodeService;
    
    @Autowired
    private UploadedFilesService uploadedFilesService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AppQrCodeGenerator appQrCodeGenerator;
    
    @Autowired
    private TokenTools tokenTools;

    @Override
    public void run(String... args) throws Exception {
    
        // var qrBytes = qrCodeService.generate("secret content", "zerochiamate.com");
        //
        // var uploadedFileDTO = uploadedFilesService.uploadDTO(qrBytes, "qrcode.png");
        //
        // var downloadedFileDTO = uploadedFilesService.downloadDTO(uploadedFileDTO.id());
        //
        //
        // // System.out.println(uploadedFileDTO.);
        //
        // // System.out.println(fileId);
        //
        // // uploadResult.filename();
        // //
        // // // System.out.println(qrBytes);
        
        // var publicQrCode = appQrCodeGenerator.generatePublicForLoginPage();
        //
        // emailService.send(new TestEmailParams(
        //         "<a href='"+publicQrCode.url()+"'>see qr code</a>",
        //         new EmailAttachment(publicQrCode.bytes(), publicQrCode.originalFilename())
        // ));

        // System.out.println(tokenTools.generateToken("hello"));
        
        startOperatorShift();
    }
    
    
    public void startOperatorShift() {
        
        var qrCode = appQrCodeGenerator.generatePrivateForStartOperatorShift();
        
        // qrCode.fileId();

        emailService.send(new TestEmailParams(
                        "file ID: " + qrCode.fileId(),
                new EmailAttachment(qrCode.bytes(), qrCode.originalFilename())
        ));
        
    }
    
    
}
