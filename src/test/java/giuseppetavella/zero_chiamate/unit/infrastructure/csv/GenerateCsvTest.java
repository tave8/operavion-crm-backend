package giuseppetavella.zero_chiamate.unit.infrastructure.csv;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.csv.Csv;
import giuseppetavella.zero_chiamate.infrastructure.csv.exceptions.CsvGenerationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenerateCsvTest {
    
    @Test
    void csvIsGeneratedAndIsTxtFileTypeWithoutFilenameHint() {
        // given
        var csv = new Csv(List.of("name"));
        
        List<String> items = List.of("Giuseppe", "Mary");
        
        // when
        for(var item : items) {
            csv.addRow(
                    item
            );
        }
        
         var fileType = FileHelper.getFileType(csv.toBytes());
        
        // then
        assertEquals("txt", fileType);
    }


    @Test
    void csvIsGeneratedAndIsCsvFileTypeWithFilenameHint() {
        // given
        var csv = new Csv(List.of("name"));

        List<String> items = List.of("Giuseppe", "Mary");

        // when
        for(var item : items) {
            csv.addRow(
                    item
            );
        }

        var fileType = FileHelper.getFileType(csv.toBytes(), "names.csv");

        // then
        assertEquals("csv", fileType);
    }


    @Test
    void cannotHaveMoreRowCellsThanHeaderFields() {
        // given
        var csv = new Csv(List.of("name"));

        List<String> items = List.of("Giuseppe", "Mary");

        assertThrows(CsvGenerationException.class, () -> {
            // when
            for(var item : items) {
                csv.addRow(
                        item,
                        "extra row"
                );
            }
        });
        

    }

}
