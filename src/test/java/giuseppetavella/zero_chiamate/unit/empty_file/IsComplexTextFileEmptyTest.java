package giuseppetavella.zero_chiamate.unit.empty_file;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.DocumentTextExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * By complex text file we mean pdf, xlsx, docx,
 * whose number of bytes are never 0, even if the file
 * is "empty" at the text layer.
 * So we must extract the text to know if they're empty.
 */
@SpringBootTest
public class IsComplexTextFileEmptyTest {
    
    @Autowired
    private DocumentTextExtractor documentTextExtractor;
    
    @Test
    public void pdfFileIsEmptyWhenExtractText() {
        var bytes = FileHelper.readFile("extra/empty.pdf");
        
        var extracted = documentTextExtractor.extract(bytes);
        
        assertEquals(0, extracted.length());
    }


    @Test
    public void pdfFileIsNotEmptyWhenExtractText() {
        var bytes = FileHelper.readFile("extra/cleaning_contract.pdf");

        var extracted = documentTextExtractor.extract(bytes);

        assertNotEquals(0, extracted.length());
    }

    @Test
    public void docxFileIsEmptyWhenExtractText() {
        var bytes = FileHelper.readFile("extra/empty.docx");

        var extracted = documentTextExtractor.extract(bytes);

        assertEquals(0, extracted.length());
    }

    @Test
    public void docxFileIsNotEmptyWhenExtractText() {
        var bytes = FileHelper.readFile("extra/cleaning_contract.docx");

        var extracted = documentTextExtractor.extract(bytes);

        assertNotEquals(0, extracted.length());
    }
    
}
