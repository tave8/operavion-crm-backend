package giuseppetavella.zero_chiamate.unit.infrastructure.pdf;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.pdf.FlyingSaucerAPIPdfService;
import giuseppetavella.zero_chiamate.infrastructure.pdf.exceptions.FlyingSaucerAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlyingSaucerAPIPdfServiceTest {

    @Autowired
    private FlyingSaucerAPIPdfService underTest;
    
    
    @Test
    void validHtmlCanBePdf1() {
        // given
        var html = "<p>hi</p>";
        
        // then
        assertDoesNotThrow(() -> {
            // when
            underTest.htmlToPdf(html);
        });
    }


    @Test
    void validHtmlCanBePdf2() {
        // given
        var html = "<div><p>hi</p><span>hi</span></div>";

        // then
        assertDoesNotThrow(() -> {
            // when
            underTest.htmlToPdf(html);
        });
    }


    @Test
    void validHtmlCanBePdf3() {
        // given
        var html = "<div><p>hi</p><span style=\"color: red;\">hi</span></div>";

        // then
        assertDoesNotThrow(() -> {
            // when
            underTest.htmlToPdf(html);
        });
    }


    @Test
    void validHtmlCanBePdf4() {
        // given
        var html = """
                
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8" />
                <title>Report</title>
            </head>
            <body>
            
                <p>Hello</p>
            
                <p>This will be a pdf.</p>
            
            </body>
            </html>
                
        """;

        // then
        assertDoesNotThrow(() -> {
            // when
            underTest.htmlToPdf(html);
        });
    }



    @Test
    void validHtmlCanBePdf5() {
        // given
        var html = """
                
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8" />
                <title>Report</title>
            </head>
            <body>
            
                <p style="background-color: blue;">Hello</p>
            
                <p>This will be a pdf.</p>
            
            </body>
            </html>
                
        """;

        // then
        assertDoesNotThrow(() -> {
            // when
            underTest.htmlToPdf(html);
        });
    }


    @Test
    void invalidHtmlCannotBePdfBecauseMetaTagNotClosed() {
        // given
        var html = """
                
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8" >
                <title>Report</title>
            </head>
            <body>
            
                <p>Hello</p>
            
                <p>This will be a pdf.</p>
            
            </body>
            </html>
                
        """;

        // then
        assertThrows(FlyingSaucerAPIException.class, () -> {
            // when 
            underTest.htmlToPdf(html);
        });
    }


    @Test
    void invalidHtmlCannotBePdfBecauseParagraphTagNotClosed() {
        // given
        var html = """
                
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8" />
                <title>Report</title>
            </head>
            <body>
            
                <p>Hello</p>
            
                <p>This will be a pdf.<p>
            
            </body>
            </html>
                
        """;

        // then
        assertThrows(FlyingSaucerAPIException.class, () -> {
            // when 
            underTest.htmlToPdf(html);
        });
    }

    @Test
    void invalidHtmlCannotBePdfBecauseQuoteMismatch() {
        // given
        var html = """
                
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8' />
                <title>Report</title>
            </head>
            <body>
            
                <p>Hello</p>
            
                <p>This will be a pdf.</p>
            
            </body>
            </html>
                
        """;

        // then
        assertThrows(FlyingSaucerAPIException.class, () -> {
            // when 
            underTest.htmlToPdf(html);
        });
    }


    @Test
    void invalidHtmlCannotBePdfBecauseTagIsNotClosed() {
        // given
        var html = "<p>hi<p>";

        // then
        assertThrows(FlyingSaucerAPIException.class, () -> {
            // when 
            underTest.htmlToPdf(html);
        });
    }
    

    @Test
    void invalidHtmlCannotBePdfBecauseAnchorTagIsNotClosed() {
        // given
        var html = "<p>hi<a>hey<a></p>";

        // then
        assertThrows(FlyingSaucerAPIException.class, () -> {
            // when 
            underTest.htmlToPdf(html);
        });
    }


    @Test
    void invalidHtmlCannotBePdfBecauseNoRootTagDespiteValidTags() {
        // given
        var html = "<p>hi</p><a>hey</a>";

        // then
        assertThrows(FlyingSaucerAPIException.class, () -> {
            // when 
            underTest.htmlToPdf(html);
        });
    }


    @Test
    void validHtmlIsPdfFileTypeAfterParsing() {
        // given
        var html = "<p>hi</p>";

        // when
        byte[] bytes = underTest.htmlToPdf(html);

        var fileType = FileHelper.getFileType(bytes);

        // then
        assertEquals("pdf", fileType);
    }


}