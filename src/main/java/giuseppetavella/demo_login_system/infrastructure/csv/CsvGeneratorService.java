package giuseppetavella.demo_login_system.infrastructure.csv;

import giuseppetavella.demo_login_system.infrastructure.CsvSeparator;
import giuseppetavella.demo_login_system.exceptions.CsvGenerationException;
import giuseppetavella.demo_login_system.helpers.FileHelper;

public class CsvGeneratorService {
    
    private final StringBuilder csv;
    private final String[] fields;
    private final CsvSeparator separator;
    private final boolean addSeparatorHint;

    /**
     * Initialize a CSV with a custom separator.
     * 
     * @param fields array of header fields
     * @param separator the value separator, for example comma
     * @param addSeparatorHint whether to add a marker at the start of the csv (excel-specific),
     *                         indicating which separator the csv is using
     */
    public CsvGeneratorService(String[] fields,
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
        this.generateHeader();
    }


    public CsvGeneratorService(String[] fields, CsvSeparator separator) throws CsvGenerationException
    {
        this(fields, separator, false);
    }
    
    /**
     * Initialize a CSV with a comma as default separator
     */
    public CsvGeneratorService(String[] fields) throws CsvGenerationException
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
        // check if the number of values is different 
        // from the number of fields
        if(values.length != getFields().length) {
            throw new CsvGenerationException("While adding a row to a csv, the number "
                                            +"of values in this row was different than "
                                            +"the number of header fields. "
                                            +"Number of values in this row was '" + values.length + "'. " 
                                            +"Number of header fields was '" + getFields().length + "'. "
                                            +"The source of truth is the number of header fields or row values?");
        }
        
        StringBuilder csv = this.getCsv();
        String separator = this.getSeparator().getValue();

        // separate each value but the last one
        csv.append(String.join(separator, values)).append("\n");
        
    }

    /**
     * Initialize the CSV with a header row.
     * Must only be generated once at initialization.
     */
    private void generateHeader() {

        StringBuilder csv = this.getCsv();
        String[] fields = this.getFields();
        String separator = this.getSeparator().getValue();
        
        // before generating the header fields,
        // let's check whether we need to add 
        // an excel-specific separator hint
        if(this.isAddSeparatorHint()) {
            csv.append("sep=").append(separator).append("\n");
        }

        // separate each value but the last one
        csv.append(String.join(separator, fields)).append("\n");

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
    public String toAttachment() {
        return this.toBase64();
    }

    /**
     * This csv -> base64 
     */
    public String toBase64() {
        return FileHelper.toBase64(this.toBytes());
    }

    public StringBuilder getCsv() {
        return csv;
    }

    public String[] getFields() {
        return fields;
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
