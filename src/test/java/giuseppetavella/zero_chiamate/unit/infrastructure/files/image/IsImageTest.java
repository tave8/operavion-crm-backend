package giuseppetavella.zero_chiamate.unit.infrastructure.files.image;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsImageTest {


    @Test
    void jpgIsImage() {
        // given
        byte[] bytes = FileHelper.readFile("extra/duck.jpg");
        
        // when
        var isImage = FileHelper.isImage(bytes);
        
        // then
        assertTrue(isImage);
    }

    @Test
    void jpegIsImage() {
        // given
        byte[] bytes = FileHelper.readFile("extra/cyclist.jpeg");

        // when
        var isImage = FileHelper.isImage(bytes);

        // then
        assertTrue(isImage);
    }

    @Test
    void pngIsImage() {
        // given
        byte[] bytes = FileHelper.readFile("extra/linkedin.png");

        // when
        var isImage = FileHelper.isImage(bytes);

        // then
        assertTrue(isImage);
    }

    
    @Test
    void txtIsNotImage() {
        // given
        byte[] bytes = FileHelper.readFile("extra/one_space.txt");

        // when
        var isImage = FileHelper.isImage(bytes);

        // then
        assertFalse(isImage);
    }
    

    @Test
    void pdfIsNotImageEvenIfPdfWasImage() {
        // given
        byte[] bytes = FileHelper.readFile("extra/cat_image_as_pdf.pdf");

        // when
        var isImage = FileHelper.isImage(bytes);

        // then
        assertFalse(isImage);
    }

}
