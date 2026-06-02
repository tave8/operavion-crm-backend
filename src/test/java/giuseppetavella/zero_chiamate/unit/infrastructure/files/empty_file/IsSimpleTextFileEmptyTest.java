package giuseppetavella.zero_chiamate.unit.infrastructure.files.empty_file;

import giuseppetavella.zero_chiamate.exceptions.EmptyFileException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the emptiness of file.
 * Test:
 * - with number of bytes
 * - when get file type
 */
public class IsSimpleTextFileEmptyTest {

    @Test
    public void txtIsEmptyWithBytes() {
        byte[] bytes = FileHelper.readFile("extra/empty.txt");

        assertEquals(0, bytes.length);
    }

    @Test
    public void txtIsNotEmptyWithBytes() {
        byte[] bytes = FileHelper.readFile("extra/jwt.txt");

        assertNotEquals(0, bytes.length);
    }
    
    
    @Test
    public void txtFileIsEmptyWhenGetFileType() {
        var bytes = FileHelper.readFile("extra/empty.txt");
        
        assertThrows(EmptyFileException.class, () -> {
            FileHelper.getFileType(bytes);
        });
    }
    

    @Test
    public void txtFileIsNotEmptyWhenGetFileType() {
        var bytes = FileHelper.readFile("extra/jwt.txt");

        assertDoesNotThrow(() -> {
            FileHelper.getFileType(bytes);
        });
    }
    
    
    
}
