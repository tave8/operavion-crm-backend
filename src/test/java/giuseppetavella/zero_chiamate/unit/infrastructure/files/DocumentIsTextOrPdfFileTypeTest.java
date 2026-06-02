package giuseppetavella.zero_chiamate.unit.infrastructure.files;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Is document a text file?
 * Prerequisites:
 * - read file from file system
 * - file type match
 * - is file a text file
 */
public class DocumentIsTextOrPdfFileTypeTest {


    @Test
    public void excelIsTextFileType() {
        byte[] bytes = FileHelper.readFile("extra/report.xlsx");

        var answer =  FileHelper.isTextOrPdfFile(bytes);

        assertTrue(answer);
    }

    @Test
    public void docxIsTextFileType() {
        byte[] bytes = FileHelper.readFile("extra/presentazione_progetto.docx");

        var answer =  FileHelper.isTextOrPdfFile(bytes);

        assertTrue(answer);
    }


    @Test
    public void csvIsTextFileType() {
        byte[] bytes = FileHelper.readFile("extra/report_turni.csv");

        var answer =  FileHelper.isTextOrPdfFile(bytes);

        assertTrue(answer);
    }


    @Test
    public void pdfIsPdfFileType() {
        byte[] bytes = FileHelper.readFile("extra/invoice.pdf");

        var answer =  FileHelper.isTextOrPdfFile(bytes);

        assertTrue(answer);
    }

    @Test
    public void xlsxIsTextFileType() {
        byte[] bytes = FileHelper.readFile("extra/invoice.pdf");

        var answer =  FileHelper.isTextOrPdfFile(bytes);

        assertTrue(answer);
    }

    @Test
    public void txtIsTextFileType() {
        byte[] bytes = FileHelper.readFile("extra/jwt.txt");

        var answer =  FileHelper.isTextOrPdfFile(bytes);

        assertTrue(answer);
    }



}
