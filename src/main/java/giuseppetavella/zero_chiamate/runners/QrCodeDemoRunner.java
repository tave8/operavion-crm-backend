package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email.params.EmailParams;
import giuseppetavella.zero_chiamate.infrastructure.email.params.TestEmailParams;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.qr_code.QrCodeService;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class QrCodeDemoRunner implements CommandLineRunner {

    @Autowired
    private QrCodeService qrCodeService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private EmailService emailService;

    @Override
    public void run(String... args) throws Exception {
    
        var qrBytes = qrCodeService.generate("secret content", "zerochiamate.com");

        // System.out.println(qrBytes);
        emailService.send(new TestEmailParams(
                new EmailAttachment(qrBytes, "qrcode.png")
        ));
        
        
    }
    
    
}
