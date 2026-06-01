package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.business.reports.contract_discrepancy.ContractDiscrepancyDetector;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.DocumentClassifier;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.DocumentTextExtractor;
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
    
    @Autowired
    private DocumentClassifier documentClassifier;
    

    @Override
    public void run(String... args) throws Exception {

        // resources/extra/invoice.pdf -> bytes
        // byte[] bytes = FileHelper.readFile("extra/cleaning_contract.docx");
        //
        // var classification = contractDiscrepancyDetector.classify(bytes);
        //
        // // var classification = documentClassifier.classifyFromFirstLines(bytes, "a");
        //
        // System.out.println("IS EXPECTED TOPIC? " + classification.isExpectedTopic());
        // System.out.println("IF NOT EXPECTED TOPIC, IT'S ABOUT: " + classification.whatIfNotExpectedTopic());
        
        // String text = documentTextExtractor.bytesToText(bytes, 100);

        // System.out.println(text);
        
        // var contractExpectations =  contractDiscrepancyDetector.extractContractExpectations(bytes);
        // //
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