package giuseppetavella.zero_chiamate.unit.files;

import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.DocumentTextExtractor;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions.DocumentEmptyTextExtractionException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test whether the document provided is a text or pdf document.
 * Prerequisites:
 * - read file from filesystem
 * - get file type
 *
 *
 * Test:
 * - is document a pdf file
 * - is document a text file
 * - is document a pdf or text file 
 */
@SpringBootTest
public class FileContainsTextTest {
    
    @Autowired
    private DocumentTextExtractor documentTextExtractor;
    
    @Test
    public void excelIsText() {
        byte[] bytes = FileHelper.readFile("extra/report.xlsx");

        assertDoesNotThrow(() -> {
            documentTextExtractor.extractAndRequireNonEmpty(bytes);
        });

    }




    @Test
    public void csvIsText() {
        byte[] bytes = FileHelper.readFile("extra/report_turni.csv");

        assertDoesNotThrow(() -> {
            documentTextExtractor.extractAndRequireNonEmpty(bytes);
        });

    }



    @Test
    public void docxIsText() {
        byte[] bytes = FileHelper.readFile("extra/presentazione_progetto.docx");

        assertDoesNotThrow(() -> {
            documentTextExtractor.extractAndRequireNonEmpty(bytes);
        });
        

    }

    
    @Test
    public void pdfWasImageSoHasNoText() {
        byte[] bytes = FileHelper.readFile("extra/cat_image_as_pdf.pdf");

        assertThrows(DocumentEmptyTextExtractionException.class, () -> {
            documentTextExtractor.extractAndRequireNonEmpty(bytes);
        });
    }

    @Test
    public void pdfIsTextSoHasText() {
        byte[] bytes = FileHelper.readFile("extra/cleaning_contract.pdf");
        
        var text = documentTextExtractor.extractAndRequireNonEmpty(bytes);
    
        assertNotEquals("", text);
    }

    @Test
    public void textFileHasText() {
        byte[] bytes = FileHelper.readFile("extra/jwt.txt");

        var text = documentTextExtractor.extractAndRequireNonEmpty(bytes);

        assertNotEquals("", text);
    }


    @Test
    public void imageIsNotText() {
        byte[] bytes = FileHelper.readFile("extra/linkedin.png");

        assertThrows(InvalidDataException.class, () -> {
            documentTextExtractor.extractAndRequireNonEmpty(bytes);
        });
        
    }


    @Test
    public void pdfIsTextButAlsoHasImage() {
        byte[] bytes = FileHelper.readFile("extra/valid_cleaning_contract_with_image.pdf");

        var text = documentTextExtractor.extractAndRequireNonEmpty(bytes);

        assertNotEquals("", text);
        
    }



    @Test
    public void pdfWasScannedDocxWithImageSoIsValid() {
        byte[] bytes = FileHelper.readFile("extra/valid_cleaning_contract_with_image_previously_docx.pdf");

        var text = documentTextExtractor.extractAndRequireNonEmpty(bytes);

        assertNotEquals("", text);

    }

    
    

}
