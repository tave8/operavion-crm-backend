package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.params.ContractDiscrepancyEmailParams;
import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.params.ContractDiscrepancyReportParams;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.ClientAddressDiscrepancyDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ContractDiscrepancyMailer {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ContractDiscrepancyReportGenerator reportGenerator;


    /**
     * Send the email that informs the admin of 
     * discrepancies (expectation vs reality) 
     * for each client address of their company.
     *
     */
    public void send(User admin,
                       List<ClientAddressDiscrepancyDTO> discrepancies,
                       LocalDate startDate,
                       LocalDate endDate)
    {
        
        var attachment = reportGenerator.asAttachment(discrepancies, startDate, endDate);
        
        var params = new ContractDiscrepancyEmailParams(
                admin.getFirstname()
        ); 
        
        var template = "emails/admin_discrepancy_report";
        
        var subject = "Report discrepanze | Settimana " + startDate + " - " + endDate;

        emailService.sendEmailFromTemplate(
                template,
                toTemplateVars(params),
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
    private Map<String, Object> toTemplateVars(ContractDiscrepancyEmailParams params) {
        return Map.of(
                "firstname", params.firstname()
        );
    }
    




}
