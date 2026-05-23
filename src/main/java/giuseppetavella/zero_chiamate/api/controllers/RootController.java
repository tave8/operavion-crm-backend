package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.utils.DocumentTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
