package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.models.CvData;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.services.AppAIService;
import giuseppetavella.demo_login_system.services.base.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/ai")
public class AIController {
    
    @Autowired
    private AppAIService appAIService;
    
    @Autowired
    private EmailService emailService;
    
    


    /**
     * Extract contract expectations from a legal contract.
     * 
     * @param file
     * @return
     */
    @PostMapping("/extract/contract-expectations")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String extractContractExpectations(@AuthenticationPrincipal User currentUser,
                                              @RequestParam("file") MultipartFile file)
    {

        PayloadValidationHelper.requiredPdf(file);
        
        return this.appAIService.extractContractExpectations(file);
        
        // return "endpoint works";

        // // this.emailService.sendEmail(
        // //         "giuseppetavella8@gmail.com",
        // //         "Your file",
        // //         "<b>hi</b>",
        // //         new EmailAttachment(file, "uploaded_file.pdf")
        // // );
        //
        // return this.appAIService.extractCv(file);

    }



    @PostMapping("/extract/cv")
    public CvData extractCv(
            @RequestParam("file") MultipartFile file) 
    {

        PayloadValidationHelper.requiredPdf(file);

        // this.emailService.sendEmail(
        //         "giuseppetavella8@gmail.com",
        //         "Your file",
        //         "<b>hi</b>",
        //         new EmailAttachment(file, "uploaded_file.pdf")
        // );
        
        return this.appAIService.extractCv(file);
        
    }
    
    
    
    
}
