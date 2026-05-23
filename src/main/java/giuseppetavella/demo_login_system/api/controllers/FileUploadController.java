package giuseppetavella.demo_login_system.api.controllers;

import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.infrastructure.storage.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file-upload")
public class FileUploadController {
    
    @Autowired
    private FileUploadService fileUploadService;
    
    @PostMapping("/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadPdf(@RequestParam("file") MultipartFile file) {

        PayloadValidationHelper.requiredPdf(file);
        
        return this.fileUploadService.upload(file, "pdf");
        
    }
    
}
