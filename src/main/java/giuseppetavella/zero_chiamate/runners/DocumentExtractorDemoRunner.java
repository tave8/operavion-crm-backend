package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.ContractDiscrepancyDetector;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.utils.DocumentTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
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
        // byte[] bytes = FileHelper.readFile("extra/cat_image_as_pdf.pdf");
        // // //
        // // // // bytes -> plain text (deterministic, no AI)
        // String text = documentTextExtractor.bytesToText(bytes, 100);

        // System.out.println(text);
        
        //
        // var contractExpectations =  contractDiscrepancyDetector.extractContractExpectations(bytes);
        //
        // System.out.println(contractExpectations);
        
        // var classification = contractDiscrepancyDetector.classify(bytes);
        //
        // System.out.println(classification.isContract());
        // System.out.println(classification.whatIfNotContract());
        
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