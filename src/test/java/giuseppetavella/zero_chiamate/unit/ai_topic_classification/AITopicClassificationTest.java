package giuseppetavella.zero_chiamate.unit.ai_topic_classification;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.DocumentTopicClassifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AITopicClassificationTest {

    @Autowired
    private DocumentTopicClassifier documentTopicClassifier;
    

    @Test
    public void itsAboutLegalContract() {
        byte[] bytes = FileHelper.readFile("extra/cleaning_contract.pdf");

        var classification = documentTopicClassifier.classifyFromFirstLines(bytes, "a legal contract");

        assertTrue(classification.isExpectedTopic());
    }


    @Test
    public void itsNotAboutLegalContract() {
        byte[] bytes = FileHelper.readFile("extra/invoice.pdf");

        var classification = documentTopicClassifier.classifyFromFirstLines(bytes, "a legal contract");

        assertFalse(classification.isExpectedTopic());
    }


    @Test
    public void itsAboutInvoice() {
        byte[] bytes = FileHelper.readFile("extra/invoice.pdf");

        var classification = documentTopicClassifier.classifyFromFirstLines(bytes, "an invoice");

        assertTrue(classification.isExpectedTopic());
    }
    
    
    @Test
    public void itsNotAboutInvoice() {
        byte[] bytes = FileHelper.readFile("extra/cleaning_contract.pdf");
        
        var classification = documentTopicClassifier.classifyFromFirstLines(bytes, "an invoice");
        
        assertFalse(classification.isExpectedTopic());
    }

}
