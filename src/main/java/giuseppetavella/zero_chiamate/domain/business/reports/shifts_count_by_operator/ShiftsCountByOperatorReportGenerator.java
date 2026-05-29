package giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator;

import giuseppetavella.zero_chiamate.config.EmailTemplate;
import giuseppetavella.zero_chiamate.config.ReportTemplate;
import giuseppetavella.zero_chiamate.domain.business.reports.shifts_count_by_operator.params.ShiftsCountByOperatorReportParams;
import giuseppetavella.zero_chiamate.infrastructure.csv.Csv;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.infrastructure.pdf.Pdf;
import giuseppetavella.zero_chiamate.infrastructure.pdf.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShiftsCountByOperatorReportGenerator {
    
    @Autowired
    private PdfService pdfService;


    /**
     * Same report, different format. This is a csv.
     * 
     * @param params
     * @return
     */
    public Csv generate(ShiftsCountByOperatorReportParams params) 
    {
        
        var csv = new Csv(List.of("Operatore", "Numero turni"));
        
        for(var user : params.shiftsCountByOperator().keySet()) {
            csv.addRow(
                    user.getFullname(),
                    params.shiftsCountByOperator().get(user).toString()
            );
        }
        
        return csv;
    }


    /**
     * Same report, different format. This is a pdf.
     * 
     * @param params
     * @return
     */
    public Pdf generatePdf(ShiftsCountByOperatorReportParams params)
    {
        
        return pdfService.templateToPdf(
                ReportTemplate.SHIFTS_COUNT_BY_OPERATOR,
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

        var csv = generate(params);

        var pdfAttachmentName = "report_turni_" + params.startDate() + "_" + params.endDate();

        return new EmailAttachment(
                csv,
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
