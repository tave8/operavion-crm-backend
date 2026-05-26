package giuseppetavella.zero_chiamate.infrastructure.csv;

import giuseppetavella.zero_chiamate.infrastructure.CsvSeparator;
import giuseppetavella.zero_chiamate.exceptions.CsvGenerationException;

/**
 * In a ExcelCsv, we always add the separator hint,
 * for simplicity - it will display it correctly,
 * regardless of what separator we use it.
 */
public class ExcelCsv extends Csv {

    public ExcelCsv(String[] fields, CsvSeparator separator) throws CsvGenerationException
    {
        super(fields, separator, true);
    }

    public ExcelCsv(String[] fields) throws CsvGenerationException
    {
        this(fields, CsvSeparator.COMMA);
    }

}
