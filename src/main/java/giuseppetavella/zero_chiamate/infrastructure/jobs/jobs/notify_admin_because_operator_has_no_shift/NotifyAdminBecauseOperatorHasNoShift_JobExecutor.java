package giuseppetavella.zero_chiamate.infrastructure.jobs.jobs.notify_admin_because_operator_has_no_shift;

import giuseppetavella.zero_chiamate.config.AppEnvironment;
import giuseppetavella.zero_chiamate.domain.entities.notifications.Notification;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationType;
import giuseppetavella.zero_chiamate.infrastructure.email.params.EmailParams;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionItem;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutionMetadata;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecutor;
import giuseppetavella.zero_chiamate.infrastructure.jobs.jobs.JobName;
import giuseppetavella.zero_chiamate.domain.entities.shifts.dto.to_send.ShiftToSendDTO;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationsService;
import giuseppetavella.zero_chiamate.domain.entities.shifts.ShiftsService;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
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
    private EmailService emailService;
    
    @Autowired
    private NotificationsService notificationsService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private AppEnvironment appEnvironment;
    
    
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
                    "L'operatore " + operator.getFullname() + " non ha un turno per domani."
            ); 
            
            //
            this.notificationsService.save(
                    newNotification
            );

            
            if (appEnvironment.isLocal()) {
                // send an email to admin
                emailService.send(new EmailParams(
                        admin.getEmail(),
                        "Operatore non ha turno per domani [LOCAL ENV]",
                        "L'operatore " + operator.getFullname() + " non ha un turno per domani."
                ));
                
            } else {
                emailService.send(new EmailParams(
                        admin.getEmail(),
                        "Operatore non ha turno per domani",
                        "L'operatore " + operator.getFullname() + " non ha un turno per domani."
                ));
            }
            

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
