package giuseppetavella.zero_chiamate.unit.pdf;

import giuseppetavella.zero_chiamate.infrastructure.pdf.FlyingSaucerAPIPdfService;
import giuseppetavella.zero_chiamate.infrastructure.pdf.exceptions.FlyingSaucerAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FlyingSaucerAPIPdfServiceTest {

    @Autowired
    private FlyingSaucerAPIPdfService underTest;
    
    
    @Test
    void validHtmlCanBePDF() {
        // given
        var html = "<p>hi</p>";
        
        // then
        assertDoesNotThrow(() -> {
            // when
            underTest.htmlToPdf(html);
        });
    }
    

    @Test
    void invalidHtmlCannotBePDF() {
        // given
        var html = "<p>hi<p>";

        // then
        assertThrows(FlyingSaucerAPIException.class, () -> {
            // when 
            underTest.htmlToPdf(html);
        });
    }
    

}