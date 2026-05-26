package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.infrastructure.csv.UsersCsvGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/csv-generation")
public class CsvGenerationController {

    @Autowired
    private UsersCsvGenerationService appCsvGenerationService;
    
    // @PostMapping("/upload-invoice")
    // public String uploadInvoice() {
    //
    //     Map<String, Object> vars = Map.of(
    //             "firstname", "Giuseppe"
    //     );
    //            
    //     String fileUrl = this.appPdfGenerationService.uploadInvoice(vars);
    //    
    //     return  fileUrl;
    // }

    /**
     * 
     */
    // @PostMapping("/save-invoice-local")
    // public String saveInvoiceLocal(@RequestParam(value = "filename",defaultValue = "invoice.csv") String filename) {
    //
    //     Map<String, Object> vars = Map.of(
    //             "firstname", "Giuseppe"
    //     );
    //
    //     this.appCsvGenerationService.saveInvoiceLocal(vars, filename);
    //
    //     return "Invoice saved locally";
    // }



}
