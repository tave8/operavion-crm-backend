package giuseppetavella.zero_chiamate.infrastructure.csv;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachable;

public class Csv implements EmailAttachable {
    
    private final byte[] csv;
    
    public Csv(byte[] csv) {
        this.csv = csv;
    }
    
    public Csv(String csv) {
        this(csv.getBytes());
    }
    
    public Csv(CsvGeneratorService csvGen) {
        this(csvGen.getCsv().toString());
    }
    
    @Override
    public String toAttachment() {
        return FileHelper.toBase64(this.getCsv());
    }

    public byte[] getCsv() {
        return csv;
    }
}
