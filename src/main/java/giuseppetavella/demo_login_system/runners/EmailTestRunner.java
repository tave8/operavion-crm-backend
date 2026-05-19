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

        Company company = this.companiesService.findById("922fb7dd-95cd-4266-aad9-c6f734f8386c");
        
        User admin = this.usersService.getAdminByCompany(company);

        // 1. Your reference date (Thursday, May 21, 2026)
        LocalDate referenceDate = LocalDate.now().plusDays(2);

        // 2. Jump back exactly 1 full week into last week (Thursday, May 14, 2026)
        LocalDate lastWeekTarget = referenceDate.minusWeeks(1);

        // 3. Securely snap to that week's Monday and Friday
        LocalDate lastMonday = lastWeekTarget.with(DayOfWeek.MONDAY); // Monday, May 11, 2026
        LocalDate lastFriday = lastWeekTarget.with(DayOfWeek.FRIDAY); // Friday, May 15, 2026
        Map<User, Integer> shiftsCountByOperator  = this.shiftsService.countShiftsByOperator(company, lastMonday, lastFriday);
        //
        // for(User user : userCountMap.keySet()) {
        //     System.out.println("user: " + user.getFullname() + " | count: " + userCountMap.get(user));
        // }
        
        this.appEmailService.sendAdminWeeklyReport(
                admin.getEmail(), 
                shiftsCountByOperator,
                lastMonday,
                lastFriday
        );

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
