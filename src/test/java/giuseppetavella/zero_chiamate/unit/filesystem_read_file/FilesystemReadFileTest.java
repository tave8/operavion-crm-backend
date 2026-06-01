package giuseppetavella.zero_chiamate.unit.filesystem_read_file;

import giuseppetavella.zero_chiamate.exceptions.FileException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilesystemReadFileTest {
    
    @Test
    public void fileNotExists() {
        assertThrows(FileException.class, () -> {
            FileHelper.readFile("cat_image_as.sds.pdf");
        });
    }

    @Test
    public void fileExists() {
        assertThrows(FileException.class, () -> {
            FileHelper.readFile("cat_image_as_pdf.pdf");
        });
    }

    // @Test
    // public void fileNotExists() {
    //     assertThrows(FileException.class, () -> {
    //         FileHelper.readFile("cat_image_as.sds.pdf");
    //     });
    // }

}
