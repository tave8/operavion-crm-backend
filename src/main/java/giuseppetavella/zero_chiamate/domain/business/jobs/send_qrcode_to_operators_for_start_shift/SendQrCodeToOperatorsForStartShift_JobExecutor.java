package giuseppetavella.zero_chiamate.domain.business.jobs.send_qrcode_to_operators_for_start_shift;

import giuseppetavella.zero_chiamate.config.AppEnvironment;
import giuseppetavella.zero_chiamate.domain.business.AppQrCodeGenerator;
import giuseppetavella.zero_chiamate.domain.business.jobs.JobName;
import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.ContractDiscrepancyDetector;
import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.ContractDiscrepancyMailer;
import giuseppetavella.zero_chiamate.domain.business.reports.qrcode_to_operators_for_start_shift.QrCodeToOperatorsForStartShiftMailer;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddressesService;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.ClientAddressDiscrepancyDTO;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.to_send.ClientAddressToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.entities.notifications.Notification;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationType;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationsService;
import giuseppetavella.zero_chiamate.domain.entities.shifts.ShiftsService;
import giuseppetavella.zero_chiamate.domain.entities.shifts.dto.to_send.ShiftToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.helpers.AuthorizationHelper;
import giuseppetavella.zero_chiamate.helpers.TimeHelper;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecution;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionItem;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionMetadata;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SendQrCodeToOperatorsForStartShift_JobExecutor extends JobExecutor<User> {
    
    @Autowired
    private SendQrCodeToOperatorsForStartShift_Repository repo;
    
    @Autowired
    private NotificationsService notificationsService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private CompaniesService companiesService;
    
    @Autowired
    private ContractDiscrepancyDetector contractDiscrepancyDetector;
    
    @Autowired
    private ClientAddressesService clientAddressesService;
    
    @Autowired
    private ContractDiscrepancyMailer contractDiscrepancyMailer;
    
    @Autowired
    private AppEnvironment appEnvironment;
    
    @Autowired
    private AppQrCodeGenerator appQrCodeGenerator;
    
    @Autowired
    private QrCodeToOperatorsForStartShiftMailer qrCodeToOperatorsForStartShiftMailer;
    
    
    public SendQrCodeToOperatorsForStartShift_JobExecutor() {
        super(JobName.SEND_QRCODE_TO_OPERATORS_FOR_START_SHIFT);
    }
    
    
    @Override
    public void processItem(JobExecutionItem<?> itemToProcess, JobExecution jobExecution) 
    {
        
        if (itemToProcess == null) {
            return;
        }
        
        var metadata = jobExecution.getMetadata();

        metadata.getExtra().put("name", "Giuseppe");
        
        jobExecution.setMetadata(metadata);
        
        var me = usersService.findById("6fb45429-c452-41f8-a214-e7d79d6c3e68");
        
        me.setFirstname("GIUSEPPE MODIFIED 3");
        
        
        
        var operator = (User) itemToProcess.getItem();
        
        throw new RuntimeException("error on purpose");
        
        // var admin = usersService.getAdminByCompany(operator.getCompany());
        //
        // qrCodeToOperatorsForStartShiftMailer.send(
        //         operator,
        //         admin
        // ); 
        
        
    }
    

    @Override
    public JobExecutionItem<User> getNextItem() {
        
        var maybeNextUser = repo.getNextItem(
                getJobName().name()
        );
        
        // null tells the job manager to stop getting next non-processed item 
        // this means, job is done
        if(maybeNextUser.isEmpty()) {
            return null; 
        }
        
        var user = maybeNextUser.get();
        
        return new JobExecutionItem<>(user, user.getId());
        
    }

    @Override
    public JobExecutionItem<User> getItemByIdOnIncompleteExecution(UUID itemId) {
        
        var maybeNextUser = repo.getItemByIdOnIncompleteExecution(itemId);


        // null tells the job manager to stop getting next incomplete item 
        // this means, incomplete job executions processing is done
        if(maybeNextUser.isEmpty()) {
            return null;
        }

        var user = maybeNextUser.get();

        return new JobExecutionItem<>(user, user.getId());
        
    }


}
