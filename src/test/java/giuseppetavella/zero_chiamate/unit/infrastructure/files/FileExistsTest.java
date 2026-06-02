package giuseppetavella.zero_chiamate.unit.infrastructure.files;

import giuseppetavella.zero_chiamate.exceptions.FileException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Read file from filesystem.
 * Test:
 * - file exists
 * - file not exists
 */
public class FileExistsTest {

    @Test
    public void fileExistsWhenReading() {
        assertDoesNotThrow(() -> {
            FileHelper.readResource("extra/cat_image_as_pdf.pdf");
        });
    }

    @Test
    public void fileExistsWhenAskingIfExists() {
        // when
        var exists = FileHelper.resourceExists("extra/cat_image_as_pdf.pdf");
        
        // then
        assertTrue(exists);
    }


    @Test
    public void fileNotExistsWhenReading() {
        assertThrows(FileException.class, () -> {
            FileHelper.readResource("extra/cat_image_as_pdf1.pdf");
        });
    }
    

    @Test
    public void fileNotExistsWhenAskingIfExists() {
        // when
        var exists = FileHelper.resourceExists("extra/cat_image_as_pdf1.pdf");
        
        // then
        assertFalse(exists);
    }
    

}
