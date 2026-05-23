package giuseppetavella.demo_login_system.runners;

import giuseppetavella.demo_login_system.domain.entities.client_addresses.ClientAddressesService;
import giuseppetavella.demo_login_system.domain.entities.companies.CompaniesService;
import giuseppetavella.demo_login_system.domain.entities.shifts.ShiftsService;
import giuseppetavella.demo_login_system.domain.entities.users.UsersService;
import giuseppetavella.demo_login_system.infrastructure.email.EmailService;
import giuseppetavella.demo_login_system.infrastructure.pdf.AppPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmailTestRunner implements CommandLineRunner {
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private EmailService appEmailService;
    
    @Autowired
    private AppPdfService appPdfGenerationService;
    
    @Autowired
    private CompaniesService companiesService;
    
    @Autowired
    private ClientAddressesService clientAddressesService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private UsersService usersService;

    @Override
    public void run(String... args) throws Exception {

        // User admin = this.usersService.findById("d561f16f-095d-4ad3-8f2c-4f0ad3429efc");
        //
        // ClientAddress clientAddress1 = this.clientAddressesService.findById("7ff21f6c-5b9f-4238-9dea-8acc53930ae7");
        // ClientAddress clientAddress2 = this.clientAddressesService.findById("7bd1abda-6c83-40cb-96cf-cf4c7fc144aa");
        //
        // List<ClientAddressDiscrepancyDTO> discrepancies = List.of(
        //         new ClientAddressDiscrepancyDTO(clientAddress1, "mancano 2 turni"),
        //         new ClientAddressDiscrepancyDTO(clientAddress2, "ci sono troppo persone, potresti bilanciare i turni")
        // );
        //
        // LocalDate startDate = LocalDate.now();
        // LocalDate endDate = LocalDate.now();
        //
        // this.appEmailService.sendAdminDiscrepancies(
        //         admin,
        //         discrepancies,
        //         startDate,
        //         endDate
        // );

        
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
