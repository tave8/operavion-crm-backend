package giuseppetavella.demo_login_system.jobs.notify_admin_because_operator_has_no_shift;

import giuseppetavella.demo_login_system.entities.Notification;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.enums.NotificationType;
import giuseppetavella.demo_login_system.job_library.JobExecutionItem;
import giuseppetavella.demo_login_system.job_library.JobExecutionMetadata;
import giuseppetavella.demo_login_system.job_library.JobExecutor;
import giuseppetavella.demo_login_system.jobs.JobName;
import giuseppetavella.demo_login_system.payloads.in_response.ShiftToSendDTO;
import giuseppetavella.demo_login_system.services.AppEmailService;
import giuseppetavella.demo_login_system.services.NotificationsService;
import giuseppetavella.demo_login_system.services.ShiftsService;
import giuseppetavella.demo_login_system.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotifyAdminBecauseOperatorHasNoShift_JobExecutor extends JobExecutor<User> {
    
    @Autowired
    private NotifyAdminBecauseOperatorHasNoShift_Repository thisRepository;
    
    @Autowired
    private AppEmailService appEmailService;
    
    @Autowired
    private NotificationsService notificationsService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private UsersService usersService;
    
    
    public NotifyAdminBecauseOperatorHasNoShift_JobExecutor() {
        super(JobName.NOTIFY_ADMIN_BECAUSE_OPERATOR_HAS_NO_SHIFT);
    }
    
    
    @Override
    public void processItem(JobExecutionItem<?> itemToProcess, JobExecutionMetadata jobExecutionMetadata) {
        
        if (itemToProcess == null) {
            return;
        }
        
        User operator = (User) itemToProcess.getItem();
        
        // tomorrow
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        
        // get the shifts of this user
        List<ShiftToSendDTO> shiftsDTO = this.shiftsService.findShiftsByOperatorBetweenDatesDTO(operator, tomorrow, tomorrow);

        // if no shifts were found for this operator
        if(shiftsDTO.isEmpty()) {

            // add notification in DB

            // find admin of operator 
            // operator -> company -> admin 
            User admin = this.usersService.getAdminByCompany(operator.getCompany());
            
            
            Notification newNotification = new Notification(
                    admin,
                    NotificationType.OPERATOR_HAS_NO_SHIFT,
                    "L'operatore " + operator.getFullname() + " non ha un turno per domani.",
                    "<added by background job>"
            );
            
            //
            this.notificationsService.save(
                    newNotification
            );

            // send an email to admin
            this.appEmailService.sendEmail(
                    admin.getEmail(),
                    "Operatore non ha turno per domani",
                    "L'operatore " + operator.getFullname() + " non ha un turno per domani."
            );

            return;
            
        }
        
        
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
