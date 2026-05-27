package giuseppetavella.zero_chiamate.infrastructure.jobs.jobs.send_admin_discrepancies;

import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddressesService;
import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationsService;
import giuseppetavella.zero_chiamate.domain.entities.shifts.ShiftsService;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.ClientAddressDiscrepancyDTO;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.notifications.Notification;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationType;
import giuseppetavella.zero_chiamate.helpers.AuthorizationHelper;
import giuseppetavella.zero_chiamate.helpers.TimeHelper;
import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.ContractDiscrepancyEmailSender;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionItem;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionMetadata;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutor;
import giuseppetavella.zero_chiamate.infrastructure.jobs.jobs.JobName;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.to_send.ClientAddressToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.shifts.dto.to_send.ShiftToSendDTO;
import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.ContractDiscrepancyAIDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class SendAdminDiscrepancies_JobExecutor extends JobExecutor<User> {
    
    @Autowired
    private SendAdminDiscrepancies_Repository thisRepository;
    
    @Autowired
    private EmailService appEmailService;
    
    @Autowired
    private NotificationsService notificationsService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private CompaniesService companiesService;
    
    @Autowired
    private ContractDiscrepancyAIDetectionService contractDiscrepancyAIDetectionService;
    
    @Autowired
    private ClientAddressesService clientAddressesService;
    
    @Autowired
    private ContractDiscrepancyEmailSender contractDiscrepancyEmailSender;
    
    
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
        
        // if this company has no client addresses, move on
        if(clientAddresses.isEmpty()) {
            return;
        }
        
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
                String discrepancyText = contractDiscrepancyAIDetectionService.findDiscrepancies(
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
        
        contractDiscrepancyEmailSender.send(
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
