package giuseppetavella.zero_chiamate.infrastructure.pdf;

import giuseppetavella.zero_chiamate.exceptions.PdfGenerationException;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AppPdfService extends PdfService {

    /**
     * Upload an invoice.
     */
    // public String uploadInvoice(Map<String, Object> vars) throws PdfGenerationException, 
    //                                                              InvalidFileUploadedException,
    //                                                              FileUploadException
    // {
    //    
    //     return this.pdfToUpload("business/invoice", vars);
    //    
    // }

    /**
     * Save an invoice locally.
     */
    // public void saveInvoiceLocal(Map<String, Object> vars, String filename) throws PdfGenerationException
    // {
    //
    //     this.pdfToSaveLocal("business/invoice", vars, "/output", filename);
    //
    // }

    public Pdf generateAdminWeeklyReport(Map<String, ? extends Object> vars) throws PdfGenerationException
    {

        // require that the vars passed have these keys
        ValidationHelper.requireMapContainsOnlyKeys(
                vars, 
                List.of("shiftsCountByOperator", "startDate", "endDate")
        );

        return new Pdf(
                this.templateToPdf("business/admin_weekly_report", vars)
        );

    }

    public Pdf generateAdminDiscrepancyReport(Map<String, ? extends Object> vars) throws PdfGenerationException
    {

        // require that the vars passed have these keys
        ValidationHelper.requireMapContainsOnlyKeys(
                vars,
                List.of("discrepancies", "startDate", "endDate")
        );

        return new Pdf(
                this.templateToPdf("business/admin_discrepancy_report", vars)
        );

    }



    public Pdf generateInvoice(Map<String, Object> vars) throws PdfGenerationException
    {

        return new Pdf(
                this.templateToPdf("business/invoice", vars)
        );

    }
    
    

}
