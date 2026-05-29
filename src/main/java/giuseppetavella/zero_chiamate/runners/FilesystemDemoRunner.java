package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.ai.AIService;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FilesystemDemoRunner implements CommandLineRunner {
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AIService aiService;

    @Override
    public void run(String... args) throws Exception {
    
        var bytes = FileHelper.readFile("extra/invoice.pdf");
        //
        var answer = aiService.askWithPdf(bytes, "summarize what's inside. be very concise. provide minimal information.");
        //
        emailService.sendEmail(
                "giuseppetavella8@gmail.com",
                "hi",
                answer

        );
        
        
        // System.out.println(bytes);
        
    }
    
}
