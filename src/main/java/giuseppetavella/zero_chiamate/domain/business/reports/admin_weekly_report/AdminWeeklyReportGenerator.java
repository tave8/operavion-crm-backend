package giuseppetavella.zero_chiamate.domain.business.reports.admin_weekly_report;

import giuseppetavella.zero_chiamate.exceptions.PdfGenerationException;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import giuseppetavella.zero_chiamate.infrastructure.pdf.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminWeeklyReportGenerator {
    
    @Autowired
    private PdfService pdfService;
    
    
    public Pdf generate(Map<String, ? extends Object> vars) throws PdfGenerationException
    {

        // require that the vars passed have these keys
        ValidationHelper.requireMapContainsOnlyKeys(
                vars,
                List.of("shiftsCountByOperator", "startDate", "endDate")
        );

        return pdfService.templateToPdf("business/admin_weekly_report", vars);

    }
    

}
