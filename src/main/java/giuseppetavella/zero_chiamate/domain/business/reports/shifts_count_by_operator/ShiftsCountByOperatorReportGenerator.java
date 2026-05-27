package giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator;

import giuseppetavella.zero_chiamate.domain.business.Template;
import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.params.ContractDiscrepancyReportParams;
import giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator.params.ShiftsCountByOperatorReportParams;
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
public class ShiftsCountByOperatorReportGenerator {
    
    @Autowired
    private PdfService pdfService;
    
    
    public Pdf generate(ShiftsCountByOperatorReportParams params) throws PdfGenerationException
    {
        
        return pdfService.templateToPdf(
                Template.REPORT_SHIFTS_COUNT_BY_OPERATOR,
                toTemplateVars(params)
        );

    }

    
    /**
     * Generate the report attachment.
     *
     * @return
     */
    public EmailAttachment asAttachment(ShiftsCountByOperatorReportParams params)
    {

        var pdf = generate(params);

        var pdfAttachmentName = "report_turni_" + params.startDate() + "_" + params.endDate();

        return new EmailAttachment(
                pdf,
                pdfAttachmentName
        );

    }



    private Map<String, Object> toTemplateVars(ShiftsCountByOperatorReportParams params) {
        return Map.of(
                "shiftsCountByOperator", params.shiftsCountByOperator(),
                "startDate", params.startDate(),
                "endDate", params.endDate()
        );
    }
    

}
