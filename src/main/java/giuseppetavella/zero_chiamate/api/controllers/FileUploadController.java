package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.api.AttachmentResponseBuilder;
import giuseppetavella.zero_chiamate.helpers.PayloadValidationHelper;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileStorageService;
import giuseppetavella.zero_chiamate.integrations.cloudflare_r2.CloudflareR2APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file-upload")
public class FileUploadController {
    
    @Autowired
    private FileStorageService fileStorageService;
    
    
    @PostMapping("/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadPdf(@RequestParam("file") MultipartFile file) {

        PayloadValidationHelper.requiredPdf(file);
        
        return fileStorageService.upload(file).url();
        
    }

    @PostMapping("/upload-and-download")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<byte[]> uploadAndDownload(@RequestParam("file") MultipartFile file) {
        
        var filename = fileStorageService.upload(file).filename();
        
        return AttachmentResponseBuilder.anyFile(
                fileStorageService.download(filename),
                filename
        );
        
    }
    
}
