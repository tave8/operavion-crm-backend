package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.params.ContractDiscrepancyReportParams;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.ClientAddressDiscrepancyDTO;
import giuseppetavella.zero_chiamate.exceptions.PdfGenerationException;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import giuseppetavella.zero_chiamate.infrastructure.pdf.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ContractDiscrepancyReportGenerator {

    @Autowired
    private PdfService pdfService;
    

    /**
     * Generate contract discrepancy report.
     * 
     * @return
     * @throws PdfGenerationException
     */
    public Pdf generate(ContractDiscrepancyReportParams params) throws PdfGenerationException
    {
        
        return pdfService.templateToPdf(
                "business/admin_discrepancy_report", 
                toTemplateVars(params)
        );
 
    }


    /**
     * Generate the report attachment.
     *
     * @return
     */
    public EmailAttachment asAttachment(List<ClientAddressDiscrepancyDTO> discrepancies,
                                        LocalDate startDate,
                                        LocalDate endDate)
    {

        var pdfParams = new ContractDiscrepancyReportParams(
                discrepancies,
                startDate,
                endDate
        );

        var pdf = generate(pdfParams);

        var pdfAttachmentName = "report_discrepanze_" + startDate + "_" + endDate;

        return new EmailAttachment(
                pdf,
                pdfAttachmentName
        );

    }
    

    private Map<String, Object> toTemplateVars(ContractDiscrepancyReportParams params) {
        return Map.of(
                "discrepancies", params.discrepancies(),
                "startDate", params.startDate(),
                "endDate", params.endDate()
        );
    }


}
