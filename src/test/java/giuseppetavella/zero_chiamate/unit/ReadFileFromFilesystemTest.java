package giuseppetavella.zero_chiamate.unit;

import giuseppetavella.zero_chiamate.exceptions.FileException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Read file from filesystem.
 * Test:
 * - file exists
 * - file not exists
 */
public class ReadFileFromFilesystemTest {

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
    public void fileIsEmpty() {
      byte[] bytes = FileHelper.readFile("extra/empty.txt");
      
      assertEquals(0, bytes.length);
    }

    @Test
    public void fileIsNotEmpty() {
        byte[] bytes = FileHelper.readFile("extra/cat_image_as_pdf.pdf");

        assertNotEquals(0, bytes.length);
    }

}
