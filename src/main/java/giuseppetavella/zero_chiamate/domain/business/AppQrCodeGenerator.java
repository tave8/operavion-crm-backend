package giuseppetavella.zero_chiamate.domain.business;

import com.stripe.service.TokenService;
import giuseppetavella.zero_chiamate.config.AppEnvironment;
import giuseppetavella.zero_chiamate.config.FrontendRoutes;
import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.UploadedFilesService;
import giuseppetavella.zero_chiamate.infrastructure.qr_code.dto.PrivateGeneratedQrCode;
import giuseppetavella.zero_chiamate.infrastructure.qr_code.dto.PublicGeneratedQrCode;
import giuseppetavella.zero_chiamate.infrastructure.qr_code.QrCodeService;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileStorageService;
import giuseppetavella.zero_chiamate.security.TokenTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class AppQrCodeGenerator {

    @Autowired
    private QrCodeService qrCodeService;
    
    @Autowired
    private UploadedFilesService uploadedFilesService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private AppEnvironment appEnvironment;
    
    @Autowired
    private FrontendRoutes frontendRoutes;
    
    @Autowired
    private TokenTools tokenTools;
    
    private static final String COMPANY_WEBSITE = "zerochiamate.com";
    
    // generate with company name
    public byte[] generate(String content) {
        return qrCodeService.generate(content, COMPANY_WEBSITE);
    }
            
    
    public PublicGeneratedQrCode generatePublicForLoginPage() {
        var loginPage = "https://app.zerochiamate.com/auth/login";
        var bytes = generate(loginPage);
        
        // upload to cloud & save in DB
        var uploadedFile = uploadedFilesService.upload(bytes, "qrcode_login_page.png");
        var publicUrl = fileStorageService.buildUrl(uploadedFile.getStorageKey());
        
        return new PublicGeneratedQrCode(
                bytes,
                publicUrl,
                uploadedFile.getId(),
                uploadedFile.getStorageKey(),
                uploadedFile.getOriginalFilename()
        );
    }

    public PrivateGeneratedQrCode generatePrivateForStartOperatorShift() {
        var randomId = UUID.randomUUID();
        
        var token = tokenTools.generateToken(randomId.toString(), Duration.ofHours(1));
        
        var urlStartOperatorShift = frontendRoutes.startOperatorShift(token);
        
        var bytes = generate(urlStartOperatorShift);

        // upload to cloud & save in DB
        var uploadedFile = uploadedFilesService.upload(bytes, "qrcode_start_operator_shift.png");
        // var publicUrl = fileStorageService.buildUrl(uploadedFile.getStorageKey());

        return new PrivateGeneratedQrCode(
                bytes,
                uploadedFile.getId(),
                uploadedFile.getOriginalFilename()
        );
    }
    
}
