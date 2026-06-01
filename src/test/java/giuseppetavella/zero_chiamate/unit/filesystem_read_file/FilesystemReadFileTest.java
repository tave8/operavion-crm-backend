package giuseppetavella.zero_chiamate.unit.filesystem_read_file;

import giuseppetavella.zero_chiamate.exceptions.FileException;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataFormatException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.DocumentTextExtractor;
import giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions.DocumentEmptyTextExtractionException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilesystemReadFileTest {
    
    @Autowired
    private DocumentTextExtractor documentTextExtractor;
    
    @Test
    public void fileExists() {
        assertDoesNotThrow(() -> {
            FileHelper.readFile("extra/cat_image_as_pdf.pdf");
        });
    }
    
    @Test
    public void fileNotExists() {
        assertThrows(FileException.class, () -> {
            FileHelper.readFile("extra/cat_image_as_pdf1.pdf");
        });
    }


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
