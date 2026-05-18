package giuseppetavella.demo_login_system.jobs.email_operator_tomorrow_shift;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailOperatorTomorrowShift_JobExecutor extends JobExecutor<User> {
    
    @Autowired
    private EmailOperatorTomorrowShift_ItemRepository thisRepository;
    
    @Autowired
    private AppEmailService appEmailService;
    
    @Autowired
    private NotificationsService notificationsService;
    
    @Autowired
    private ShiftsService shiftsService;
    
    
    public EmailOperatorTomorrowShift_JobExecutor() {
        super(JobName.EMAIL_OPERATOR_TOMORROW_SHIFT);
    }
    
    
    @Override
    public void processItem(JobExecutionItem<?> itemToProcess, JobExecutionMetadata jobExecutionMetadata) {
        
        if (itemToProcess == null) {
            return;
        }
        
        // send email, do business-specific logic
        User user = (User) itemToProcess.getItem();
        
        // tomorrow
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        
        // get the shifts of this user
        List<ShiftToSendDTO> shiftsDTO = this.shiftsService.findShiftsByOperatorBetweenDatesDTO(user, tomorrow, tomorrow);

        // if no shifts were found for this operator
        if(shiftsDTO.isEmpty()) {

            // add notification in DB

            // Notification newNotification = new Notification(
            //         user,
            //         NotificationType.TOMORROW_SHIFT,
            //         "Ecco il tuo turno di domani...",
            //         "<added by background job>"
            // );
            //
            // this.notificationsService.save(
            //         newNotification
            // );
            
            return;
            
        }
        
        // if a shift was found for this operator 
        
        // if it exists, get the first shift
        ShiftToSendDTO shiftDTO = shiftsDTO.getFirst();
        
        // String clientName = shiftDTO.getClientAddress().getClientName();
        
        // add notification in DB

        Notification newNotification = new Notification(
                user,
                NotificationType.TOMORROW_SHIFT,
                "Il tuo turno per domani: " + shiftDTO.getName(),
                "<added by background job>"
        );
        
        this.notificationsService.save(
                newNotification
        );
        
        // send an email to the operator
        // this.appEmailService.sendEmail(
        //         user.getEmail(),
        //         "Il tuo turno per domani",
        //         "Ecco il tuo turno per domani: " + shiftDTO.getName()
        // );
        
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
