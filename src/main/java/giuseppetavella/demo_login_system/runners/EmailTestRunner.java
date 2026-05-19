package giuseppetavella.demo_login_system.runners;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.models.template_models.AdminWeeklyReportTemplateModel;
import giuseppetavella.demo_login_system.services.*;
import giuseppetavella.demo_login_system.services.base.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

@Component
public class EmailTestRunner implements CommandLineRunner {
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AppEmailService appEmailService;
    
    @Autowired
    private AppPdfService appPdfGenerationService;
    
    @Autowired
    private CompaniesService companiesService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private UsersService usersService;

    @Override
    public void run(String... args) throws Exception {



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
