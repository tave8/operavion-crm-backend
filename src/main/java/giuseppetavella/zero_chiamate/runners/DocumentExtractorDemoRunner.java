package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.ContractDiscrepancyDetector;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.utils.DocumentTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Demo: deterministic, AI-free text extraction from a PDF.
 *
 * Reads extra/invoice.pdf from resources, extracts its text layer with
 * DocumentTextExtractor (Apache Tika, no AI), and prints the result.
 */
@Component
public class DocumentExtractorDemoRunner implements CommandLineRunner {

    @Autowired
    private DocumentTextExtractor documentTextExtractor;
    
    @Autowired
    private ContractDiscrepancyDetector contractDiscrepancyDetector;
    

    @Override
    public void run(String... args) throws Exception {

        // resources/extra/invoice.pdf -> bytes
        // byte[] invoiceBytes = FileHelper.readFile("extra/cleaning_contract.pdf");
        //
        // // bytes -> plain text (deterministic, no AI)
        // String text = documentTextExtractor.bytesToText(invoiceBytes, 100);
        //
        // System.out.println("file is empty? " + (text.isEmpty()));
        // System.out.println("=== extracted text ===");
        // System.out.println(text);
        // System.out.println("=== end ===");
        
        
        // var classification = contractDiscrepancyDetector.classify(invoiceBytes);
        //
        // System.out.println("IS CONTRACT: " + classification.isContract());
        // System.out.println("WHAT IF NOT CONTRACT: " + classification.whatIfNotContract());

    }

}