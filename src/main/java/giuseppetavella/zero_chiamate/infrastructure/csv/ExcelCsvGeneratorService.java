package giuseppetavella.zero_chiamate.infrastructure.csv;

import giuseppetavella.zero_chiamate.infrastructure.CsvSeparator;
import giuseppetavella.zero_chiamate.exceptions.CsvGenerationException;

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
