package giuseppetavella.zero_chiamate.unit.infrastructure.files;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the file type/extension, such as pdf, img, csv etc.
 * Required unit tests:
 * - read file from filesystem
 * 
 * Test:
 * - pdf
 * - csv
 * - txt
 * - png
 * - jpg
 * - xlsx
 * - docx
 */
public class IsFileTypeMatchTest {

    
    @Test
    public void pdfIsPdf() {
        var bytes = FileHelper.readFile("extra/cat_image_as_pdf.pdf");
        
        var fileType = FileHelper.getFileType(bytes);
        
        assertEquals("pdf", fileType);
    }


    @Test
    public void csvIsCsvWithFilenameHint() {
        var bytes = FileHelper.readFile("extra/report_turni.csv");

        var fileType = FileHelper.getFileType(bytes, "report_turni.csv");

        assertEquals("csv", fileType);
    }

    @Test
    public void csvIsTxtWithoutFilenameHint() {
        var bytes = FileHelper.readFile("extra/report_turni.csv");

        var fileType = FileHelper.getFileType(bytes);

        assertEquals("txt", fileType);
    }

    @Test
    public void docxIsDocx() {
        var bytes = FileHelper.readFile("extra/presentazione_progetto.docx");

        var fileType = FileHelper.getFileType(bytes);

        assertEquals("docx", fileType);
    }

    @Test
    public void pngIsPng() {
        var bytes = FileHelper.readFile("extra/linkedin.png");

        var fileType = FileHelper.getFileType(bytes);

        assertEquals("png", fileType);
    }

    @Test
    public void jpgIsJpg() {
        var bytes = FileHelper.readFile("extra/duck.jpg");

        var fileType = FileHelper.getFileType(bytes);

        assertEquals("jpg", fileType);
    }

    @Test
    public void jpegIsJpg() {
        var bytes = FileHelper.readFile("extra/cyclist.jpeg");

        var fileType = FileHelper.getFileType(bytes);

        assertEquals("jpg", fileType);
    }



    @Test
    public void txtIsTxt() {
        var bytes = FileHelper.readFile("extra/jwt.txt");

        var fileType = FileHelper.getFileType(bytes);

        assertEquals("txt", fileType);
    }

    @Test
    public void xlsxIsXlsx() {
        var bytes = FileHelper.readFile("extra/report.xlsx");

        var fileType = FileHelper.getFileType(bytes);

        assertEquals("xlsx", fileType);
    }



}
