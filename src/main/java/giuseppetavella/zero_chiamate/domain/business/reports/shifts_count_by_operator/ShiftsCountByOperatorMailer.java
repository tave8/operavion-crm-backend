package giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator;

import giuseppetavella.zero_chiamate.domain.business.Template;
import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.params.ContractDiscrepancyEmailParams;
import giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator.params.ShiftsCountByOperatorEmailParams;
import giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator.params.ShiftsCountByOperatorReportParams;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class ShiftsCountByOperatorMailer {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ShiftsCountByOperatorReportGenerator reportGenerator;
    

    public void send(User admin,
                     Map<User, Integer> shiftsCountByOperator,
                     LocalDate startDate,
                     LocalDate endDate)
    {
        
        var reportParams = new ShiftsCountByOperatorReportParams(
                shiftsCountByOperator, startDate, endDate
        );
        
        var emailParams = new ShiftsCountByOperatorEmailParams(
                admin.getFirstname()
        );
        
        var attachment = reportGenerator.asAttachment(reportParams);
        
        var subject = "Report turni | Settimana " + startDate + " - " + endDate;

        emailService.sendEmailFromTemplate(
                Template.EMAIL_SHIFTS_COUNT_BY_OPERATOR,
                toTemplateVars(emailParams),
                admin.getEmail(),
                subject,
                attachment
        );
        

    }


    /**
     * Generate the email params.
     *
     * @return
     */
    private Map<String, Object> toTemplateVars(ShiftsCountByOperatorEmailParams params) {
        return Map.of(
               "firstname", params.firstname() 
        );
    }


}
