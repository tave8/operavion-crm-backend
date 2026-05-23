package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.infrastructure.CvDataModel;
import giuseppetavella.zero_chiamate.helpers.PayloadValidationHelper;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.domain.business.cv_extraction.CvExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/ai")
public class AIController {
    
    @Autowired
    private CvExtractionService cvExtractionService;
    
    @Autowired
    private EmailService emailService;
    




    @PostMapping("/extract/cv")
    public CvDataModel extractCv(
            @RequestParam("file") MultipartFile file) 
    {

        PayloadValidationHelper.requiredPdf(file);

        // this.emailService.sendEmail(
        //         "giuseppetavella8@gmail.com",
        //         "Your file",
        //         "<b>hi</b>",
        //         new EmailAttachment(file, "uploaded_file.pdf")
        // );
        
        return cvExtractionService.extractCv(file);
        
    }
    
    
    
    
}
