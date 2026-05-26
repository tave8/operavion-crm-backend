package giuseppetavella.zero_chiamate.csv_generation;

import giuseppetavella.zero_chiamate.exceptions.CsvGenerationException;
import giuseppetavella.zero_chiamate.infrastructure.csv.Csv;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvGenerationTest {
    
    
    @Test
    void addHeader_isValid() {
        
        List<String> names = List.of("Giuseppe", "Maria");
        
        String[] fields = {"Fullname", "Age,", "People\" count"};
        
        var csv = new Csv(fields);
        
        // assertThrows(CsvGenerationException.class, () -> {
        for (var name : names) {
            csv.addRow(
                    name,
                    123+"",
                    223+""
            );
        }
        // });
        
        // csv.

        
        System.out.println(csv);
        
        // assertTrue();
        
    }
    
}
