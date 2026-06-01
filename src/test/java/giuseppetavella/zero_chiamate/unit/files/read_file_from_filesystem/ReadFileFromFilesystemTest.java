package giuseppetavella.zero_chiamate.unit.files.read_file_from_filesystem;

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
    

}
