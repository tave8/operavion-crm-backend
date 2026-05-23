package giuseppetavella.demo_login_system.runners;

import giuseppetavella.demo_login_system.infrastructure.pdf.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PdfRunner implements CommandLineRunner {
    
    @Autowired
    private PdfService pdfService;
    
    @Override
    public void run(String... args) throws Exception {
        
        // this.pdfGenerationService.generateInvoice();
        
    }
}
