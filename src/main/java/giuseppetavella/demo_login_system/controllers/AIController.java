package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.FileHelper;
import giuseppetavella.demo_login_system.models.CvData;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.payloads.in_response.ContractExpectationToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ExtractedContractExpectationsToSendDTO;
import giuseppetavella.demo_login_system.services.AppAIService;
import giuseppetavella.demo_login_system.services.base.EmailService;
import giuseppetavella.demo_login_system.workers.ContractAnalysisWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/ai")
public class AIController {
    
    @Autowired
    private AppAIService appAIService;
    
    @Autowired
    private EmailService emailService;
    




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
