package giuseppetavella.zero_chiamate.unit;

import giuseppetavella.zero_chiamate.config.ReportTemplate;
import giuseppetavella.zero_chiamate.config.Template;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.pdf.FlyingSaucerAPIPdfService;
import giuseppetavella.zero_chiamate.infrastructure.template.ThymeleafAPIService;
import giuseppetavella.zero_chiamate.infrastructure.template.exceptions.ThymeleafAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ThymeleafAPIServiceTest {

    @Autowired
    private ThymeleafAPIService underTest;
    
    @Autowired
    private FlyingSaucerAPIPdfService flyingSaucerAPIPdfService;

    
    @Test
    void templateExistsEvenIfTemplateVarsNotFilled() {
        // given
        String templatePath = "emails/verify_email.html";
        Map<String, Object> templateVars = Map.of();

        // then
        assertDoesNotThrow(() -> {
            // when
            underTest.fillTemplate(templatePath, templateVars);
        });
    }
    
    
    @Test
    void templateNotExists() {
        // given
        String templatePath = "/doesnt-exist";
        Map<String, Object> templateVars = Map.of();
        
        // then
        assertThrows(ThymeleafAPIException.class, () -> {
            // when
            underTest.fillTemplate(templatePath, templateVars);
        });
    }



    @Test
    void templateIsFilledThenBecomesPdf() {
        // given
        String templatePath = "emails/verify_email.html";
        Map<String, Object> templateVars = Map.of(
                "firstname", "Giuseppe",
                "verificationUrl", "https://zerochiamate.com"
        );

        // when
        var html = underTest.fillTemplate(templatePath, templateVars);
        
        var pdfBytes = flyingSaucerAPIPdfService.htmlToPdf(html);
        
        // then
        assertEquals("pdf", FileHelper.getFileType(pdfBytes));
        
    }
    
    
}