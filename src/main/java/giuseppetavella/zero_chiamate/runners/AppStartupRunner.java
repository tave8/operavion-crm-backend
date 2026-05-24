package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.config.AppEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {

    @Autowired
    private AppEnvironment appEnvironment;
    
    @Override
    public void run(String... args) throws Exception {

        // System.out.println(appEnvironment.isLocal());
        // System.out.println(appEnvironment.isPreview());
        // System.out.println(appEnvironment.isProduction());

        // System.out.println(appEnvironment.getFrontendUrl());
        // System.out.println(appEnvironment.getServerUrl());
        
    }
}
