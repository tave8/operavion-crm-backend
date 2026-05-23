package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.pdf.AppPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmailAttachmentsDemoRunner implements CommandLineRunner {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailService appEmailService;

    @Autowired
    private AppPdfService appPdfGenerationService;

    @Override
    public void run(String... args) throws Exception {


        // String emailID = this.emailService.sendEmail("giuseppetavella8@gmail.com", "title!", "<i>whatsup</i>");

        // System.out.println(emailID);

        // User user = new User(
        //         "hunjsnajnsajkdna3923njnkjdnjkwendkjwnejkd@gmail.com",
        //         "1234",
        //         "Giuseppe",
        //         "Tavella"
        // );
        //
        // appEmailService.sendVerifyEmail(user);

        // Map<String, Object> vars = Map.of();
        //
        // String recipient = "giuseppetavella8@gmail.com";
        // String subject = "subject test";
        // String html = "<b>hello</b>";
        //
        // EmailAttachment emailAttachment1 = new EmailAttachment(
        //         this.appPdfGenerationService.generateInvoiceAttachment(vars),
        //         "i_called_this.pdf"
        // );
        //
        // EmailAttachment emailAttachment2 = new EmailAttachmentFromURL(
        //         "https://foodandfriends.org/wp-content/uploads/2021/12/CHEW-Lesson-Intuitive-Cooking.pdf",
        //         "i_called_this.pdf"
        // );
        //
        // List<EmailAttachment> emailAttachments = List.of(
        //         emailAttachment1, emailAttachment2
        // );
        //
        // this.emailService.sendEmailWithAttachments(
        //         recipient,
        //         subject,
        //         html,
        //         emailAttachments
        // );


    }

}
