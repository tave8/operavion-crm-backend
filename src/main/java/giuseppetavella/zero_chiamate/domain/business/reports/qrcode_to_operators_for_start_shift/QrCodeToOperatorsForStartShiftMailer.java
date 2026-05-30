package giuseppetavella.zero_chiamate.domain.business.reports.qrcode_to_operators_for_start_shift;

import giuseppetavella.zero_chiamate.config.AppEnvironment;
import giuseppetavella.zero_chiamate.config.EmailTemplate;
import giuseppetavella.zero_chiamate.domain.business.AppQrCodeGenerator;
import giuseppetavella.zero_chiamate.domain.business.reports.qrcode_to_operators_for_start_shift.params.QrCodeOperatorStartShiftEmailParams;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email.params.EmailTemplateParams;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QrCodeToOperatorsForStartShiftMailer {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AppQrCodeGenerator appQrCodeGenerator;
    
    @Autowired
    private AppEnvironment appEnvironment;


    /**
     * For testing purposes, for now, the email is sent to the admin,
     * but the message is for the operator.
     * (operator has no email as we defined it)
     * 
     * @param operator
     * @param admin
     */
    public void send(User operator, User admin)
    {
        
        var emailParams = new QrCodeOperatorStartShiftEmailParams(
                operator.getFirstname()
        );
        
        var qrCode = appQrCodeGenerator.generatePrivateForStartOperatorShift();
        
        var attachment = new EmailAttachment(
                qrCode.bytes(),
                qrCode.originalFilename()
        );
        
        var subject = "Codice QR per il turno di oggi";
        
        if(appEnvironment.isLocal()) {
            subject += " [LOCAL ENV]";    
        }

        emailService.sendTemplate(new EmailTemplateParams(
                EmailTemplate.QRCODE_OPERATOR_START_SHIFT,
                toTemplateVars(emailParams),
                admin.getEmail(),
                subject,
                attachment
        ));
        

    }


    /**
     * Generate the email params.
     *
     * @return
     */
    private Map<String, Object> toTemplateVars(QrCodeOperatorStartShiftEmailParams params) {
        return Map.of(
               "firstname", params.firstname() 
        );
    }


}
