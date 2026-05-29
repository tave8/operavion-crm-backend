package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.security.TokenTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qrcode")
public class QrCodeValidateController {
    
    @Autowired
    private TokenTools tokenTools;

    @GetMapping("/{token}/validate")
    public String validate(@PathVariable String token) {
        
        tokenTools.verifyToken(token);
        
        return "token is valid";
    }

}
