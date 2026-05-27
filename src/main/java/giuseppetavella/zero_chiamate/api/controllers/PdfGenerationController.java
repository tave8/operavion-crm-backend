package giuseppetavella.zero_chiamate.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pdf-generation")
public class PdfGenerationController {


    
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
    // public String saveInvoiceLocal(@RequestParam(value = "filename",defaultValue = "invoice.pdf") String filename) {
    //
    //     Map<String, Object> vars = Map.of(
    //             "firstname", "Giuseppe"
    //     );
    //
    //     this.appPdfGenerationService.saveInvoiceLocal(vars, filename);
    //
    //     return "Invoice saved locally";
    // }



}
