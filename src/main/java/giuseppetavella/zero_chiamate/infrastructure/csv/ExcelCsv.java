package giuseppetavella.zero_chiamate.infrastructure.csv;

import giuseppetavella.zero_chiamate.infrastructure.CsvSeparator;
import giuseppetavella.zero_chiamate.exceptions.CsvGenerationException;

import java.util.List;

/**
 * In a ExcelCsv, we always add the separator hint,
 * for simplicity - it will display it correctly,
 * regardless of what separator we use it.
 */
public class ExcelCsv extends Csv {

    public ExcelCsv(List<String> fields,
                    String nullReplacement,
                    CsvSeparator separator) throws CsvGenerationException
    {
        super(fields, nullReplacement, separator, true);
    }

    public ExcelCsv(List<String> fields,
                    String nullReplacement) throws CsvGenerationException
    {
        this(fields, nullReplacement, CsvSeparator.COMMA);
    }
    

}
