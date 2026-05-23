package giuseppetavella.demo_login_system.infrastructure.csv;

import giuseppetavella.demo_login_system.infrastructure.CsvSeparator;
import giuseppetavella.demo_login_system.exceptions.CsvGenerationException;

/**
 * In a ExcelCsv, we always add the separator hint,
 * for simplicity.
 */
public class ExcelCsvGeneratorService extends CsvGeneratorService {
    
    public ExcelCsvGeneratorService(String[] fields, CsvSeparator separator) throws CsvGenerationException
    {
        super(fields, separator, true);
    }
    
    public ExcelCsvGeneratorService(String[] fields) throws CsvGenerationException
    {
        this(fields, CsvSeparator.COMMA);
    }

}
