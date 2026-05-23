package giuseppetavella.demo_login_system.api.controllers;

import giuseppetavella.demo_login_system.infrastructure.CvDataModel;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.infrastructure.email.BaseEmailService;
import giuseppetavella.demo_login_system.domain.business.cv_extraction.CvExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/ai")
public class AIController {
    
    @Autowired
    private CvExtractionService cvExtractionService;
    
    @Autowired
    private BaseEmailService baseEmailService;
    




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
