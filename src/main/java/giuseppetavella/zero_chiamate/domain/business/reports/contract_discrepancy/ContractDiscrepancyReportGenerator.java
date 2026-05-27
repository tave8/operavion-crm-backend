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
     * @param vars
     * @return
     * @throws PdfGenerationException
     */
    public Pdf generate(Map<String, ? extends Object> vars) throws PdfGenerationException
    {

        // require that the vars passed have these keys
        ValidationHelper.requireMapContainsOnlyKeys(
                vars,
                List.of("discrepancies", "startDate", "endDate")
        );

        return pdfService.templateToPdf("business/admin_discrepancy_report", vars);

    }


}
