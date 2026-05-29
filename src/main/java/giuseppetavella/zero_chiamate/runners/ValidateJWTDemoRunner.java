package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.security.TokenTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ValidateJWTDemoRunner implements CommandLineRunner {

    @Autowired
    private TokenTools tokenTools;

    @Override
    public void run(String... args) throws Exception {
        
        // var subject = "hi";
        // var token = tokenTools.generateToken(subject, Duration.ofMinutes(1));
        //
        // tokenTools.verifyToken(token);
        //
        // // System.out.println("token is valid");
        //
        // System.out.println(token);
        
    }
    
}
