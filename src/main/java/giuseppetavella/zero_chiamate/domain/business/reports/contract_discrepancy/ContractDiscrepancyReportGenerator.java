package giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy;

import giuseppetavella.zero_chiamate.exceptions.PdfGenerationException;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import giuseppetavella.zero_chiamate.infrastructure.pdf.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    

    private Map<String, Object> toTemplateVars(ContractDiscrepancyReportParams params) {
        return Map.of(
                "discrepancies", params.discrepancies(),
                "startDate", params.startDate(),
                "endDate", params.endDate()
        );
    }


}
