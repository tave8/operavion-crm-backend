package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FilesystemDemoRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
    
        var bytes = FileHelper.readFile("extra/invoice.pdf");


        System.out.println(bytes);
        
    }
    
}
