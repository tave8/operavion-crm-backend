package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.ClientAddressDiscrepancyDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ContractDiscrepancyEmailSender {

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
        
        var attachment = generateReportAttachment(discrepancies, startDate, endDate);
        
        // *****************
        // BUILD THE EMAIL
        // **************

        // build the hashmap that gets passed to the html template
        // that will be sent as email
        Map<String, Object> emailTemplateVars = Map.of(
                "firstname", admin.getFirstname()
        );

        // the html template for the email, this will be filled
        var emailTemplate = "emails/admin_discrepancy_report";
        // the email subject
        var emailSubject = "Report discrepanze | Settimana " + startDate + " - " + endDate;


        // right before sending email, make sure you didn't forget
        // any variable to pass to html template

        ValidationHelper.requireMapContainsOnlyKeys(
                emailTemplateVars,
                List.of("firstname")
        );

        emailService.sendEmailFromTemplate(
                emailTemplate,
                emailTemplateVars,
                admin.getEmail(),
                emailSubject,
                attachment
        );

    }

    /**
     * Generate the report attachment.
     * 
     * @return
     */
    private EmailAttachment generateReportAttachment(List<ClientAddressDiscrepancyDTO> discrepancies,
                                                     LocalDate startDate,
                                                     LocalDate endDate)
    {
        
        var pdfParams = new ContractDiscrepancyReportParams(
                discrepancies,
                startDate,
                endDate
        );
        
        var pdf = reportGenerator.generate(pdfParams);

        var pdfAttachmentName = "report_discrepanze_" + startDate + "_" + endDate;

        return new EmailAttachment(
                pdf,
                pdfAttachmentName
        );
        
    }


}
