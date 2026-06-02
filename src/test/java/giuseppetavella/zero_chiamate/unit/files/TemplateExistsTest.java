package giuseppetavella.zero_chiamate.unit.files;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemplateExistsTest {

    @Test
    public void templateExistsWhenAskingIfExists() {
        // when
        var exists = FileHelper.templateExists("emails/verify_email");
        // then
        assertTrue(exists);
    }
    
    @Test
    public void templateNotExistsWhenAskingIfexists() {
        // when
        var exists = FileHelper.templateExists("emailsss/doesnt_exist.html");
        // then
        assertFalse(exists);
    }


}
