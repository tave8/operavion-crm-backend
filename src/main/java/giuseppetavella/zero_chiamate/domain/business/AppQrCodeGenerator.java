package giuseppetavella.zero_chiamate.domain.business;

import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.UploadedFilesService;
import giuseppetavella.zero_chiamate.domain.entities.uploaded_files.dto.to_send.UploadedFileDTO;
import giuseppetavella.zero_chiamate.infrastructure.qr_code.PublicGeneratedQrCode;
import giuseppetavella.zero_chiamate.infrastructure.qr_code.QrCodeService;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppQrCodeGenerator {

    @Autowired
    private QrCodeService qrCodeService;
    
    @Autowired
    private UploadedFilesService uploadedFilesService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
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
    
}
