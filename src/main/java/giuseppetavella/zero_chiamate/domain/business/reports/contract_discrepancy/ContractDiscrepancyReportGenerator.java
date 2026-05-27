package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import giuseppetavella.zero_chiamate.config.Template;
import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.params.ContractDiscrepancyReportParams;
import giuseppetavella.zero_chiamate.exceptions.PdfGenerationException;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import giuseppetavella.zero_chiamate.infrastructure.pdf.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                Template.REPORT_CONTRACT_DISCREPANCY, 
                toTemplateVars(params)
        );
 
    }


    /**
     * Generate the report attachment.
     *
     * @return
     */
    public EmailAttachment asAttachment(ContractDiscrepancyReportParams params)
    {
        
        var pdf = generate(params);

        var pdfAttachmentName = "report_discrepanze_" + params.startDate() + "_" + params.endDate();

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
