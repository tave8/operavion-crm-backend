package giuseppetavella.zero_chiamate.domain.business.contract_discrepancy;

import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.ClientAddressDiscrepancyDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.helpers.DataValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.pdf.AppPdfService;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ContractDiscrepancyEmailService {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AppPdfService appPdfService;


    /**
     * Send the email that informs the admin of 
     * discrepancies (expectation vs reality) 
     * for each client address of their company.
     *
     */
    public void sendAdminDiscrepancies(User admin,
                                       List<ClientAddressDiscrepancyDTO> discrepancies,
                                       LocalDate startDate,
                                       LocalDate endDate)
    {

        // *****************
        // BUILD THE PDF
        // *****************

        // build the hashmap that gets passed to the html template
        // that will be turned into pdf

        // generate email attachment from pdf

        Map<String, Object> newPdfVars = Map.of(
                "discrepancies", discrepancies,
                "startDate", startDate,
                "endDate", endDate
        );


        // generate the pdf 
        Pdf pdf = this.appPdfService.generateAdminDiscrepancyReport(newPdfVars);
        String pdfAttachment = pdf.toAttachment();
        String pdfAttachmentName = "report_discrepanze_" + startDate + "_" + endDate + ".pdf";

        EmailAttachment attachment = new EmailAttachment(pdfAttachment, pdfAttachmentName);

        // *****************
        // BUILD THE EMAIL
        // **************

        // build the hashmap that gets passed to the html template
        // that will be sent as email
        Map<String, Object> emailTemplateVars = Map.of(
                "firstname", admin.getFirstname()
        );

        // the html template for the email, this will be filled
        String emailTemplate = "emails/admin_discrepancy_report";
        // the email subject
        String emailSubject = "Report discrepanze | Settimana " + startDate + " - " + endDate;


        // right before sending email, make sure you didn't forget
        // any variable to pass to html template

        DataValidationHelper.requireMapContainsOnlyKeys(
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


}
