package giuseppetavella.zero_chiamate.infrastructure.csv;

import giuseppetavella.zero_chiamate.exceptions.CsvException;
import giuseppetavella.zero_chiamate.infrastructure.CsvSeparator;
import giuseppetavella.zero_chiamate.exceptions.CsvGenerationException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachable;
import org.jspecify.annotations.NonNull;

/**
 * This is a business-independent entity + behavior.
 */
public class Csv implements EmailAttachable {
    
    // the csv is simply a string builder instance
    private final StringBuilder csv;
    // the columns, also known as fields
    private final String[] fields;
    // what separator are we using, for example comma or semicolon
    private final CsvSeparator separator;
    // add a hint that indicates which csv separator are we using?
    // only excel suports this, so should  be used with care
    private final boolean addSeparatorHint;
    // was the header added? we can only add it once
    private boolean headerAdded = false;

    /**
     * Initialize a CSV with a custom separator.
     * 
     * @param fields array of header fields
     * @param separator the value separator, for example comma
     * @param addSeparatorHint whether to add a marker at the start of the csv (excel-specific),
     *                         indicating which separator the csv is using
     */
    public Csv(String[] fields,
               CsvSeparator separator,
               boolean addSeparatorHint) throws CsvGenerationException
    {
        if(fields.length == 0) {
            throw new CsvGenerationException("Csv must have at least 1 header field.");
        }
        
        this.csv = new StringBuilder();
        this.fields = fields;
        this.separator = separator;
        this.addSeparatorHint = addSeparatorHint;
        // generate header. this must come AFTER other fields 
        // have been initialized, otherwise you get initialization error
        this.addHeader();
    }


    public Csv(String[] fields, CsvSeparator separator) throws CsvGenerationException
    {
        this(fields, separator, false);
    }
    
    /**
     * Initialize a CSV with a comma as default separator
     */
    public Csv(String[] fields) throws CsvGenerationException
    {
        this(fields, CsvSeparator.COMMA, false);
    }
    
    
    /**
     * Add a row to the CSV.
     * The number of values in each row 
     * must match the number of header fields.
     */
    public void addRow(String... values) throws CsvGenerationException
    {
        // the number of cell values passed
        // matches the number of csv columns?
        var fieldNumberMatch = values.length == getFields().length;
        
        // check if the number of values is different 
        // from the number of fields
        if(!fieldNumberMatch) {
            throw new CsvGenerationException("While adding a row to a csv, the number "
                                            +"of values in this row was different than "
                                            +"the number of header fields. "
                                            +"Number of values in this row was '" + values.length + "'. " 
                                            +"Number of header fields was '" + getFields().length + "'. "
                                            +"The source of truth is the number of header fields or row values?");
        }
        
        var csv = this.getCsv();
        var separator = this.getSeparator().getValue();

        // loop through the values to be added as a csv row
        for (int i = 0; i < values.length; i++) 
        {
            var value = getSafeStringFromValues(values, i, separator);

            csv.append(value);

            // append separator after every value except the last
            var isLast = i == values.length - 1;
            
            if (!isLast) {
                csv.append(separator);
            }
        }

        // end of row
        csv.append("\n");
        
    }

    /**
     * Return a safe string. The values string are used 
     * for providing context about the potential error, 
     * and not for necessity.
     * 
     * @param values
     * @param currIdx
     * @param separator
     * @return
     */
    private @NonNull String getSafeStringFromValues(String[] values, 
                                                   int currIdx, 
                                                   String separator) 
    {
        // the current value
        var value = values[currIdx];

        // cell value cannot be null
        if (value == null) {
            throw new CsvGenerationException(
                    "While adding a row to a csv, cell value cannot be null. " +
                            "Cell index: " + currIdx + ". " +
                            "Corresponding field: '" + getFieldAt(currIdx) + "'. " + 
                            "Previous value: '" + getPreviousValueIfExists(values, currIdx) + "'. " +
                            "Next value: '" + getNextValueIfExists(values, currIdx) + "'."
            );
        }
        
        return escapeIfNecessary(value, separator);
    }

    
    /**
     * Get the field at the given index.
     */
    public String getFieldAt(int i) throws CsvException {
        if (i < 0 || i >= fields.length) {
            throw new CsvException(
                    "Field index out of bounds. " +
                            "Index: " + i + ". " +
                            "Number of fields: " + fields.length + "."
            );
        }
        return fields[i];
    }
    

    /**
     * Initialize the CSV with a header row.
     * 
     * Must only be generated AFTER setting 
     * all other attributes, otherwise 
     * you'll get an initialization error.
     */
    private void addHeader() {
        
        if(headerAdded) {
            throw new CsvGenerationException(
                    "Internal error while generating a csv. "
                    +"While adding the header, the header was already added."
            );
        }

        var isNotProperlyInitialized = this.csv == null || this.fields == null || this.separator == null;
        
        // .addHeader() must be called only after these 
        // attributes are set
        if (isNotProperlyInitialized) {
            throw new CsvGenerationException(
                    "Internal error while generating a csv. " +
                            "Initialization error: Some required attribute was null. " +
                            "You must call .addHeader() AFTER " +
                            "all attributes have been properly initialized."
            );
        }

        var csv = this.csv;
        var fields = this.fields;
        var separator = this.separator.getValue();
        
        // before generating the header fields,
        // let's check whether we need to add 
        // an excel-specific separator hint
        if(this.isAddSeparatorHint()) {
            csv.append("sep=").append(separator).append("\n");
        }
        
        // adding the header is a sub-case of adding a row
        // keep in mind, 
        this.addRow(fields);
        // we add the header only once
        this.headerAdded = true;

    }


    
    /**
     * Escape a CSV cell value if it contains characters that would break the CSV structure.
     *
     * A value must be escaped (wrapped in double quotes) if it contains:
     * - the separator (e.g. ',' or ';') — would be misread as a column delimiter
     * - a double quote — would break the quoting mechanism
     * - a newline — would be misread as a new row
     *
     * Internal double quotes are escaped by doubling them (""),
     * as per RFC 4180.
     *
     * Examples:
     * "hello,world"  with separator ','  ->  "\"hello,world\""
     * "say \"hi\""                       ->  "\"say \"\"hi\"\"\""
     * "line1\nline2"                     ->  "\"line1\nline2\""
     *
     * @param value     the cell value to escape
     * @param separator the CSV separator in use
     * @return the escaped value, or the original value if no escaping was needed
     */
    public static @NonNull String escapeIfNecessary(@NonNull String value, String separator) {

        var containsSeparator = value.contains(separator);
        var containsQuote = value.contains("\"");
        var containsNewline = value.contains("\n");
        var containsCarriageReturn = value.contains("\r");

        var needsEscaping = containsSeparator || containsQuote || containsNewline || containsCarriageReturn;

        if (needsEscaping) {
            // escape internal double quotes by doubling them, then wrap in double quotes
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    /**
     * Return the previous value, if it exists.
     * 
     * @param values
     * @param currentIdx
     * @return
     */
    private static String getPreviousValueIfExists(String[] values, int currentIdx) {
        boolean hasPrevious = currentIdx > 0;
        if (hasPrevious) {
            return values[currentIdx - 1];
        }
        return "<no prev>";
    }

    /**
     * Return the next value, if it exists.
     * 
     * @param values
     * @param currentIdx
     * @return
     */
    private static String getNextValueIfExists(String[] values, int currentIdx) {
        boolean hasNext = currentIdx < values.length - 1;
        if (hasNext) {
            return values[currentIdx + 1];
        }
        return "<no next>";
    }
    
    
    /**
     * This csv -> bytes 
     */
    public byte[] toBytes() {
        return this.getCsv().toString().getBytes();
    }

    /**
     * This csv -> to base64
     */
    @Override
    public String toAttachment() {
        return this.toBase64();
    }

    /**
     * This csv -> base64 
     */
    public String toBase64() {
        return FileHelper.toBase64(this.toBytes());
    }

    private StringBuilder getCsv() {
        return csv;
    }

    /**
     * The header fields, i.e. the columns.
     * 
     * @return
     */
    public String[] getFields() {
        return fields.clone();
    }

    public CsvSeparator getSeparator() {
        return separator;
    }

    public boolean isAddSeparatorHint() {
        return addSeparatorHint;
    }

    /**
     * When a {@code .toString()} is called on a Csv object,
     * the csv that was built so far is returned.
     */
    @Override
    public String toString() {
        return this.getCsv().toString();
    }
    
}
