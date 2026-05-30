package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.security.TokenTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qrcode")
public class QrCodeValidateController {
    
    public record Payload(String message) {}
    
    @Autowired
    private TokenTools tokenTools;

    @PostMapping("/{token}/verify")
    public Payload verify(@PathVariable String token) {
        
        tokenTools.verifyToken(token);
        
        return new Payload("token is valid");
    }

}
