package giuseppetavella.demo_login_system.api.controllers;

import giuseppetavella.demo_login_system.helpers.FileHelper;
import giuseppetavella.demo_login_system.utils.DocumentTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class RootController {
    
    @Autowired
    private DocumentTextExtractor documentTextExtractor;
    
    @GetMapping
    public String root() {
        return "the server works";
    }
    
    // @PostMapping("/extract-file")
    // public String extractFile(@RequestParam("file") MultipartFile file)
    // {
    //    
    //     byte[] bytes = FileHelper.getBytes(file);
    //    
    //     return documentTextExtractor.bytesToText(bytes);
    //    
    // }
    
}
