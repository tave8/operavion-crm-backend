package giuseppetavella.demo_login_system.jobs.send_admin_discrepancies;

import giuseppetavella.demo_login_system.dto.ClientAddressDiscrepancyDTO;
import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.Notification;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.enums.NotificationType;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.helpers.TimeHelper;
import giuseppetavella.demo_login_system.job_library.JobExecutionItem;
import giuseppetavella.demo_login_system.job_library.JobExecutionMetadata;
import giuseppetavella.demo_login_system.job_library.JobExecutor;
import giuseppetavella.demo_login_system.jobs.JobName;
import giuseppetavella.demo_login_system.payloads.in_response.ClientAddressToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ShiftToSendDTO;
import giuseppetavella.demo_login_system.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class SendAdminDiscrepancies_JobExecutor extends JobExecutor<User> {
    
    @Autowired
    private SendAdminDiscrepancies_Repository thisRepository;
    
    @Autowired
    private AppEmailService appEmailService;
    
    @Autowired
    private NotificationsService notificationsService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private CompaniesService companiesService;
    
    @Autowired
    private AppAIService appAIService;
    
    @Autowired
    private ClientAddressesService clientAddressesService;
    
    
    public SendAdminDiscrepancies_JobExecutor() {
        super(JobName.SEND_ADMIN_DISCREPANCIES);
    }
    
    
    @Override
    public void processItem(JobExecutionItem<?> itemToProcess, 
                            JobExecutionMetadata jobExecutionMetadata) 
    {
        
        if (itemToProcess == null) {
            return;
        }
        
        // ****************
        // CHECKS
        // ***************
        
        User admin = (User) itemToProcess.getItem();
        
        AuthorizationHelper.requireUserAdmin(admin);
        
        Company company = admin.getCompany();
        
        // *******************
        // CORE LOGIC: FIND DISCREPANCIES BY CLIENT ADDRESS
        // *******************

        LocalDate lastMonday = TimeHelper.lastMonday();
        LocalDate lastFriday = TimeHelper.lastFriday();

        // contains the client addresses, along with the AI-generated
        // discrepancy summary, to be emailed to the admin in a single pdf report
        List<ClientAddressDiscrepancyDTO> discrepanciesList = new ArrayList<>();
        
        // get all client addresess of this company
        List<ClientAddressToSendDTO> clientAddresses = clientAddressesService.findAllClientAddressesByCompany(company);
        
        // for each client address:
        //      get their contract expectation
        //      if contract expectation is success:
        //          get this client address shifts between the date range
        //          ask AI to find discrepancies between expected and actual 
        //      else:
        //          set the discrepancy for this client address as "non ci sono aspettative di contratto"
        for (ClientAddressToSendDTO ca : clientAddresses) {
            
            // the contract expectation for this client address exists 
            // and is successful (was successfully processed)
            if(ca.getContractExpectation().isSuccess()) {
        
                // get the shifts for this client addresss, in date range
                List<ShiftToSendDTO> shiftsDTO = shiftsService.findShiftsByClientAddressBetweenDatesDTO(
                        ca.getClientAddress(),
                        lastMonday,
                        lastMonday
                );
        
                // the shifts for this client address in date range, stringified
                String shiftsInfo = shiftsService.stringifyShifts(shiftsDTO);
                
                // the expectations of this client address' contract
                String expectations = ca.getContractExpectation().getDetail().getExpectations();
                
                // the AI generates the summary "expectation vs reality"
                String discrepancyText = appAIService.findDiscrepancies(
                        expectations,
                        shiftsInfo
                );
                
                

                // System.out.println("\n");
                // System.out.println("***************************");
                //
                // System.out.println(
                //         "CLIENT ADDRESS: " + ca.getClientAddress().getClientAndAddressName()
                // );
                //
                // System.out.println(
                //         "EXPECTATIONS: " + expectations
                // );
                //
                // System.out.println(
                //         "DISCREPANCY TEXT (AI): " + discrepancyText
                // );
                //
                // System.out.println("***************************");
                // System.out.println("\n");
                //
                

                ClientAddressDiscrepancyDTO discrepancy = new ClientAddressDiscrepancyDTO(
                        ca.getClientAddress(),
                        discrepancyText
                );

                discrepanciesList.add(discrepancy);
                

            } 
            // this client address has no successful contract expectation, 
            // or none at all
            else {

                String discrepancyText = "(Nessuna aspettativa di contratto)";
                
                ClientAddressDiscrepancyDTO discrepancy = new ClientAddressDiscrepancyDTO(
                        ca.getClientAddress(),
                        discrepancyText
                );

                discrepanciesList.add(discrepancy);
                
            }


        }
        
        // loop is finished, this means "all client addresses 
        // of a company have been processed"
        
        // *********************
        // SEND EMAIL & NOTIFY ADMIN 
        // ********************

        // add notification
        
        Notification newNotification = new Notification(
                admin,
                NotificationType.DISCREPANCY_REPORT_GENERATION_SUCCESS,
                "Report discrepanze pronto",
                "Il tuo report discrepanze settimanale è pronto."
        );

        // add a notification so admin sees processing is done    
        this.notificationsService.save(newNotification);
        
        // send email 
        
        this.appEmailService.sendAdminDiscrepancies(
                admin,
                discrepanciesList,
                lastMonday,
                lastFriday
        );


        // once we've processed all client addresses of this admin,
        //  this job execution will be marked as success
        
    }
    

    @Override
    public JobExecutionItem<User> getNextItem() {
        
        Optional<User> maybeNextUser = this.thisRepository.getNextItem(this.getJobName().name());
        
        if(maybeNextUser.isEmpty()) {
            return null; 
        }
        
        User user = maybeNextUser.get();
        
        return new JobExecutionItem<>(user, user.getId());
        
    }

    @Override
    public JobExecutionItem<User> getItemByIdOnIncompleteExecution(UUID itemId) {
        
        Optional<User> maybeNextUser = this.thisRepository.getItemByIdOnIncompleteExecution(itemId);

        if(maybeNextUser.isEmpty()) {
            return null;
        }

        User user = maybeNextUser.get();

        return new JobExecutionItem<>(user, user.getId());
        
    }


}
