package giuseppetavella.demo_login_system.runners;

import giuseppetavella.demo_login_system.services.AppEmailService;
import giuseppetavella.demo_login_system.services.AppPdfService;
import giuseppetavella.demo_login_system.services.base.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmailTestRunner implements CommandLineRunner {
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AppEmailService appEmailService;
    
    @Autowired
    private AppPdfService appPdfGenerationService;

    @Override
    public void run(String... args) throws Exception {
        
        // this.appEmailService.sendAdminWeeklyReport();

        // try {
        //    
        //     throw new RuntimeException("Hello Giuseppe, this is a mockup exception!");
        //    
        // } catch(RuntimeException ex) {
        //    
        //     this.appEmailService.sendEmailToDevForBackgroundJobProblem(
        //             "problem developer",
        //             "you had a problem with something",
        //             ex        
        //     );
        //    
        // }
        
        
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
        
        
    }
    
}
