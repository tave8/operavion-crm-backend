package giuseppetavella.demo_login_system.runners;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.models.template_models.AdminWeeklyReportTemplateModel;
import giuseppetavella.demo_login_system.services.*;
import giuseppetavella.demo_login_system.services.base.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

        Company company = this.companiesService.findById("922fb7dd-95cd-4266-aad9-c6f734f8386c");
        
        User admin = this.usersService.getAdminByCompany(company);

        LocalDate today = LocalDate.now().plusDays(1);
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        Map<User, Integer> shiftsCountByOperator  = this.shiftsService.countShiftsByOperator(company, today, tomorrow);
        //
        // for(User user : userCountMap.keySet()) {
        //     System.out.println("user: " + user.getFullname() + " | count: " + userCountMap.get(user));
        // }
        
        this.appEmailService.sendAdminWeeklyReport(admin.getEmail(), shiftsCountByOperator);

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
